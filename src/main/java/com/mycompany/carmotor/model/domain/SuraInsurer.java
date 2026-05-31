package com.mycompany.carmotor.model.domain;

import com.mycompany.carmotor.model.patterns.insurance.DummyVehicle;
import com.mycompany.carmotor.model.patterns.insurance.SuraCalculator;

public class SuraInsurer implements IInsurer {

    @Override
    public double calculatePolicy(double vehicleValue) {
        return new SuraCalculator(new DummyVehicle(vehicleValue))
                .calculatePolicy().getTotalPremium();
    }

    @Override
    public String getEntityName() { return "Seguros Sura"; }
}