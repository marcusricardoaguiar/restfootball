package de.planerio.developertest.exceptions;

import de.planerio.developertest.services.converters.CountryConverter;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GeneralException
        extends ResponseEntityExceptionHandler {

    static Logger logger = LoggerFactory.getLogger(CountryConverter.class);

    @ExceptionHandler(value
            = { IllegalArgumentException.class, IllegalStateException.class, ConstraintViolationException.class })
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        logger.info("GENERAL_EXCEPTION -> Error Message: " + ex.getMessage());
        String bodyOfResponse = "An unexpected error has occurred. The error has been logged and is being investigated.";
        return handleExceptionInternal(ex, ResponseEntity.status(HttpStatus.CONFLICT).body(bodyOfResponse),
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}

