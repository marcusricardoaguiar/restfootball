package de.planerio.developertest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class LeaguesByCountryException extends RuntimeException {

    public LeaguesByCountryException(){
        super("There cannot be more than one league per country");
    }
}