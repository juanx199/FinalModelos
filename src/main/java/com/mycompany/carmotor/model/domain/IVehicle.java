package com.mycompany.carmotor.model.domain;

import java.util.Collections;
import java.util.List;

import com.mycompany.carmotor.model.patterns.state.VehicleState;

public interface IVehicle {

    void showDetail();
    double getPrice();
    List<String> getPhotoPaths();

    String getBrand();
    int getModel();
    VehicleType getType();
    int getPassengerCapacity();
    int getLastDigitPlate();

    default void setState(VehicleState state) {}
    default VehicleState getState() { return null; }
    default void startNegotiation() {}
    default void confirmSale() {}
    default void cancelNegotiation() {}
    default String getStateName() { return "AVAILABLE"; }
    default boolean isInsurable() { return true; }

    default Advisor getAdvisor() { return null; }
    default void setAdvisor(Advisor advisor) {}
    default List<MaintenanceRecord> getMaintenanceHistory() { return Collections.emptyList(); }
    default void addMaintenance(MaintenanceRecord record) {}
}