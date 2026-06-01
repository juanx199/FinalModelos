package com.mycompany.carmotor.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mycompany.carmotor.model.domain.IInsurer;
import com.mycompany.carmotor.model.domain.IVehicle;
import com.mycompany.carmotor.model.domain.MaintenanceRecord;
import com.mycompany.carmotor.model.domain.Vehicle;
import com.mycompany.carmotor.model.domain.VehicleType;
import com.mycompany.carmotor.model.patterns.catalog.BranchCatalog;
import com.mycompany.carmotor.model.patterns.catalog.FilteredVehicleIterator;
import com.mycompany.carmotor.model.patterns.catalog.SearchByBrandStrategy;
import com.mycompany.carmotor.model.patterns.catalog.SearchByPlateStrategy;
import com.mycompany.carmotor.model.patterns.catalog.SearchByTypeStrategy;
import com.mycompany.carmotor.model.patterns.catalog.SortByPriceStrategy;
import com.mycompany.carmotor.model.patterns.catalog.VehicleCriteriaStrategy;
import com.mycompany.carmotor.model.patterns.creational.InsuranceFactory;
import com.mycompany.carmotor.model.patterns.insurance.InsuranceQuote;
import com.mycompany.carmotor.repository.VehicleRepository;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
    }

    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

    // Búsquedas
    public List<Vehicle> searchByBrand(String brand) {
        return vehicleRepository.findByBrandIgnoreCase(brand);
    }

    public List<Vehicle> searchByType(String type) {
        return vehicleRepository.findByType(VehicleType.fromString(type));
    }

    public List<Vehicle> searchByLastDigitPlate(int lastDigitPlate) {
        return vehicleRepository.findByLastDigitPlate(lastDigitPlate);
    }

    public List<Vehicle> searchByModel(int model) {          // ← int, no String
        return vehicleRepository.findByModel(model);
    }

        public List<Vehicle> searchByCriteria(String brand, String type,
            Integer model, Integer lastDigitPlate, Boolean insurable, String priceRange) {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        VehicleType typeFilter = (type == null || type.isBlank())
                ? null
                : VehicleType.fromString(type);

        return vehicles.stream()
                .filter(v -> brand == null || brand.isBlank()
                        || v.getBrand().equalsIgnoreCase(brand.trim()))
                .filter(v -> typeFilter == null || v.getType() == typeFilter)
                .filter(v -> model == null || v.getModel() == model)
                .filter(v -> lastDigitPlate == null || v.getLastDigitPlate() == lastDigitPlate)
            .filter(v -> insurable == null || v.isInsurable() == insurable)
            .filter(v -> priceRange == null || priceRange.isBlank()
                || matchesPriceRange(v.getPrice(), priceRange))
                .toList();
    }

        private boolean matchesPriceRange(double price, String priceRange) {
        return switch (priceRange) {
            case "0_50000000" -> price <= 50000000;
            case "50000001_80000000" -> price >= 50000001 && price <= 80000000;
            case "80000001_110000000" -> price >= 80000001 && price <= 110000000;
            case "110000001_plus" -> price >= 110000001;
            default -> true;
        };
        }

    public List<Vehicle> getByState(String stateName) {
        return vehicleRepository.findByStateName(stateName);
    }

    // Ordenamientos
    public List<Vehicle> getAllOrderByPriceAsc() {
        return vehicleRepository.findAllByOrderByPriceAsc();
    }

    public List<Vehicle> getAllOrderByPriceDesc() {
        return vehicleRepository.findAllByOrderByPriceDesc();
    }

    public List<Vehicle> getAllOrderByModel() {
        return vehicleRepository.findAllByOrderByModelDesc();
    }

    public List<Vehicle> getAllOrderByPassengerCapacity() {
        return vehicleRepository.findAllByOrderByPassengerCapacityAsc();
    }

    // Historial de mantenimiento
    public List<MaintenanceRecord> getMaintenanceHistory(Long vehicleId) {
        Vehicle vehicle = getVehicleById(vehicleId);
        return vehicle.getMaintenanceHistory();
    }

    public Vehicle addMaintenanceRecord(Long vehicleId, MaintenanceRecord record) {
        Vehicle vehicle = getVehicleById(vehicleId);
        vehicle.addMaintenance(record);
        return vehicleRepository.save(vehicle);
    }

    // Cotización de seguros
    public List<InsuranceQuote> getInsuranceQuotes(Long vehicleId) {
        Vehicle vehicle = getVehicleById(vehicleId);
        if (!vehicle.isInsurable()) {
            return List.of();
        }

        List<String> insurers = InsuranceFactory.listAvailableInsurers();
        List<InsuranceQuote> quotes = new ArrayList<>();

        for (String insurerName : insurers) {
            IInsurer insurer = InsuranceFactory.getInsurer(insurerName);
            double premium = insurer.calculatePolicy(vehicle.getPrice());
            quotes.add(new InsuranceQuote(
                insurer.getEntityName(), vehicle, 0, 0, 0, premium
            ));
        }
        return quotes;
    }

    // State pattern
    public Vehicle startNegotiation(Long vehicleId) {
        Vehicle vehicle = getVehicleById(vehicleId);
        vehicle.startNegotiation();
        return vehicleRepository.save(vehicle);
    }

    public Vehicle confirmSale(Long vehicleId) {
        Vehicle vehicle = getVehicleById(vehicleId);
        vehicle.confirmSale();
        return vehicleRepository.save(vehicle);
    }

    public Vehicle cancelNegotiation(Long vehicleId) {
        Vehicle vehicle = getVehicleById(vehicleId);
        vehicle.cancelNegotiation();
        return vehicleRepository.save(vehicle);
    }

    // En VehicleService.java — agrega este método
    public List<IVehicle> searchWithIterator(String strategyType, String criteria) {
        // 1. Obtenemos todos los vehículos de la BD
        List<IVehicle> allVehicles = new ArrayList<>(vehicleRepository.findAll());

        // 2. Creamos el catálogo con esos vehículos
        BranchCatalog catalog = new BranchCatalog(allVehicles);

        // 3. Seleccionamos la estrategia
        VehicleCriteriaStrategy strategy = switch (strategyType) {
            case "brand" ->
                new SearchByBrandStrategy();
            case "type" ->
                new SearchByTypeStrategy();
            case "plate" ->
                new SearchByPlateStrategy();
            case "price" ->
                new SortByPriceStrategy();
            default ->
                new SearchByBrandStrategy();
        };

        // 4. Creamos el FilteredVehicleIterator — aquí está el patrón en acción
        FilteredVehicleIterator iterator = new FilteredVehicleIterator(catalog, strategy, criteria);

        // 5. Retornamos los resultados filtrados
        return iterator.getFilteredVehicles();
    }
}
