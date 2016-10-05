package com.barclaycardus.ccd.validator;

import com.barclaycardus.ccd.dto.ArgumentData;
import com.barclaycardus.ccd.exception.ArgumentValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;


/**
 * Created by abhishek on 30/04/16.
 */
public class DefaultArgumentValidator implements ArgumentValidator {

    private static final String OUTPUT_FILE_EXTENSION = ".xlsx";

    @Override
    public void validate(ArgumentData argumentData) throws IOException, ArgumentValidationException {
        validateFolderPath(argumentData.getInputFolderPath());
        validateFilePath(argumentData.getOutputFilePath());
    }

    private void validateFolderPath(String path) throws ArgumentValidationException {
        File file = new File(path);
        if(!file.exists() || !file.isDirectory() || !file.canRead()) {
            throw new ArgumentValidationException("Input folder path validation failed.");
        }
    }


    private void validateFilePath(String path) throws IOException, ArgumentValidationException {
        if(path.endsWith(OUTPUT_FILE_EXTENSION)) {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                file.delete();
            } else {
                file.createNewFile();
                file.delete();
            }
        } else {
            throw new ArgumentValidationException("Output file extension mismatch. Output file must be like *.xlsx.");
        }
    }

}
