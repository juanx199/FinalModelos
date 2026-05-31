package com.mycompany.carmotor.model.patterns.testdrive;

import com.mycompany.carmotor.model.domain.IVehicle;
import com.mycompany.carmotor.model.domain.TestDriveSlot;
import java.time.LocalDateTime;

public class TestDriveEvent {
    private final String type;
    private final TestDriveSlot slot;
    private final IVehicle vehicle;
    private final LocalDateTime timestamp;

    public TestDriveEvent(String type, TestDriveSlot slot, IVehicle vehicle) {
        this.type = type;
        this.slot = slot;
        this.vehicle = vehicle;
        this.timestamp = LocalDateTime.now();
    }

    public String getType()            { return type; }
    public TestDriveSlot getSlot()     { return slot; }
    public IVehicle getVehicle()       { return vehicle; }
    public LocalDateTime getTimestamp(){ return timestamp; }

    @Override
    public String toString() {
        return "TestDriveEvent [" + type + "] Slot: " + (slot != null ? slot.getSlotId() : "N/A")
        + " | Vehículo: " + (vehicle != null ? vehicle.getBrand() + " " + vehicle.getModel() : "N/A")
        + " | Timestamp: " + timestamp;
    }
}