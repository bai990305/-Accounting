package com.bai.account.exception;

import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //DRY : Don't repeat yourself
    @ExceptionHandler(ServiceException.class)
    ResponseEntity<?> handleServiceException(ServiceException ex) {
        val errorResponse = ErrorResponse.builder()
                                         .code(ex.getErrorCode())
                                         .errorType(ex.getErrorType())
                                         .statusCode(ex.getStatusCode())
                                         .message(ex.getMessage())
                                         .build();
        return ResponseEntity.status(ex.getStatusCode() != 0 ? ex.getStatusCode()
                                     : HttpStatus.INTERNAL_SERVER_ERROR.value())
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse);
    }
}
