package de.planerio.developertest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NameEmptyException extends RuntimeException {

    public NameEmptyException(){
        super("Please, provide a name for this entity");
    }
}