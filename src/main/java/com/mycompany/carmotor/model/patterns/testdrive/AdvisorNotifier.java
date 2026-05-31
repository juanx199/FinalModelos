package com.mycompany.carmotor.model.patterns.testdrive;

import com.mycompany.carmotor.model.domain.Advisor;

public class AdvisorNotifier implements TestDriveObserver {

    private final Advisor advisor;

    public AdvisorNotifier(Advisor advisor) {
        this.advisor = advisor;
    }

    @Override
    public void update(TestDriveEvent event) {
        String vehicleInfo = event.getVehicle() != null
                ? event.getVehicle().getBrand() + " " + event.getVehicle().getModel()
                : "N/A";

        switch (event.getType()) {
            case "SCHEDULED" -> sendNotification("Nuevo test drive agendado para vehículo " + vehicleInfo
                        + " en slot " + event.getSlot().getSlotId() + ".");
            case "CANCELLED" -> sendNotification("Test drive cancelado para vehículo " + vehicleInfo
                        + " en slot " + event.getSlot().getSlotId() + ".");
            case "RESCHEDULED" -> sendNotification("Test drive reprogramado para vehículo " + vehicleInfo + ".");
            default -> sendNotification("Evento de test drive: " + event.getType());
        }
    }

    private void sendNotification(String message) {
        System.out.println("  [AdvisorNotifier] 📱 Notificación para " + advisor.getName()
                + " (" + advisor.getContactInfo() + "): " + message);
    }
}