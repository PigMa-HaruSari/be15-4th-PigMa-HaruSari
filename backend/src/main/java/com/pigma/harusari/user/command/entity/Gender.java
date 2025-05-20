package com.pigma.harusari.user.command.entity;

import lombok.Getter;

@Getter
public enum Gender {

    NONE(0),
    MALE(1),
    FEMALE(2);

    private final int code;

    Gender(int code) {
        this.code = code;
    }

    public static Gender fromCode(int code) {
        return switch (code) {
            case 1 -> MALE;
            case 2 -> FEMALE;
            default -> NONE;
        };
    }

    public static Gender fromString(String input) {
        return switch (input.toUpperCase()) {
            case "MALE" -> MALE;
            case "FEMALE" -> FEMALE;
            default -> NONE;
        };
    }

}