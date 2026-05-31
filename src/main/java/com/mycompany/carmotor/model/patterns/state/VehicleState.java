package com.mycompany.carmotor.model.patterns.state;

import com.mycompany.carmotor.model.domain.IVehicle;

public interface VehicleState {
    void startNegotiation(IVehicle vehicle);
    void confirmSale(IVehicle vehicle);
    void cancelNegotiation(IVehicle vehicle);
    String getStateName();
}