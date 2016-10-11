package com.barclaycardus.ccd.handler;

import com.barclaycardus.ccd.dto.Log;
import com.barclaycardus.ccd.dto.ResultEntry;
import com.barclaycardus.ccd.utility.DigitSequenceFinder;
import com.barclaycardus.ccd.writer.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 02/05/16.
 */

public class AccountSearchHandler extends SearchHandler {

    private static final String SENSITIVE_DATA_TYPE = "Account Number";
    private static final String ALL_ZERO_15_DIGIT_ACCOUNT_NUMBER = "000000000000000";
    private static final String ALL_ZERO_16_DIGIT_ACCOUNT_NUMBER = "0000000000000000";
    private static final int NUMBER_OF_DIGITS_IN_15_DIGIT_ACCOUNT_NUMBER = 15;
    private static final int NUMBER_OF_DIGITS_IN_16_DIGIT_ACCOUNT_NUMBER = 16;

    private Writer writer;
    private static AccountSearchHandler instance = null;

    private AccountSearchHandler(Writer writer) {
        this.writer = writer;
    }

    public static AccountSearchHandler getInstance(Writer writer) {
        if(instance == null) {
            synchronized(AccountSearchHandler.class) {
                if(instance == null) {
                    instance = new AccountSearchHandler(writer);
                }
            }
        }

        return instance;
    }


    public void handle(Log log) throws IOException {
        List<String> fifteenDigitAccountNumbers = DigitSequenceFinder.findSequenceFrom(log.getDetails(),
                                                            NUMBER_OF_DIGITS_IN_15_DIGIT_ACCOUNT_NUMBER);
        List<String> sixteenDigitAccountNumbers = DigitSequenceFinder.findSequenceFrom(log.getDetails(),
                                                            NUMBER_OF_DIGITS_IN_16_DIGIT_ACCOUNT_NUMBER);
        List<String> validatedAccountNumbers = new ArrayList<>();

        if(!fifteenDigitAccountNumbers.isEmpty()) {
            validatedAccountNumbers.addAll(getValidatedAccountNumber(fifteenDigitAccountNumbers));
        }
        if(!sixteenDigitAccountNumbers.isEmpty()) {
            validatedAccountNumbers.addAll(getValidatedAccountNumber(sixteenDigitAccountNumbers));
        }

        if(!validatedAccountNumbers.isEmpty()) {
             ResultEntry resultEntry = new ResultEntry.Builder()
                    .type(SENSITIVE_DATA_TYPE)
                    .sensitiveData(getConcatenatedAccountNumberOutOf(validatedAccountNumbers))
                    .filePath(log.getFilePath())
                    .loggingTimestamp(log.getTimestamp())
                    .logData(log.getDetails())
                    .build();
            writer.write(resultEntry);
        }

        if(searchHandler != null) {
            searchHandler.handle(log);
        }
    }

    private List<String> getValidatedAccountNumber(List<String> accountNumbers) throws IOException{
        List<String> validatedAccountNumbers = new ArrayList<>();
        for(String accountNumber : accountNumbers) {
            if(validateAccountNumberByModTenCheck(accountNumber)) {
                validatedAccountNumbers.add(accountNumber);
            }
        }
        return validatedAccountNumbers;
    }

    private boolean validateAccountNumberByModTenCheck(String accountNumber) {
        boolean result = false;
        int numberOfDigits = accountNumber.length();
        if(!accountNumber.equals((numberOfDigits == NUMBER_OF_DIGITS_IN_15_DIGIT_ACCOUNT_NUMBER) ?
                                        ALL_ZERO_15_DIGIT_ACCOUNT_NUMBER : ALL_ZERO_16_DIGIT_ACCOUNT_NUMBER)) {
            int[] digitHolder;
            if(numberOfDigits == NUMBER_OF_DIGITS_IN_15_DIGIT_ACCOUNT_NUMBER) {
                digitHolder = new int[NUMBER_OF_DIGITS_IN_15_DIGIT_ACCOUNT_NUMBER];
            } else {
                digitHolder = new int[NUMBER_OF_DIGITS_IN_16_DIGIT_ACCOUNT_NUMBER];
            }
            int sum = 0;
            int count = 0;
            int weight = 1;

            for(int i = 0; i < accountNumber.length(); i++) {
                digitHolder[count++] = accountNumber.charAt(i) - '0';
            }

            for(int i = (count - 1); i >= 0; i--) {
                int value = digitHolder[i] * weight;
                if(value > 9) {
                    value -= 9;
                }
                sum += value;
                weight = (weight == 1) ? 2 : 1;
            }
            result = ((sum % 10) == 0);
        }
        return result;
    }

    private String getConcatenatedAccountNumberOutOf(List<String> accountNumbers) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(accountNumbers.get(0));
        for(int i = 1; i < accountNumbers.size(); i++) {
            stringBuilder.append(", " + accountNumbers.get(i));
        }
        return stringBuilder.toString();
    }

}
