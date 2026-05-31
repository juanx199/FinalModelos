package com.mycompany.carmotor.model.domain;

public enum PhotoCategory {
    EXTERIOR,
    INTERIOR,
    ENGINE,
    OTHER;

    public static PhotoCategory fromString(String value) {
        if (value == null) return OTHER;
        return switch (value.toUpperCase().trim()) {
            case "EXTERIOR"         -> EXTERIOR;
            case "INTERIOR"         -> INTERIOR;
            case "ENGINE",
            "MOTOR"            -> ENGINE;
            default                 -> OTHER;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case EXTERIOR -> "Exterior";
            case INTERIOR -> "Interior";
            case ENGINE   -> "Engine";
            case OTHER    -> "Other";
        };
    }
}