package com.mycompany.carmotor.model.domain;

public class FalabellaInsurer implements IInsurer {

    @Override
    public double calculatePolicy(double vehicleValue) {
        return vehicleValue * 0.05;
    }

    @Override
    public String getEntityName() { return "Seguros Falabella"; }
}