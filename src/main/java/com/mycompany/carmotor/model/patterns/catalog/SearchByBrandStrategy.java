package com.mycompany.carmotor.model.patterns.catalog;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.carmotor.model.domain.IVehicle;

public class SearchByBrandStrategy implements VehicleCriteriaStrategy {

    @Override
    public List<IVehicle> apply(List<IVehicle> vehicles, String criteria) {
        if (criteria == null || criteria.isEmpty()) return vehicles;
        List<IVehicle> result = new ArrayList<>();
        for (IVehicle v : vehicles) {
            if (v.getBrand().equalsIgnoreCase(criteria)) result.add(v);
        }
        return result;
    }
}