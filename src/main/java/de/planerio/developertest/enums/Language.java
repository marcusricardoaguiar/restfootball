package de.planerio.developertest.enums;

import de.planerio.developertest.exceptions.LanguageInvalidException;

import java.util.stream.Stream;

public enum Language {
    DE("de"), FR("fr"), EN("en"), ES("es"), IT("it");

    private final String language;

    Language(String language) {
        this.language = language;
    }

    public String getValue() {
        return language;
    }

    public static Language of(String lang) {
        return Stream.of(Language.values())
                .filter(p -> p.getValue().equals(lang))
                .findFirst()
                .orElseThrow(LanguageInvalidException::new);
    }
}