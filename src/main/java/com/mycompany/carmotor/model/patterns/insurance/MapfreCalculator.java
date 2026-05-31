package com.mycompany.carmotor.model.patterns.insurance;

import com.mycompany.carmotor.model.domain.IVehicle;
import com.mycompany.carmotor.model.domain.VehicleType;

public class MapfreCalculator extends InsuranceCalculator {

    public MapfreCalculator(IVehicle vehicle) {
        super("Seguros Mapfre", vehicle);
    }

    @Override
    protected double assessRisk() {
        VehicleType type = vehicle.getType();
        return (type == VehicleType.SUV || type == VehicleType.PICKUP_4X4) ? 120000 : 60000;
    }

    @Override
    protected double applyDiscount() {
        return vehicle.getPrice() * getBaseRate() * 0.05;
    }

    @Override
    protected double getBaseRate() { return 0.05; }
}