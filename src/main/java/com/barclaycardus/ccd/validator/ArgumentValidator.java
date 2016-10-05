package com.barclaycardus.ccd.validator;

import com.barclaycardus.ccd.dto.ArgumentData;
import com.barclaycardus.ccd.exception.ArgumentValidationException;

import java.io.IOException;

/**
 * Created by abhishek on 30/04/16.
 */
public interface  ArgumentValidator {

    void validate(ArgumentData argumentData) throws IOException, ArgumentValidationException;

}
