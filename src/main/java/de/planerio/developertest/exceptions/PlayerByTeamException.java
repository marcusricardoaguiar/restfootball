package de.planerio.developertest.exceptions;

import de.planerio.developertest.constants.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class PlayerByTeamException extends RuntimeException {

    public PlayerByTeamException(){
        super("There cannot be more than " + Constants.PLAYERS_PER_TEAM + " players in a team");
    }
}