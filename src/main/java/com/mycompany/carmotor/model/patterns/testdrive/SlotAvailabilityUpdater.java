package com.mycompany.carmotor.model.patterns.testdrive;

import com.mycompany.carmotor.model.domain.TestDriveSlot;
import com.mycompany.carmotor.model.patterns.structural.BranchComposite;

public class SlotAvailabilityUpdater implements TestDriveObserver {

    private final BranchComposite branch;

    public SlotAvailabilityUpdater(BranchComposite branch) {
        this.branch = branch;
    }

    @Override
    public void update(TestDriveEvent event) {
        TestDriveSlot slot = event.getSlot();
        switch (event.getType()) {
            case "SCHEDULED" -> System.out.println("  [SlotAvailabilityUpdater] Slot '" + slot.getSlotId()
                        + "' marcado como RESERVADO en sede '" + branch.getName() + "'.");
            case "CANCELLED" -> {
                System.out.println("  [SlotAvailabilityUpdater] Slot '" + slot.getSlotId()
                        + "' liberado en sede '" + branch.getName() + "'.");
                refreshSlots();
            }
            case "RESCHEDULED" -> System.out.println("  [SlotAvailabilityUpdater] Slot actualizado por reprogramación "
                        + "en sede '" + branch.getName() + "'.");
            default -> System.out.println("  [SlotAvailabilityUpdater] Evento no reconocido: " + event.getType());
        }
    }

    private void refreshSlots() {
        System.out.println("  [SlotAvailabilityUpdater] Actualizando slots en '"
                + branch.getName() + "'...");
    }
}