package com.mycompany.carmotor.model.patterns.catalog;

import java.util.List;

import com.mycompany.carmotor.model.domain.IVehicle;

public class BranchCatalog implements VehicleCatalog {

    private final List<IVehicle> vehicles;

    public BranchCatalog(List<IVehicle> vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public VehicleIterator createIterator() {
        return new BranchVehicleIterator(vehicles);
    }
}