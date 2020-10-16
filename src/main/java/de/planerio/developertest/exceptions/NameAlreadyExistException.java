package de.planerio.developertest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NameAlreadyExistException extends RuntimeException {

    public NameAlreadyExistException(){
        super("Name already exist. Please, provide another name!");
    }
}