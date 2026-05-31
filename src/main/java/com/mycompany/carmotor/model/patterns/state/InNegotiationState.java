package com.mycompany.carmotor.model.patterns.state;

import com.mycompany.carmotor.model.domain.IVehicle;

public class InNegotiationState implements VehicleState {

    @Override
    public void startNegotiation(IVehicle vehicle) {
        System.out.println("[State] ✗ Ya existe una negociación en curso para '"
                + vehicle.getBrand() + " " + vehicle.getModel() + "'.");
    }

    @Override
    public void confirmSale(IVehicle vehicle) {
        System.out.println("[State] Vehículo '" + vehicle.getBrand() + " " + vehicle.getModel()
                + "' → Confirmando venta...");
        vehicle.setState(new SoldState());
        System.out.println("[State] ¡Venta confirmada! Estado: " + vehicle.getStateName());
    }

    @Override
    public void cancelNegotiation(IVehicle vehicle) {
        System.out.println("[State] Vehículo '" + vehicle.getBrand() + " " + vehicle.getModel()
                + "' → Cancelando negociación...");
        vehicle.setState(new AvailableState());
        System.out.println("[State] Negociación cancelada. Estado: " + vehicle.getStateName());
    }

    @Override
    public String getStateName() {
        return "IN_NEGOTIATION";
    }
}