package com.mycompany.carmotor.service;

import com.mycompany.carmotor.model.domain.Vehicle;
import com.mycompany.carmotor.model.patterns.creational.BranchSingleton;
import com.mycompany.carmotor.model.patterns.structural.BranchComposite;
import com.mycompany.carmotor.repository.VehicleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BranchService {

    private final VehicleRepository vehicleRepository;
    private BranchSingleton branchSingleton;

    public BranchService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @PostConstruct
    public void initBranches() {
        branchSingleton = BranchSingleton.getInstance("CarMotor");

        BranchComposite root = branchSingleton.getRootBranch();

        // Sede Bogotá
        BranchComposite bogota = new BranchComposite(
                "CarMotor Bogotá Norte",
                "Av. 19 # 122-45, Bogotá",
                "601-745-1234",
                "Mon-Fri 8am-6pm, Sat 9am-2pm"
        );
        bogota.addTestDriveSlot("Mon Jun 02 - 9:00am");
        bogota.addTestDriveSlot("Mon Jun 02 - 11:00am");
        bogota.addTestDriveSlot("Tue Jun 03 - 10:00am");
        bogota.addTestDriveSlot("Wed Jun 04 - 2:00pm");
        bogota.addTestDriveSlot("Fri Jun 06 - 9:00am");

        // Sede Medellín
        BranchComposite medellin = new BranchComposite(
                "CarMotor Medellín",
                "Calle 10 # 43E-31, Medellín",
                "604-444-5678",
                "Mon-Fri 8am-6pm, Sat 9am-1pm"
        );
        medellin.addTestDriveSlot("Tue Jun 03 - 9:00am");
        medellin.addTestDriveSlot("Thu Jun 05 - 11:00am");
        medellin.addTestDriveSlot("Sat Jun 07 - 10:00am");

        // Sede Cali
        BranchComposite cali = new BranchComposite(
                "CarMotor Cali",
                "Carrera 5 # 15-20, Cali",
                "602-330-9876",
                "Mon-Sat 9am-5pm"
        );
        cali.addTestDriveSlot("Mon Jun 02 - 10:00am");
        cali.addTestDriveSlot("Wed Jun 04 - 9:00am");
        cali.addTestDriveSlot("Fri Jun 06 - 3:00pm");

        root.add(bogota);
        root.add(medellin);
        root.add(cali);

        // Asignar vehículos a sedes usando el repository
        List<Vehicle> vehicles = vehicleRepository.findAll();
        if (vehicles.size() >= 5) {
            bogota.addVehicle(vehicles.get(0));
            bogota.addVehicle(vehicles.get(1));
            medellin.addVehicle(vehicles.get(2));
            medellin.addVehicle(vehicles.get(3));
            cali.addVehicle(vehicles.get(4));
        }

        System.out.println("[BranchService] Branch system initialized with 3 branches.");
    }

    public List<BranchComposite> getAllBranches() {
        return branchSingleton.getRootBranch().getSubBranches();
    }

    public BranchComposite getRootBranch() {
        return branchSingleton.getRootBranch();
    }
}