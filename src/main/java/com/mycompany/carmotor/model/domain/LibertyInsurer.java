package com.mycompany.carmotor.model.domain;

import com.mycompany.carmotor.model.patterns.insurance.DummyVehicle;
import com.mycompany.carmotor.model.patterns.insurance.LibertyCalculator;

public class LibertyInsurer implements IInsurer {

    @Override
    public double calculatePolicy(double vehicleValue) {
        return new LibertyCalculator(new DummyVehicle(vehicleValue))
                .calculatePolicy().getTotalPremium();
    }

    @Override
    public String getEntityName() { return "Seguros Liberty"; }
}