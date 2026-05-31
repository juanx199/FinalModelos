package com.mycompany.carmotor.model.patterns.state;

import com.mycompany.carmotor.model.domain.IVehicle;

public class SoldState implements VehicleState {

    @Override
    public void startNegotiation(IVehicle vehicle) {
        System.out.println("[State] ✗ El vehículo '" + vehicle.getBrand() + " " + vehicle.getModel()
                + "' ya fue vendido.");
    }

    @Override
    public void confirmSale(IVehicle vehicle) {
        System.out.println("[State] ✗ El vehículo '" + vehicle.getBrand() + " " + vehicle.getModel()
                + "' ya fue vendido.");
    }

    @Override
    public void cancelNegotiation(IVehicle vehicle) {
        System.out.println("[State] ✗ El vehículo '" + vehicle.getBrand() + " " + vehicle.getModel()
                + "' ya fue vendido. No hay negociación que cancelar.");
    }

    @Override
    public String getStateName() {
        return "SOLD";
    }
}