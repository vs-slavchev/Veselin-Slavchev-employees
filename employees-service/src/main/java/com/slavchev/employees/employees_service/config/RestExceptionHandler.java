package com.slavchev.employees.employees_service.config;

import com.slavchev.employees.employees_service.exceptions.InvalidCsvException;
import com.slavchev.employees.employees_service.exceptions.UploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private final static String ERROR_FIELD = "error";

    @ExceptionHandler(InvalidCsvException.class)
    public ResponseEntity<Object> handleInvalidCsvException(final InvalidCsvException invalidCsvException) {
        logger.error(invalidCsvException.getMessage(), invalidCsvException);
        return new ResponseEntity<>(Map.of(ERROR_FIELD, invalidCsvException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UploadException.class)
    public ResponseEntity<Object> handleUploadException(final UploadException uploadException) {
        logger.error(uploadException.getMessage(), uploadException);
        return new ResponseEntity<>(Map.of(ERROR_FIELD, uploadException.getMessage()), HttpStatus.BAD_REQUEST);
    }
}