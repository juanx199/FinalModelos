package com.mycompany.carmotor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.carmotor.model.domain.Vehicle;
import com.mycompany.carmotor.model.domain.VehicleType;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // Búsqueda por marca
    List<Vehicle> findByBrandIgnoreCase(String brand);

    // Búsqueda por tipo (SUV, camioneta, etc)
    List<Vehicle> findByType(VehicleType type);

    // Búsqueda por último dígito de placa
    List<Vehicle> findByLastDigitPlate(int lastDigitPlate);

    // Búsqueda por año
    List<Vehicle> findByModel(int model);

    // Ordenar por precio ascendente
    List<Vehicle> findAllByOrderByPriceAsc();

    // Ordenar por precio descendente
    List<Vehicle> findAllByOrderByPriceDesc();

    // Ordenar por año
    List<Vehicle> findAllByOrderByModelDesc();

    // Ordenar por capacidad de pasajeros
    List<Vehicle> findAllByOrderByPassengerCapacityAsc();

    // Búsqueda por estado (DISPONIBLE, EN NEGOCIACIÓN, VENDIDO)
    List<Vehicle> findByStateName(String stateName);
}