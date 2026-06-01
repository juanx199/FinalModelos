package com.mycompany.carmotor.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mycompany.carmotor.model.domain.Advisor;
import com.mycompany.carmotor.model.domain.IVehicle;
import com.mycompany.carmotor.model.domain.TestDriveSlot;
import com.mycompany.carmotor.model.domain.TestDriveBooking;
import com.mycompany.carmotor.model.patterns.structural.BranchComposite;
import com.mycompany.carmotor.model.patterns.testdrive.AdvisorNotifier;
import com.mycompany.carmotor.model.patterns.testdrive.CancelTestDriveCmd;
import com.mycompany.carmotor.model.patterns.testdrive.ClientConfirmationSender;
import com.mycompany.carmotor.model.patterns.testdrive.RescheduleTestDriveCmd;
import com.mycompany.carmotor.model.patterns.testdrive.ScheduleTestDriveCmd;
import com.mycompany.carmotor.model.patterns.testdrive.SlotAvailabilityUpdater;
import com.mycompany.carmotor.model.patterns.testdrive.TestDriveInvoker;
import com.mycompany.carmotor.model.patterns.testdrive.TestDriveScheduler;
import com.mycompany.carmotor.repository.TestDriveBookingRepository;

import jakarta.annotation.PostConstruct;

@Service
public class TestDriveService {

    private final BranchService branchService;
    private final VehicleService vehicleService;
    private final TestDriveBookingRepository testDriveBookingRepository;

    // Un scheduler por sede
    private final Map<String, TestDriveScheduler> schedulers = new HashMap<>();
    // Un invoker global para undo
    private final TestDriveInvoker invoker = new TestDriveInvoker();

    public TestDriveService(BranchService branchService, VehicleService vehicleService, TestDriveBookingRepository testDriveBookingRepository) {
        this.branchService = branchService;
        this.vehicleService = vehicleService;
        this.testDriveBookingRepository = testDriveBookingRepository;
    }

    @PostConstruct
    public void initSchedulers() {
        refreshSchedulers();
    }

