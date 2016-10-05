package com.barclaycardus.ccd.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by abhishek on 02/05/16.
 */
public class DigitSequenceFinder {

    private static final String REGEX_FOR_DIGIT = "[0-9]";

    public List<String> findSequenceFrom(String text, int numberOfDigits) {
        List<String> result = new ArrayList<>();
        if(text != null && !text.isEmpty() && text.length() >= numberOfDigits) {
            Pattern pattern = Pattern.compile(createRegexForNumberContaining(numberOfDigits));
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                result.add(text.substring(matcher.start(), matcher.end()));
            }
        }
        return result;
    }

    private String createRegexForNumberContaining(int numberOfDigits) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < numberOfDigits; i ++) {
            stringBuilder.append(REGEX_FOR_DIGIT);
        }
        return stringBuilder.toString();
    }

}
