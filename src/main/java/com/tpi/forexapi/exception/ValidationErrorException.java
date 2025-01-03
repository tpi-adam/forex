package com.tpi.forexapi.exception;

import lombok.Data;

/**
 * 檢核錯誤例外
 */
@Data
public class ValidationErrorException extends RuntimeException {
    private String errorCode;
    private String errorMessage;

    public ValidationErrorException(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
