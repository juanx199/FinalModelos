package com.mycompany.carmotor.model.patterns.catalog;

import java.util.List;

import com.mycompany.carmotor.model.domain.IVehicle;

public class VehicleSearchContext {

    private VehicleCriteriaStrategy strategy;
    private final List<IVehicle> vehicles;

    public VehicleSearchContext(List<IVehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void setStrategy(VehicleCriteriaStrategy strategy) {
        this.strategy = strategy;
    }

    public List<IVehicle> executeSearch(String criteria) {
        if (strategy == null) {
            throw new IllegalStateException("Search strategy not set.");
        }
        return strategy.apply(vehicles, criteria);
    }
}