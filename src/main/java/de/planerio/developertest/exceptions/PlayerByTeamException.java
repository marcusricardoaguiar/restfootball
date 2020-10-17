package de.planerio.developertest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class PlayerByTeamException extends RuntimeException {

    public PlayerByTeamException(){
        super("There cannot be more than 25 players in a team");
    }
}