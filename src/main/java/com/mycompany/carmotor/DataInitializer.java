package com.mycompany.carmotor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.mycompany.carmotor.model.domain.Advisor;
import com.mycompany.carmotor.model.domain.BankEntity;
import com.mycompany.carmotor.model.domain.MaintenanceRecord;
import com.mycompany.carmotor.model.domain.Vehicle;
import com.mycompany.carmotor.repository.AdvisorRepository;
import com.mycompany.carmotor.repository.BankEntityRepository;
import com.mycompany.carmotor.repository.VehicleRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final VehicleRepository vehicleRepository;
    private final AdvisorRepository advisorRepository;
    private final BankEntityRepository bankEntityRepository;

    public DataInitializer(VehicleRepository vehicleRepository,
            AdvisorRepository advisorRepository,
            BankEntityRepository bankEntityRepository) {
        this.vehicleRepository = vehicleRepository;
        this.advisorRepository = advisorRepository;
        this.bankEntityRepository = bankEntityRepository;
    }

    @Override
    public void run(String... args) {

        // Asesores
        Advisor a1 = new Advisor("Carlos Mendoza", "/img/advisor1.jpg", "cel: 300-111-2233");
        Advisor a2 = new Advisor("Laura Gómez", "/img/advisor2.jpg", "cel: 310-444-5566");
        advisorRepository.saveAll(List.of(a1, a2));

        // Vehicle(brand, model(int año), price, type, passengerCapacity, lastDigitPlate, photos)
        Vehicle v1 = new Vehicle("Toyota", 2020, 45000000, "Sedan", 5, 3,
                List.of("/img/v1-ext.jpg", "/img/v1-int.jpg", "/img/v1-mot.jpg"));
        v1.setAdvisor(a1);
        v1.addMaintenance(new MaintenanceRecord(LocalDate.of(2023, 3, 10), true, false, "Taller Toyota Norte"));
        v1.addMaintenance(new MaintenanceRecord(LocalDate.of(2024, 1, 22), true, true, "Taller Toyota Norte"));

        Vehicle v2 = new Vehicle("Chevrolet", 2021, 68000000, "SUV", 5, 7,
                List.of("/img/v2-ext.jpg", "/img/v2-int.jpg"));
        v2.setAdvisor(a1);
        v2.addMaintenance(new MaintenanceRecord(LocalDate.of(2023, 8, 5), true, true, "AutoServicios Bogotá"));

        Vehicle v3 = new Vehicle("Mazda", 2022, 95000000, "SUV", 5, 1,
                List.of("/img/v3-ext.jpg"));
        v3.setAdvisor(a2);

        Vehicle v4 = new Vehicle("Ford", 2019, 120000000, "Camioneta 4x4", 5, 9,
                List.of("/img/v4-ext.jpg", "/img/v4-int.jpg"));
        v4.setAdvisor(a2);
        v4.addMaintenance(new MaintenanceRecord(LocalDate.of(2022, 11, 30), false, true, "Ford Center"));

        Vehicle v5 = new Vehicle("Renault", 2023, 58000000, "SUV", 5, 5,
                List.of("/img/v5-ext.jpg"));
        v5.setAdvisor(a1);

        vehicleRepository.saveAll(List.of(v1, v2, v3, v4, v5));

        // Entidades bancarias
        bankEntityRepository.saveAll(List.of(
                new BankEntity("Bancolombia", "/img/bancolombia.png", "601-745-0000"),
                new BankEntity("Davivienda", "/img/davivienda.png", "601-330-0000"),
                new BankEntity("Banco Bogotá", "/img/bancobogota.png", "601-332-0000")
        ));
    }
}
