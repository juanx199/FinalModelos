package com.mycompany.carmotor;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.mycompany.carmotor.model.domain.Advisor;
import com.mycompany.carmotor.model.domain.BankEntity;
import com.mycompany.carmotor.model.domain.MaintenanceRecord;
import com.mycompany.carmotor.model.domain.Photo;
import com.mycompany.carmotor.model.domain.Vehicle;
import com.mycompany.carmotor.model.domain.Branch;
import com.mycompany.carmotor.repository.AdvisorRepository;
import com.mycompany.carmotor.repository.BankEntityRepository;
import com.mycompany.carmotor.repository.VehicleRepository;
import com.mycompany.carmotor.repository.BranchRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final VehicleRepository vehicleRepository;
    private final AdvisorRepository advisorRepository;
    private final BankEntityRepository bankEntityRepository;
    private final BranchRepository branchRepository;

    public DataInitializer(VehicleRepository vehicleRepository,
            AdvisorRepository advisorRepository,
            BankEntityRepository bankEntityRepository,
            BranchRepository branchRepository) {
        this.vehicleRepository = vehicleRepository;
        this.advisorRepository = advisorRepository;
        this.bankEntityRepository = bankEntityRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public void run(String... args) {
        if (vehicleRepository.count() > 0) {
            return;
        }

        // Sedes
        Branch b1 = new Branch("CarMotor Bogotá Norte", "Av. 19 # 122-45, Bogotá", "601-745-1234", "Mon-Fri 8am-6pm, Sat 9am-2pm");
        b1.setTestDriveSlots(new ArrayList<>(List.of("Mon Jun 02 - 9:00am", "Mon Jun 02 - 11:00am", "Tue Jun 03 - 10:00am", "Wed Jun 04 - 2:00pm", "Fri Jun 06 - 9:00am")));

        Branch b2 = new Branch("CarMotor Medellín", "Calle 10 # 43E-31, Medellín", "604-444-5678", "Mon-Fri 8am-6pm, Sat 9am-1pm");
        b2.setTestDriveSlots(new ArrayList<>(List.of("Tue Jun 03 - 9:00am", "Thu Jun 05 - 11:00am", "Sat Jun 07 - 10:00am")));

        Branch b3 = new Branch("CarMotor Cali", "Carrera 5 # 15-20, Cali", "602-330-9876", "Mon-Sat 9am-5pm");
        b3.setTestDriveSlots(new ArrayList<>(List.of("Mon Jun 02 - 10:00am", "Wed Jun 04 - 9:00am", "Fri Jun 06 - 3:00pm")));

        branchRepository.saveAll(List.of(b1, b2, b3));

        // Asesores
        Advisor a1 = new Advisor("Carlos Mendoza", "/img/advisor1.jpg", "cel: 300-111-2233");
        Advisor a2 = new Advisor("Laura Gómez", "/img/advisor2.jpg", "cel: 310-444-5566");
        advisorRepository.saveAll(List.of(a1, a2));

        // Vehicle(brand, model(int año), price, type, passengerCapacity, lastDigitPlate, photos)
        Vehicle v1 = new Vehicle("Toyota", 2020, 45000000, "Sedán", 5, 3, List.of());
        v1.setAdvisor(a1);
        v1.setBranch(b1);
        v1.getPhotos().add(createPhoto("Exterior", "/img/v1-ext.jpg", v1));
        v1.getPhotos().add(createPhoto("Interior", "/img/v1-int.jpg", v1));
        v1.getPhotos().add(createPhoto("Motor", "/img/v1-mot.jpg", v1));
        v1.addMaintenance(new MaintenanceRecord(LocalDate.of(2023, 3, 10), true, false, "Taller Toyota Norte"));
        v1.addMaintenance(new MaintenanceRecord(LocalDate.of(2024, 1, 22), true, true, "Taller Toyota Norte"));

        Vehicle v2 = new Vehicle("Chevrolet", 2021, 68000000, "SUV", 5, 7, List.of());
        v2.setAdvisor(a1);
        v2.setBranch(b1);
        v2.getPhotos().add(createPhoto("Exterior", "/img/v2-ext.jpg", v2));
        v2.getPhotos().add(createPhoto("Interior", "/img/v2-int.jpg", v2));
        v2.addMaintenance(new MaintenanceRecord(LocalDate.of(2023, 8, 5), true, true, "AutoServicios Bogotá"));

        Vehicle v3 = new Vehicle("Mazda", 2022, 95000000, "SUV", 5, 1, List.of());
        v3.setAdvisor(a2);
        v3.setBranch(b2);
        v3.getPhotos().add(createPhoto("Exterior", "/img/v3-ext.jpg", v3));

        Vehicle v4 = new Vehicle("Ford", 2019, 120000000, "Camioneta 4x4", 5, 9, List.of());
        v4.setAdvisor(a2);
        v4.setBranch(b2);
        v4.setInsurable(false);
        v4.getPhotos().add(createPhoto("Exterior", "/img/v4-ext.jpg", v4));
        v4.getPhotos().add(createPhoto("Interior", "/img/v4-int.jpg", v4));
        v4.addMaintenance(new MaintenanceRecord(LocalDate.of(2022, 11, 30), false, true, "Ford Center"));

        Vehicle v5 = new Vehicle("Renault", 2023, 58000000, "SUV", 5, 5, List.of());
        v5.setAdvisor(a1);
        v5.setBranch(b3);
        v5.getPhotos().add(createPhoto("Exterior", "/img/v5-ext.jpg", v5));

        vehicleRepository.saveAll(List.of(v1, v2, v3, v4, v5));

        // Entidades bancarias
        bankEntityRepository.saveAll(List.of(
                new BankEntity("Bancolombia", "/img/bancolombia.png", "601-745-0000"),
                new BankEntity("Davivienda", "/img/davivienda.png", "601-330-0000"),
                new BankEntity("Banco Bogotá", "/img/bancobogota.png", "601-332-0000")
        ));
    }

    private Photo createPhoto(String area, String path, Vehicle vehicle) {
        Photo p = new Photo(area, path);
        p.setVehicle(vehicle);
        return p;
    }
}
