package com.mycompany.carmotor.model.domain;

public interface IInsurer {
    double calculatePolicy(double vehicleValue);
    String getEntityName();
}