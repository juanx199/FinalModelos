package com.mycompany.carmotor.model.patterns.testdrive;

import com.mycompany.carmotor.model.domain.IVehicle;
import com.mycompany.carmotor.model.domain.TestDriveSlot;

public class RescheduleTestDriveCmd implements TestDriveCommand {

    private final TestDriveSlot oldSlot;
    private final TestDriveSlot newSlot;
    private final TestDriveScheduler scheduler;

    public RescheduleTestDriveCmd(TestDriveSlot oldSlot, TestDriveSlot newSlot, TestDriveScheduler scheduler) {
        this.oldSlot = oldSlot;
        this.newSlot = newSlot;
        this.scheduler = scheduler;
    }

    @Override
    public void execute() {
        System.out.println("[Command] Ejecutando RescheduleTestDriveCmd...");
        IVehicle vehicle = oldSlot.getVehicle();
        scheduler.cancelTestDrive(oldSlot.getSlotId(), "Reprogramación a nuevo slot");
        scheduler.scheduleTestDrive(newSlot, vehicle);
        System.out.println("[Command] Reprogramado de '" + oldSlot.getSlotId()
                + "' a '" + newSlot.getSlotId() + "'.");
    }

    @Override
    public void undo() {
        System.out.println("[Command] Deshaciendo RescheduleTestDriveCmd...");
        IVehicle vehicle = newSlot.getVehicle();
        scheduler.cancelTestDrive(newSlot.getSlotId(), "Undo de reprogramación");
        scheduler.scheduleTestDrive(oldSlot, vehicle);
        System.out.println("[Command] Revertido al slot '" + oldSlot.getSlotId() + "'.");
    }
}