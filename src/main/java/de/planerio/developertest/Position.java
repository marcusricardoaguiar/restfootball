package de.planerio.developertest;

import java.util.stream.Stream;

public enum Position {
    GK("GK"), CB("CB"), RB("RB"), LB("LB"), LWB("LWB"),
        RWB("RWB"), CDM("CDM"), CM("CM"), LM("LM"),
        RM("RM"), CAM("CAM"), ST("ST"), CF("CF");

    private String position;

    private Position(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public static Position of(String position) {
        return Stream.of(Position.values())
                .filter(p -> p.getPosition().equals(position))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}