    public void refreshSchedulers() {
        schedulers.clear();
        List<BranchComposite> branches = branchService.getAllBranches();

        for (BranchComposite branch : branches) {
            TestDriveScheduler scheduler = new TestDriveScheduler();

            // Crear slots reales desde los strings de la sede
            List<String> slotStrings = branch.getTestDriveSlots();
            for (int i = 0; i < slotStrings.size(); i++) {
                String slotId = branch.getName().replace(" ", "_") + "_slot_" + i;
                TestDriveSlot slot = new TestDriveSlot(slotId, LocalDateTime.now().plusDays(i + 1));
                slot.setDisplayText(slotStrings.get(i));
                scheduler.addSlot(slot);
            }

            // Registrar observadores
            scheduler.subscribe(new SlotAvailabilityUpdater(branch));
            scheduler.subscribe(new ClientConfirmationSender("client@carmotor.com"));

            schedulers.put(branch.getName(), scheduler);
        }

        // Cargar y aplicar reservas existentes desde la base de datos
        try {
            List<TestDriveBooking> bookings = testDriveBookingRepository.findAll();
            for (TestDriveBooking booking : bookings) {
                TestDriveScheduler scheduler = schedulers.get(booking.getBranchName());
                if (scheduler != null) {
                    TestDriveSlot slot = scheduler.findSlot(booking.getSlotId());
                    if (slot != null) {
                        try {
                            IVehicle vehicle = vehicleService.getVehicleById(booking.getVehicleId());
                            slot.setVehicle(vehicle);
                            slot.reserve();
                        } catch (Exception ex) {
                            System.err.println("[TestDriveService] No se pudo restaurar la reserva " + booking.getId() + ": " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("[TestDriveService] Error al cargar reservas desde la base de datos: " + ex.getMessage());
        }

        System.out.println("[TestDriveService] Schedulers initialized for "
                + schedulers.size() + " branches.");
    }

    public Map<String, TestDriveScheduler> getSchedulers() {
        return schedulers;
    }

    public TestDriveScheduler getSchedulerForBranch(String branchName) {
        return schedulers.get(branchName);
    }

    public boolean scheduleTestDrive(Long vehicleId, String branchName, String slotId, String clientName, String clientEmail, String clientPhone) {
        if (isBlank(branchName) || isBlank(slotId) || isBlank(clientName)
                || isBlank(clientEmail) || isBlank(clientPhone)) {
            return false;
        }

        branchName = branchName.trim();
        slotId = slotId.trim();

        if (testDriveBookingRepository.existsByBranchNameAndSlotId(branchName, slotId)) {
            return false;
        }

        TestDriveScheduler scheduler = schedulers.get(branchName);
        if (scheduler == null) return false;

        TestDriveSlot slot = scheduler.findSlot(slotId);
        if (slot == null || !slot.isAvailable()) return false;

        IVehicle vehicle = vehicleService.getVehicleById(vehicleId);

        // Registrar notificador del asesor si tiene uno
        if (vehicle.getAdvisor() != null) {
            Advisor advisor = (Advisor) vehicle.getAdvisor();
            scheduler.subscribe(new AdvisorNotifier(advisor));
        }

        // Persistir la reserva en base de datos
        try {
            TestDriveBooking booking = new TestDriveBooking(
                    vehicleId,
                    branchName,
                    slotId,
                    clientName.trim(),
                    clientEmail.trim(),
                    clientPhone.trim());
            testDriveBookingRepository.save(booking);
        } catch (Exception ex) {
            System.err.println("[TestDriveService] Error al persistir la reserva: " + ex.getMessage());
            return false;
        }

        // Usar el Command pattern a través del Invoker
        ScheduleTestDriveCmd cmd = new ScheduleTestDriveCmd(slot, scheduler, vehicle);
        invoker.setCommand(cmd);
        invoker.executeCommand();

        return true;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public boolean cancelTestDrive(String branchName, String slotId) {
        TestDriveScheduler scheduler = schedulers.get(branchName);
        if (scheduler == null) return false;

        TestDriveSlot slot = scheduler.findSlot(slotId);
        if (slot == null || slot.isAvailable()) return false;

        CancelTestDriveCmd cmd = new CancelTestDriveCmd(slot, scheduler);
        invoker.setCommand(cmd);
        invoker.executeCommand();
        return true;
    }

    public List<TestDriveBooking> getBookingsForVehicle(Long vehicleId) {
        return testDriveBookingRepository.findByVehicleIdOrderByBookingTimeDesc(vehicleId);
    }

    public boolean cancelBooking(Long vehicleId, Long bookingId) {
        TestDriveBooking booking = testDriveBookingRepository.findById(bookingId).orElse(null);
        if (booking == null || !booking.getVehicleId().equals(vehicleId)) {
            return false;
        }
        boolean cancelled = cancelTestDrive(booking.getBranchName(), booking.getSlotId());
        if (!cancelled) {
            return false;
        }
        testDriveBookingRepository.delete(booking);
        return true;
    }

    public boolean rescheduleBooking(Long vehicleId, Long bookingId, String newBranchName, String newSlotId) {
        if (isBlank(newBranchName) || isBlank(newSlotId)) {
            return false;
        }

        TestDriveBooking booking = testDriveBookingRepository.findById(bookingId).orElse(null);
        if (booking == null || !booking.getVehicleId().equals(vehicleId)) {
            return false;
        }

        newBranchName = newBranchName.trim();
        newSlotId = newSlotId.trim();
        if (booking.getBranchName().equals(newBranchName) && booking.getSlotId().equals(newSlotId)) {
            return false;
        }
        if (testDriveBookingRepository.existsByBranchNameAndSlotIdAndIdNot(newBranchName, newSlotId, bookingId)) {
            return false;
        }

        TestDriveScheduler oldScheduler = schedulers.get(booking.getBranchName());
        TestDriveScheduler newScheduler = schedulers.get(newBranchName);
        if (oldScheduler == null || newScheduler == null) {
            return false;
        }

        TestDriveSlot oldSlot = oldScheduler.findSlot(booking.getSlotId());
        TestDriveSlot newSlot = newScheduler.findSlot(newSlotId);
        if (oldSlot == null || oldSlot.isAvailable() || newSlot == null || !newSlot.isAvailable()) {
            return false;
        }

        IVehicle vehicle = vehicleService.getVehicleById(vehicleId);

        if (oldScheduler == newScheduler) {
            RescheduleTestDriveCmd cmd = new RescheduleTestDriveCmd(oldSlot, newSlot, oldScheduler);
            invoker.setCommand(cmd);
            invoker.executeCommand();
        } else {
            CancelTestDriveCmd cancelCmd = new CancelTestDriveCmd(oldSlot, oldScheduler);
            invoker.setCommand(cancelCmd);
            invoker.executeCommand();

            ScheduleTestDriveCmd scheduleCmd = new ScheduleTestDriveCmd(newSlot, newScheduler, vehicle);
            invoker.setCommand(scheduleCmd);
            invoker.executeCommand();
        }

        booking.setBranchName(newBranchName);
        booking.setSlotId(newSlotId);
        booking.setBookingTime(LocalDateTime.now());
        testDriveBookingRepository.save(booking);
        return true;
    }

    public void undoLast() {
        invoker.undoLast();
    }
}
