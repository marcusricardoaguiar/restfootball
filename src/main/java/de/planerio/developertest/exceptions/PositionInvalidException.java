package de.planerio.developertest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class PositionInvalidException extends RuntimeException {

    public PositionInvalidException(){
        super("You haven't provide a correct position. Choose one of these: " +
                "'GK', 'CB', 'RB', 'LB', 'LWB', " +
                "'RWB', 'CDM', 'CM', 'LM', 'RM', " +
                "'CAM', 'ST', 'CF'");
    }
}