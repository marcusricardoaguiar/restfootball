package de.planerio.developertest.enums;

import de.planerio.developertest.exceptions.PositionInvalidException;

import java.util.stream.Stream;

public enum Position {
    GK("GK"), CB("CB"), RB("RB"), LB("LB"), LWB("LWB"),
        RWB("RWB"), CDM("CDM"), CM("CM"), LM("LM"),
        RM("RM"), CAM("CAM"), ST("ST"), CF("CF");

    private final String position;

    Position(String position) {
        this.position = position;
    }

    public String getValue() {
        return position;
    }

    public static Position of(String position) {
        return Stream.of(Position.values())
                .filter(p -> p.getValue().equals(position))
                .findFirst()
                .orElseThrow(PositionInvalidException::new);
    }
}