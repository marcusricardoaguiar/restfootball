package de.planerio.developertest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ShirtNumberException extends RuntimeException {

    public ShirtNumberException(){
        super("Jersery numbers must be in the 1-99 range, inclusive");
    }
}