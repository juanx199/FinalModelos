package com.mycompany.carmotor.model.patterns.insurance;

import java.util.Collections;
import java.util.List;

import com.mycompany.carmotor.model.domain.IVehicle;
import com.mycompany.carmotor.model.domain.VehicleType;

public class DummyVehicle implements IVehicle {

    private final double price;

    public DummyVehicle(double price) {
        this.price = price;
    }

    @Override public double getPrice() { return price; }
    @Override public void showDetail() {}
    @Override public List<String> getPhotoPaths() { return Collections.emptyList(); }
    @Override public String getBrand() { return "Generic"; }
    @Override public int getModel() { return 0; }
    @Override public VehicleType getType() { return VehicleType.UNKNOWN; }
    @Override public int getPassengerCapacity() { return 0; }
    @Override public int getLastDigitPlate() { return 0; }
}