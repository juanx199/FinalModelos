package com.mycompany.carmotor.model.patterns.insurance;

import com.mycompany.carmotor.model.domain.IVehicle;

public class SuraCalculator extends InsuranceCalculator {

    public SuraCalculator(IVehicle vehicle) {
        super("Seguros Sura", vehicle);
    }

    @Override
    protected double assessRisk() {
        return vehicle.getLastDigitPlate() % 2 == 0 ? 50000 : 80000;
    }

    @Override
    protected double applyDiscount() {
        return vehicle.getModel() > 2022 ? 30000 : 0;
    }

    @Override
    protected double getBaseRate() { return 0.07; }
}