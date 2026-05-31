package com.mycompany.carmotor.model.domain;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "MaintenanceRecords")
public class MaintenanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private boolean oilChange;
    private boolean brakeInspection;
    private String workshop;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    public MaintenanceRecord() {}

    public MaintenanceRecord(LocalDate date, boolean oilChange, boolean brakeInspection, String workshop) {
        this.date = date;
        this.oilChange = oilChange;
        this.brakeInspection = brakeInspection;
        this.workshop = workshop;
    }

    public Long getId() { return id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public boolean isOilChange() { return oilChange; }
    public void setOilChange(boolean oilChange) { this.oilChange = oilChange; }
    public boolean isBrakeInspection() { return brakeInspection; }
    public void setBrakeInspection(boolean brakeInspection) { this.brakeInspection = brakeInspection; }
    public String getWorkshop() { return workshop; }
    public void setWorkshop(String workshop) { this.workshop = workshop; }
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Mantenimiento [").append(date).append("] en taller '").append(workshop).append("': ");
        if (oilChange) sb.append("Cambio de Aceite. ");
        if (brakeInspection) sb.append("Revisión de Frenos. ");
        if (!oilChange && !brakeInspection) sb.append("Mantenimiento general.");
        return sb.toString().trim();
    }
}