package com.mycompany.carmotor.service;

import com.mycompany.carmotor.model.domain.Vehicle;
import com.mycompany.carmotor.model.domain.Branch;
import com.mycompany.carmotor.model.patterns.creational.BranchSingleton;
import com.mycompany.carmotor.model.patterns.structural.BranchComposite;
import com.mycompany.carmotor.repository.VehicleRepository;
import com.mycompany.carmotor.repository.BranchRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class BranchService {

    private final VehicleRepository vehicleRepository;
    private final BranchRepository branchRepository;
    private BranchSingleton branchSingleton;

    public BranchService(VehicleRepository vehicleRepository, BranchRepository branchRepository) {
        this.vehicleRepository = vehicleRepository;
        this.branchRepository = branchRepository;
    }

    public void initBranches() {
        branchSingleton = BranchSingleton.getInstance("CarMotor");
        refreshBranchSystem();
    }

    public void refreshBranchSystem() {
        if (branchSingleton == null) {
            branchSingleton = BranchSingleton.getInstance("CarMotor");
        }
        BranchComposite root = branchSingleton.getRootBranch();
        root.getSubBranches().clear();

        List<Branch> branches = branchRepository.findAll();
        for (Branch b : branches) {
            BranchComposite composite = new BranchComposite(
                    b.getName(),
                    b.getAddress(),
                    b.getPhone(),
                    b.getBusinessHours()
            );

            // Add slots
            if (b.getTestDriveSlots() != null) {
                for (String slot : b.getTestDriveSlots()) {
                    composite.addTestDriveSlot(slot);
                }
            }

            // Add vehicles associated with this branch
            if (b.getVehicles() != null) {
                for (Vehicle v : b.getVehicles()) {
                    composite.addVehicle(v);
                }
            }

            root.add(composite);
        }
        System.out.println("[BranchService] Branch system refreshed/initialized with " + branches.size() + " branches from DB.");
    }

    public List<Branch> getAllBranchesFromDb() {
        return branchRepository.findAll();
    }

    public Branch getBranchById(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sede no encontrada con id: " + id));
    }

    public void saveBranch(Branch branch) {
        branchRepository.save(branch);
        refreshBranchSystem();
    }

    public void deleteBranch(Long id) {
        Branch branch = getBranchById(id);
        if (branch.getVehicles() != null) {
            for (Vehicle v : branch.getVehicles()) {
                v.setBranch(null);
                vehicleRepository.save(v);
            }
        }
        branchRepository.deleteById(id);
        refreshBranchSystem();
    }

    public List<BranchComposite> getAllBranches() {
        return branchSingleton.getRootBranch().getSubBranches();
    }

    public BranchComposite getRootBranch() {
        return branchSingleton.getRootBranch();
    }
}