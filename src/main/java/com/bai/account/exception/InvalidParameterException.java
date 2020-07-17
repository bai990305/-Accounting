package com.bai.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidParameterException extends ServiceException {
    /**
     * Constructor for InvalidParameterException.
     * @param message thrown message.
     */
    public InvalidParameterException(String message) {
        super(message);
        this.setStatusCode(HttpStatus.BAD_REQUEST.value());
        this.setErrorCode(BizErrorCode.INVALID_PARAMETER);
        this.setErrorType(ErrorType.Client);
    }
}
