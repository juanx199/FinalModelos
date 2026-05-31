package com.mycompany.carmotor.model.patterns.testdrive;

import com.mycompany.carmotor.model.domain.IVehicle;
import com.mycompany.carmotor.model.domain.TestDriveSlot;

public class ScheduleTestDriveCmd implements TestDriveCommand {

    private final TestDriveSlot slot;
    private final TestDriveScheduler scheduler;
    private final IVehicle vehicle;

    public ScheduleTestDriveCmd(TestDriveSlot slot, TestDriveScheduler scheduler, IVehicle vehicle) {
        this.slot = slot;
        this.scheduler = scheduler;
        this.vehicle = vehicle;
    }

    @Override
    public void execute() {
        System.out.println("[Command] Ejecutando ScheduleTestDriveCmd...");
        scheduler.scheduleTestDrive(slot, vehicle);
    }

    @Override
    public void undo() {
        System.out.println("[Command] Deshaciendo ScheduleTestDriveCmd...");
        scheduler.cancelTestDrive(slot.getSlotId(), "Undo de agendamiento");
    }
}