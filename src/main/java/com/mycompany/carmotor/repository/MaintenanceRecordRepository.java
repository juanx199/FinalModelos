package com.mycompany.carmotor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.carmotor.model.domain.MaintenanceRecord;

@Repository
public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {

    // Historial de mantenimiento de un vehículo específico
    List<MaintenanceRecord> findByVehicleId(Long vehicleId);

    // Mantenimientos por taller
    List<MaintenanceRecord> findByWorkshopIgnoreCase(String workshop);
}