package com.mycompany.carmotor.model.domain;

import java.time.LocalDateTime;

public class TestDriveSlot {

    private String slotId;
    private LocalDateTime dateTime;
    private boolean available;
    private IVehicle vehicle;
    private String displayText;

    public TestDriveSlot(String slotId, LocalDateTime dateTime) {
        this.slotId    = slotId;
        this.dateTime  = dateTime;
        this.available = true;
    }

    public TestDriveSlot(String slotId, LocalDateTime dateTime, IVehicle vehicle) {
        this(slotId, dateTime);
        this.vehicle = vehicle;
    }

    public void reserve() { this.available = false; }
    public void release() { this.available = true; this.vehicle = null; }

    public String getSlotId()           { return slotId; }
    public LocalDateTime getDateTime()  { return dateTime; }
    public boolean isAvailable()        { return available; }
    public IVehicle getVehicle()        { return vehicle; }
    public void setVehicle(IVehicle v)  { this.vehicle = v; }

    @Override
    public String toString() {
        return "TestDriveSlot{id='" + slotId + "', dateTime=" + dateTime
                + ", available=" + available + ", displayText='" + displayText + "'}";
    }

    public String getDisplayText() { return displayText; }
    public void setDisplayText(String displayText) { this.displayText = displayText; }
}