package com.mycompany.carmotor.model.patterns.testdrive;

public class ClientConfirmationSender implements TestDriveObserver {

    private final String clientEmail;

    public ClientConfirmationSender(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    @Override
    public void update(TestDriveEvent event) {
        String vehicleInfo = event.getVehicle() != null
                ? event.getVehicle().getBrand() + " " + event.getVehicle().getModel()
                : "N/A";

        switch (event.getType()) {
            case "SCHEDULED" -> sendEmail("Confirmación: Su test drive del vehículo " + vehicleInfo
                        + " ha sido agendado para " + event.getSlot().getDateTime() + ".");
            case "CANCELLED" -> sendEmail("Aviso: Su test drive del vehículo " + vehicleInfo
                        + " ha sido cancelado.");
            case "RESCHEDULED" -> sendEmail("Actualización: Su test drive del vehículo " + vehicleInfo
                        + " ha sido reprogramado para " + event.getSlot().getDateTime() + ".");
            default -> sendEmail("Notificación de test drive: " + event.getType());
        }
    }

    private void sendEmail(String message) {
        System.out.println("  [ClientConfirmationSender] ✉ Email enviado a " + clientEmail + ": " + message);
    }
}