package com.mycompany.carmotor.model.patterns.insurance;

import com.mycompany.carmotor.model.domain.IVehicle;

public class AxaColpatriaCalculator extends InsuranceCalculator {

    public AxaColpatriaCalculator(IVehicle vehicle) {
        super("Seguros Axa Colpatria", vehicle);
    }

    @Override
    protected double assessRisk() { return 150000; }

    @Override
    protected double applyDiscount() {
        return vehicle.getModel() > 2020 ? 50000 : 0;
    }

    @Override
    protected double getBaseRate() { return 0.06; }
}