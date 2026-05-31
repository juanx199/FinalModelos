package com.mycompany.carmotor.model.patterns.testdrive;

import com.mycompany.carmotor.model.domain.IVehicle;
import com.mycompany.carmotor.model.domain.TestDriveSlot;
import java.util.ArrayList;
import java.util.List;

public class TestDriveScheduler implements TestDriveSubject {

    private final List<TestDriveObserver> observers;
    private final List<TestDriveSlot> slots;
    private TestDriveEvent lastEvent;

    public TestDriveScheduler() {
        this.observers = new ArrayList<>();
        this.slots     = new ArrayList<>();
    }

    @Override
    public void subscribe(TestDriveObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("[Observer] Observador suscrito: " + observer.getClass().getSimpleName());
        }
    }

    @Override
    public void unsubscribe(TestDriveObserver observer) {
        observers.remove(observer);
        System.out.println("[Observer] Observador desuscrito: " + observer.getClass().getSimpleName());
    }

    @Override
    public void notifyObservers() {
        if (lastEvent == null) return;
        System.out.println("[Observer] Notificando a " + observers.size() + " observador(es)...");
        for (TestDriveObserver o : observers) {
            o.update(lastEvent);
        }
    }

    public void scheduleTestDrive(TestDriveSlot slot, IVehicle vehicle) {
        slot.setVehicle(vehicle);
        slot.reserve();
        if (!slots.contains(slot)) slots.add(slot);
        this.lastEvent = new TestDriveEvent("SCHEDULED", slot, vehicle);
        System.out.println("[Scheduler] Test drive agendado: " + slot);
        notifyObservers();
    }

    public void cancelTestDrive(String slotId, String reason) {
        for (TestDriveSlot slot : slots) {
            if (slot.getSlotId().equals(slotId)) {
                IVehicle vehicle = slot.getVehicle();
                slot.release();
                this.lastEvent = new TestDriveEvent("CANCELLED", slot, vehicle);
                System.out.println("[Scheduler] Test drive cancelado: " + slot + " | Motivo: " + reason);
                notifyObservers();
                return;
            }
        }
        System.out.println("[Scheduler] No se encontró el slot con ID: " + slotId);
    }

    public void addSlot(TestDriveSlot slot) {
        if (!slots.contains(slot)) slots.add(slot);
    }

    public TestDriveSlot findSlot(String slotId) {
        return slots.stream()
                .filter(s -> s.getSlotId().equals(slotId))
                .findFirst()
                .orElse(null);
    }

    public List<TestDriveSlot> getSlots()      { return slots; }
    public TestDriveEvent getLastEvent()        { return lastEvent; }
}