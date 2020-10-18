package de.planerio.developertest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class WrongParameterException extends RuntimeException {

    public WrongParameterException(){
        super("The query parameter provided is not correct");
    }
}