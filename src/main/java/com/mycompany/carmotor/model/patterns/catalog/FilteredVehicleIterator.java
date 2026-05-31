package com.mycompany.carmotor.model.patterns.catalog;

import com.mycompany.carmotor.model.domain.IVehicle;
import java.util.ArrayList;
import java.util.List;

/**
 * Iterator concreto que combina Iterator + Strategy.
 * Filtra los vehículos del catálogo usando una estrategia de búsqueda
 * mientras itera, sin necesidad de crear una lista filtrada de antemano.
 */
public class FilteredVehicleIterator implements VehicleIterator {

    private final BranchCatalog catalog;
    private final VehicleCriteriaStrategy strategy;
    private final String criteria;

    private final List<IVehicle> filteredVehicles;
    private int currentIndex = 0;

    public FilteredVehicleIterator(BranchCatalog catalog,
                                    VehicleCriteriaStrategy strategy,
                                    String criteria) {
        this.catalog = catalog;
        this.strategy = strategy;
        this.criteria = criteria;
        this.filteredVehicles = buildFilteredList();
    }

    /**
     * Construye la lista filtrada usando la estrategia al momento de crear el iterador.
     */
    private List<IVehicle> buildFilteredList() {
        // Recolectamos todos los vehículos del catálogo usando su propio iterador
        List<IVehicle> all = new ArrayList<>();
        VehicleIterator baseIterator = catalog.createIterator();
        baseIterator.reset();
        while (baseIterator.hasNext()) {
            all.add(baseIterator.next());
        }
        // Aplicamos la estrategia de filtrado
        return strategy.apply(all, criteria);
    }

    @Override
    public boolean hasNext() {
        return currentIndex < filteredVehicles.size();
    }

    @Override
    public IVehicle next() {
        if (!hasNext()) return null;
        return filteredVehicles.get(currentIndex++);
    }

    @Override
    public void reset() {
        currentIndex = 0;
    }

    @Override
    public IVehicle current() {
        if (currentIndex < 0 || currentIndex >= filteredVehicles.size()) return null;
        return filteredVehicles.get(currentIndex);
    }

    public int size() {
        return filteredVehicles.size();
    }

    public List<IVehicle> getFilteredVehicles() {
        return filteredVehicles;
    }
}