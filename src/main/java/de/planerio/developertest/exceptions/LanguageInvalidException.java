package de.planerio.developertest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class LanguageInvalidException extends RuntimeException {

    public LanguageInvalidException(){
        super("You haven't provide a correct language. Choose one of these: 'de', 'fr', 'en', 'es', 'it'");
    }
}