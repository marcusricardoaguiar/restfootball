package de.planerio.developertest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ShirtNumberByTeamException extends RuntimeException {

    public ShirtNumberByTeamException(){
        super("A team cannot have two players with the same jersey number.");
    }
}