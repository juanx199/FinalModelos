package com.mycompany.carmotor.model.patterns.catalog;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.carmotor.model.domain.IVehicle;
import com.mycompany.carmotor.model.domain.VehicleType;

public class SearchByTypeStrategy implements VehicleCriteriaStrategy {

    @Override
    public List<IVehicle> apply(List<IVehicle> vehicles, String criteria) {
        if (criteria == null || criteria.isEmpty()) {
            return vehicles;
        }
        List<IVehicle> result = new ArrayList<>();
        VehicleType searchType = VehicleType.fromString(criteria);
        for (IVehicle v : vehicles) {
            if (v.getType() == searchType) {
                result.add(v);
            }
        }
        return result;
    }
}
