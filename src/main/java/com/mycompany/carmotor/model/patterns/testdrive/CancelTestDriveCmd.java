package com.mycompany.carmotor.model.patterns.testdrive;

import com.mycompany.carmotor.model.domain.TestDriveSlot;

public class CancelTestDriveCmd implements TestDriveCommand {

    private final TestDriveSlot slot;
    private final TestDriveScheduler scheduler;
    private TestDriveSlot backupSlot;

    public CancelTestDriveCmd(TestDriveSlot slot, TestDriveScheduler scheduler) {
        this.slot = slot;
        this.scheduler = scheduler;
    }

    @Override
    public void execute() {
        System.out.println("[Command] Ejecutando CancelTestDriveCmd...");
        this.backupSlot = new TestDriveSlot(slot.getSlotId(), slot.getDateTime(), slot.getVehicle());
        if (!slot.isAvailable()) this.backupSlot.reserve();
        scheduler.cancelTestDrive(slot.getSlotId(), "Cancelación solicitada por el usuario");
    }

    @Override
    public void undo() {
        System.out.println("[Command] Deshaciendo CancelTestDriveCmd...");
        if (backupSlot != null && backupSlot.getVehicle() != null) {
            scheduler.scheduleTestDrive(slot, backupSlot.getVehicle());
            System.out.println("[Command] Cancelación revertida en slot '" + slot.getSlotId() + "'.");
        } else {
            System.out.println("[Command] No se puede revertir: sin backup.");
        }
    }
}