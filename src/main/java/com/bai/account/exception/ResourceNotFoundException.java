package com.bai.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends ServiceException {
    /**
     * Constructor for ResourceNotFoundException.
     * @param message thrown message.
     */
    public ResourceNotFoundException(String message) {
        super(message);
        this.setStatusCode(HttpStatus.NOT_FOUND.value());
        this.setErrorCode(BizErrorCode.RESOURCE_NOT_FOUND);
        this.setErrorType(ErrorType.Client);
    }
}