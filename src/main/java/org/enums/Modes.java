package org.enums;

import lombok.Getter;

public enum Modes {
    ARCADE("a"),
    REALISTIC("r"),
    SIMULATION("s");

    Modes(String s) {
        letter = s;
    }

    @Getter
    private String letter;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
