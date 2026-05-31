package com.mycompany.carmotor.model.patterns.creational;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.carmotor.model.domain.IVehicle;
import com.mycompany.carmotor.model.domain.VehicleType;
import com.mycompany.carmotor.model.patterns.structural.VehicleProxy;

public class VehicleBuilder {

    private String brand;
    private String year;
    private Double price;
    private VehicleType type;
    private int passengerCapacity;
    private int lastDigitPlate;
    private List<String> photoPaths;

    public VehicleBuilder() {
        this.reset();
    }

    public final void reset() {
        this.brand = null;
        this.year = null;
        this.type = null;
        this.passengerCapacity = 0;
        this.lastDigitPlate = -1;
        this.photoPaths = new ArrayList<>();
        System.out.println("[Builder] -> State reset.");
    }

    public VehicleBuilder withBasicData(String brand, String year, int lastDigitPlate) {
        this.brand = brand;
        this.year = year;
        this.lastDigitPlate = lastDigitPlate;
        System.out.println("[Builder] -> Basic data set: " + brand + " " + year + " (Plate: " + lastDigitPlate + ")");
        return this;
    }

    public VehicleBuilder withSpecs(VehicleType type, int passengerCapacity, double price) {
        this.type = type;
        this.passengerCapacity = passengerCapacity;
        this.price = price;
        System.out.println("[Builder] -> Specs set: " + type + " (Price: $" + price + ")");
        return this;
    }

    public VehicleBuilder addPhoto(String path) {
        this.photoPaths.add(path);
        System.out.println("[Builder] -> Photo added: " + path + " (Total: " + photoPaths.size() + ")");
        return this;
    }

    public IVehicle build() {
        if (this.brand == null || this.year == null || this.price == null) {
            throw new IllegalStateException("Missing required fields: brand, year or price.");
        }

        int yearInt;
        try {
            yearInt = Integer.parseInt(this.year);
        } catch (NumberFormatException e) {
            yearInt = 0;
        }

        System.out.println("[Builder] -> Building proxy for " + brand + " " + year + "...");

        VehicleProxy proxy = new VehicleProxy(
                this.brand, yearInt, this.price,
                this.type, this.passengerCapacity,
                this.lastDigitPlate, this.photoPaths
        );

        this.reset();
        return proxy;
    }
}
