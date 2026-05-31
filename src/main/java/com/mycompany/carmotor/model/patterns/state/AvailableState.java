package com.mycompany.carmotor.model.patterns.state;

import com.mycompany.carmotor.model.domain.IVehicle;

public class AvailableState implements VehicleState {

    @Override
    public void startNegotiation(IVehicle vehicle) {
        System.out.println("[State] Vehículo '" + vehicle.getBrand() + " " + vehicle.getModel()
                + "' → Iniciando negociación...");
        vehicle.setState(new InNegotiationState());
        System.out.println("[State] Estado cambiado a: " + vehicle.getStateName());
    }

    @Override
    public void confirmSale(IVehicle vehicle) {
        System.out.println("[State] ✗ No se puede confirmar venta. Primero debe iniciar una negociación.");
    }

    @Override
    public void cancelNegotiation(IVehicle vehicle) {
        System.out.println("[State] ✗ No se puede cancelar. No hay negociación activa.");
    }

    @Override
    public String getStateName() {
        return "AVAILABLE";
    }
} 