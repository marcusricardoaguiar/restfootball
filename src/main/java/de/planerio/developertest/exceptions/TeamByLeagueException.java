package de.planerio.developertest.exceptions;

import de.planerio.developertest.constants.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class TeamByLeagueException extends RuntimeException {

    public TeamByLeagueException(){
        super("There cannot be more than " + Constants.TEAMS_PER_LEAGUE + " teams per league");
    }
}