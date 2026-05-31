package com.mycompany.carmotor.model.patterns.catalog;

import com.mycompany.carmotor.model.domain.IVehicle;

public interface VehicleIterator {
    boolean hasNext();
    IVehicle next();
    void reset();
    IVehicle current();
}