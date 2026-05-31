package com.mycompany.carmotor.model.patterns.insurance;

import com.mycompany.carmotor.model.domain.IVehicle;

public class LibertyCalculator extends InsuranceCalculator {

    public LibertyCalculator(IVehicle vehicle) {
        super("Seguros Liberty", vehicle);
    }

    @Override
    protected double assessRisk() {
        return vehicle.getPassengerCapacity() > 5 ? 90000 : 40000;
    }

    @Override
    protected double applyDiscount() {
        return vehicle.getBrand().equalsIgnoreCase("Toyota") ? 25000 : 0;
    }

    @Override
    protected double getBaseRate() { return 0.055; }
}
