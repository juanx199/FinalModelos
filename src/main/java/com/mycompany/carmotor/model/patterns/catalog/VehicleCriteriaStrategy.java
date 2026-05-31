package com.mycompany.carmotor.model.patterns.catalog;

import java.util.List;

import com.mycompany.carmotor.model.domain.IVehicle;

public interface VehicleCriteriaStrategy {
    List<IVehicle> apply(List<IVehicle> vehicles, String criteria);
}