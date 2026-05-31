package com.mycompany.carmotor.model.domain;

import com.mycompany.carmotor.model.patterns.insurance.DummyVehicle;
import com.mycompany.carmotor.model.patterns.insurance.MapfreCalculator;

public class MapfreInsurer implements IInsurer {

    @Override
    public double calculatePolicy(double vehicleValue) {
        return new MapfreCalculator(new DummyVehicle(vehicleValue))
                .calculatePolicy().getTotalPremium();
    }

    @Override
    public String getEntityName() { return "Seguros Mapfre"; }
}
