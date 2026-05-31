package com.mycompany.carmotor.model.domain;

import com.mycompany.carmotor.model.patterns.insurance.AxaColpatriaCalculator;
import com.mycompany.carmotor.model.patterns.insurance.DummyVehicle;

public class AxaColpatriaInsurer implements IInsurer {

    @Override
    public double calculatePolicy(double vehicleValue) {
        return new AxaColpatriaCalculator(new DummyVehicle(vehicleValue))
                .calculatePolicy().getTotalPremium();
    }

    @Override
    public String getEntityName() { return "Seguros Axa Colpatria"; }
}