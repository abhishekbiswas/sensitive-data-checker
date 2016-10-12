package com.barclaycardus.ccd.validator;

import com.barclaycardus.ccd.dto.ArgumentData;
import com.barclaycardus.ccd.exception.ArgumentValidationException;

import java.io.File;
import java.io.IOException;


/**
 * Created by abhishek on 30/04/16.
 */
public class DefaultArgumentValidator implements ArgumentValidator {

    @Override
    public void validate(ArgumentData argumentData) throws IOException, ArgumentValidationException {
        validateFolderPath(argumentData.getInputFolderPath());
    }

    private void validateFolderPath(String path) throws ArgumentValidationException {
        File file = new File(path);
        if(!file.exists() || !file.isDirectory() || !file.canRead()) {
            throw new ArgumentValidationException("Input folder path validation failed.");
        }
    }

}
