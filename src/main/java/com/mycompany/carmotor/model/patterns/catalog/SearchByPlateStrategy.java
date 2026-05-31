package com.mycompany.carmotor.model.patterns.catalog;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.carmotor.model.domain.IVehicle;

public class SearchByPlateStrategy implements VehicleCriteriaStrategy {

    @Override
    public List<IVehicle> apply(List<IVehicle> vehicles, String criteria) {
        if (criteria == null || criteria.isEmpty()) return vehicles;
        List<IVehicle> result = new ArrayList<>();
        try {
            int digit = Integer.parseInt(criteria.trim());
            for (IVehicle v : vehicles) {
                if (v.getLastDigitPlate() == digit) result.add(v);
            }
        } catch (NumberFormatException e) {
            System.out.println("[Strategy] Invalid plate criteria (must be integer): " + criteria);
        }
        return result;
    }
}