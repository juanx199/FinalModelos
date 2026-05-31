package com.mycompany.carmotor.model.patterns.catalog;

import java.util.List;

import com.mycompany.carmotor.model.domain.IVehicle;

public class BranchVehicleIterator implements VehicleIterator {

    private final List<IVehicle> vehicles;
    private int position = 0;

    public BranchVehicleIterator(List<IVehicle> vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public boolean hasNext() {
        return position < vehicles.size();
    }

    @Override
    public IVehicle next() {
        if (!hasNext()) return null;
        return vehicles.get(position++);
    }

    @Override
    public void reset() {
        position = 0;
    }

    @Override
    public IVehicle current() {
        if (position < 0 || position >= vehicles.size()) return null;
        return vehicles.get(position);
    }
}