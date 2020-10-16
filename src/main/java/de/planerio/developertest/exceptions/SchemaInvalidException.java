package de.planerio.developertest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SchemaInvalidException extends RuntimeException {

    public SchemaInvalidException(){
        super("Entity schema is not valid");
    }
}