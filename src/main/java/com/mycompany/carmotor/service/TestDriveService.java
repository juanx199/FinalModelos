package com.mycompany.carmotor.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mycompany.carmotor.model.domain.Advisor;
import com.mycompany.carmotor.model.domain.IVehicle;
import com.mycompany.carmotor.model.domain.TestDriveSlot;
import com.mycompany.carmotor.model.patterns.structural.BranchComposite;
import com.mycompany.carmotor.model.patterns.testdrive.AdvisorNotifier;
import com.mycompany.carmotor.model.patterns.testdrive.CancelTestDriveCmd;
import com.mycompany.carmotor.model.patterns.testdrive.ClientConfirmationSender;
import com.mycompany.carmotor.model.patterns.testdrive.ScheduleTestDriveCmd;
import com.mycompany.carmotor.model.patterns.testdrive.SlotAvailabilityUpdater;
import com.mycompany.carmotor.model.patterns.testdrive.TestDriveInvoker;
import com.mycompany.carmotor.model.patterns.testdrive.TestDriveScheduler;

import jakarta.annotation.PostConstruct;

@Service
public class TestDriveService {

    private final BranchService branchService;
    private final VehicleService vehicleService;

    // Un scheduler por sede
    private final Map<String, TestDriveScheduler> schedulers = new HashMap<>();
    // Un invoker global para undo
    private final TestDriveInvoker invoker = new TestDriveInvoker();

    public TestDriveService(BranchService branchService, VehicleService vehicleService) {
        this.branchService = branchService;
        this.vehicleService = vehicleService;
    }

    @PostConstruct
    public void initSchedulers() {
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
        System.out.println("[TestDriveService] Schedulers initialized for "
                + schedulers.size() + " branches.");
    }

    public Map<String, TestDriveScheduler> getSchedulers() {
        return schedulers;
    }

    public TestDriveScheduler getSchedulerForBranch(String branchName) {
        return schedulers.get(branchName);
    }

    public boolean scheduleTestDrive(Long vehicleId, String branchName, String slotId) {
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

        // Usar el Command pattern a través del Invoker
        ScheduleTestDriveCmd cmd = new ScheduleTestDriveCmd(slot, scheduler, vehicle);
        invoker.setCommand(cmd);
        invoker.executeCommand();

        return true;
    }

    public boolean cancelTestDrive(String branchName, String slotId) {
        TestDriveScheduler scheduler = schedulers.get(branchName);
        if (scheduler == null) return false;

        CancelTestDriveCmd cmd = new CancelTestDriveCmd(
                scheduler.findSlot(slotId), scheduler);
        invoker.setCommand(cmd);
        invoker.executeCommand();
        return true;
    }

    public void undoLast() {
        invoker.undoLast();
    }
}