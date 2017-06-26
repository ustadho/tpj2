package com.tpj.packinglist.exception;

public class RestServerException extends RuntimeException {

    public RestServerException() {
    }

    public RestServerException(String message) {
        super(message);
    }

    public RestServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestServerException(Throwable cause) {
        super(cause);
    }

    public RestServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
