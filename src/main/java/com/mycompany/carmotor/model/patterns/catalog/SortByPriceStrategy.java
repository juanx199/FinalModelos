package com.mycompany.carmotor.model.patterns.catalog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.mycompany.carmotor.model.domain.IVehicle;

public class SortByPriceStrategy implements VehicleCriteriaStrategy {

    @Override
    public List<IVehicle> apply(List<IVehicle> vehicles, String criteria) {
        List<IVehicle> result = new ArrayList<>(vehicles);
        boolean asc = criteria == null || !criteria.equalsIgnoreCase("desc");
        result.sort(asc
                ? Comparator.comparingDouble(IVehicle::getPrice)
                : Comparator.comparingDouble(IVehicle::getPrice).reversed());
        return result;
    }
}