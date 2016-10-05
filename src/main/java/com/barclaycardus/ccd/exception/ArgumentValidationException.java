package com.barclaycardus.ccd.exception;

/**
 * Created by abiswas on 5/3/2016.
 */
public class ArgumentValidationException extends Exception {

    public ArgumentValidationException() {
        super();
    }

    public ArgumentValidationException(String message) {
        super(message);
    }

    public ArgumentValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
