package com.mycompany.carmotor.model.patterns.structural;

import java.util.List;

import com.mycompany.carmotor.model.domain.Advisor;
import com.mycompany.carmotor.model.domain.IVehicle;
import com.mycompany.carmotor.model.domain.MaintenanceRecord;
import com.mycompany.carmotor.model.domain.Vehicle;
import com.mycompany.carmotor.model.domain.VehicleType;
import com.mycompany.carmotor.model.patterns.state.VehicleState;

public class VehicleProxy implements IVehicle {

    private Vehicle vehicleReal;

    private final String brand;
    private final int year;
    private final double basePrice;
    private final VehicleType type;
    private final int passengerCapacity;
    private final int lastDigitPlate;
    private final List<String> originalPaths;

    public VehicleProxy(String brand, int year, double price, VehicleType type,
                        int passengerCapacity, int lastDigitPlate, List<String> photoPaths) {
        this.brand = brand;
        this.year = year;
        this.basePrice = price;
        this.type = type;
        this.passengerCapacity = passengerCapacity;
        this.lastDigitPlate = lastDigitPlate;
        this.originalPaths = photoPaths;
        this.vehicleReal = null;
        System.out.println("[Proxy] Light proxy created for " + brand + " " + year + " $" + price);
    }

    private void ensureVehicleLoaded() {
        if (this.vehicleReal == null) {
            System.out.println("[Proxy] -> Loading Vehicle (expensive operation)...");
            this.vehicleReal = new Vehicle(brand, year, basePrice, type.toString(),
                    passengerCapacity, lastDigitPlate, originalPaths);
            System.out.println("[Proxy] -> Vehicle loaded successfully.");
        }
    }

    @Override public void showDetail() { ensureVehicleLoaded(); vehicleReal.showDetail(); }
    @Override public double getPrice() { return basePrice; } // dato ligero, no carga el real
    @Override public String getBrand() { return brand; }
    @Override public int getModel() { return year; }
    @Override public VehicleType getType() { return type; }
    @Override public int getPassengerCapacity() { return passengerCapacity; }
    @Override public int getLastDigitPlate() { return lastDigitPlate; }

    @Override public List<String> getPhotoPaths() {
        ensureVehicleLoaded();
        return vehicleReal.getPhotoPaths();
    }

    @Override public Advisor getAdvisor() { ensureVehicleLoaded(); return vehicleReal.getAdvisor(); }
    @Override public void setAdvisor(Advisor advisor) { ensureVehicleLoaded(); vehicleReal.setAdvisor(advisor); }

    @Override public List<MaintenanceRecord> getMaintenanceHistory() {
        ensureVehicleLoaded();
        return vehicleReal.getMaintenanceHistory();
    }

    @Override public void setState(VehicleState state) { ensureVehicleLoaded(); vehicleReal.setState(state); }
    @Override public VehicleState getState() { ensureVehicleLoaded(); return vehicleReal.getState(); }
    @Override public void startNegotiation() { ensureVehicleLoaded(); vehicleReal.startNegotiation(); }
    @Override public void confirmSale() { ensureVehicleLoaded(); vehicleReal.confirmSale(); }
    @Override public void cancelNegotiation() { ensureVehicleLoaded(); vehicleReal.cancelNegotiation(); }
    @Override public String getStateName() { ensureVehicleLoaded(); return vehicleReal.getStateName(); }
}