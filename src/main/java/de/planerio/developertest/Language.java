package de.planerio.developertest;

import java.util.stream.Stream;

public enum Language {
    de("de"), fr("fr"), en("en"), es("es"), it("it");

    private String language;

    private Language(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public static Language of(String language) {
        return Stream.of(Language.values())
                .filter(p -> p.getLanguage().equals(language))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}