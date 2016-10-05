package com.barclaycardus.ccd.writer;

import com.barclaycardus.ccd.dto.ResultEntry;
import com.barclaycardus.ccd.manager.ApplicationManager;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;

/**
 * Created by abhishek on 02/05/16.
 */
public class FileWriter implements Writer {

    private static final String SHEET_NAME = "Result";
    private static final int START_OF_HEADER_ROW = 0;
    private static final int START_OF_DATA_ROW = 1;

    private static final String TYPE_HEADER = "SENSITIVE DATA TYPE";
    private static final String SENSITIVE_DATA_HEADER = "SENSITIVE DATA";
    private static final String FILE_PATH_HEADER = "FILE PATH";
    private static final String LOGGING_TIMESTAMP_HEADER = "LOGGING_TIMESTAMP";
    private static final String LOG_DATA_HEADER = "LOG DATA";


    private int rowNumber = START_OF_DATA_ROW;

    @Autowired
    ApplicationManager applicationManager;


    @Override
    public void write(ResultEntry resultEntry) throws IOException {
        createResultSheetIfNotPresent();

        InputStream fileInputStream = new FileInputStream(applicationManager.getArgumentData().getOutputFilePath());
        XSSFWorkbook workBook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workBook.getSheet(SHEET_NAME);

        FileOutputStream fileOutputStream = new FileOutputStream(applicationManager.getArgumentData().getOutputFilePath());
        createRowWithData(sheet, rowNumber++, resultEntry);

        workBook.write(fileOutputStream);
        fileInputStream.close();
        fileOutputStream.close();
        workBook.close();
    }

    private void createResultSheetIfNotPresent() throws IOException {
        File file = new File(applicationManager.getArgumentData().getOutputFilePath());
        if(!file.exists()) {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(applicationManager.getArgumentData().getOutputFilePath()));
            XSSFWorkbook workBook = new XSSFWorkbook();
            XSSFSheet sheet = workBook.createSheet(SHEET_NAME);

            ResultEntry headerEntry = new ResultEntry.Builder()
                                                .type(TYPE_HEADER)
                                                .sensitiveData(SENSITIVE_DATA_HEADER)
                                                .filePath(FILE_PATH_HEADER)
                                                .loggingTimestamp(LOGGING_TIMESTAMP_HEADER)
                                                .logData(LOG_DATA_HEADER)
                                                .build();

            createRowWithData(sheet, START_OF_HEADER_ROW, headerEntry);

            workBook.write(fileOutputStream);
            fileOutputStream.close();
            workBook.close();
        }
    }

    private void createRowWithData(XSSFSheet sheet, int rowNumber, ResultEntry resultEntry) {
        XSSFRow row = sheet.createRow(rowNumber);

        row.createCell(0).setCellValue(resultEntry.getType());
        row.createCell(1).setCellValue(resultEntry.getSensitiveData());
        row.createCell(2).setCellValue(resultEntry.getFilePath());
        row.createCell(3).setCellValue(resultEntry.getLoggingTimestamp());
        row.createCell(4).setCellValue(resultEntry.getLogData());
    }

}