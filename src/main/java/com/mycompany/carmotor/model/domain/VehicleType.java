package com.mycompany.carmotor.model.domain;

public enum VehicleType {
    SUV,
    PICKUP_4X4,
    SEDAN,
    HATCHBACK,
    OTHER,
    UNKNOWN; 

    public static VehicleType fromString(String value) {
        if (value == null) return OTHER;
        return switch (value.toUpperCase().trim().replace(" ", "_")) {
            case "SUV"          -> SUV;
            case "PICKUP_4X4",
            "CAMIONETA_4X4",
            "CAMIONETA 4X4" -> PICKUP_4X4;
            case "SEDAN",
            "SEDÁN"         -> SEDAN;
            case "HATCHBACK"    -> HATCHBACK;
            case "UNKNOWN"      -> UNKNOWN;
            default             -> OTHER;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case SUV        -> "SUV";
            case PICKUP_4X4 -> "Pickup 4x4";
            case SEDAN      -> "Sedan";
            case HATCHBACK  -> "Hatchback";
            case OTHER      -> "Other";
            case UNKNOWN    -> "Unknown";
        };
    }
}