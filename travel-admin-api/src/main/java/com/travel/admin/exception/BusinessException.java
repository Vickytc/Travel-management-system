package com.travel.admin.exception;

/**
 * Custom business exceptions
 */
public class BusinessException extends RuntimeException {
    private Integer errorCode;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
