package com.mycompany.carmotor.model.patterns.structural;

import com.mycompany.carmotor.model.domain.BankEntity;
import com.mycompany.carmotor.model.domain.IInsurer;
import com.mycompany.carmotor.model.domain.IVehicle;
import com.mycompany.carmotor.model.patterns.creational.InsuranceFactory;

public class NegotiationFacade {

    public void simulateFullNegotiation(IVehicle vehicle, BankEntity bank,
        int installments, String insurerName) {
        System.out.println("\n=== FULL NEGOTIATION SIMULATION (FACADE) ===");
        System.out.println("Vehicle: " + vehicle.getBrand() + " - " + vehicle.getType()
                + " (Year: " + vehicle.getModel() + ")");
        System.out.println("Base Price: $" + vehicle.getPrice());

        if (vehicle.getAdvisor() != null) {
            System.out.println("Assigned Advisor: " + vehicle.getAdvisor().getNombre()
                    + " (Contact: " + vehicle.getAdvisor().getDatosContacto() + ")");
        } else {
            System.out.println("Assigned Advisor: None.");
        }

        if (bank != null && installments > 0) {
            FinancialAdapter adapter = new FinancialAdapter(bank);
            double installment = adapter.getSimulatedInstallment(vehicle.getPrice(), installments);
            System.out.println("Financing with: " + bank.getNombre());
            System.out.println("Estimated monthly installment over " + installments
                    + " months: $" + installment);
        } else {
            System.out.println("Financing: Not requested (cash payment).");
        }

        if (insurerName != null && !insurerName.isEmpty()) {
            try {
                IInsurer insurer = InsuranceFactory.getInsurer(insurerName);
                double policyValue = insurer.calculatePolicy(vehicle.getPrice());
                System.out.println("Full coverage insurance with: " + insurer.getEntityName());
                System.out.println("Estimated annual policy value: $" + policyValue);
            } catch (Exception e) {
                System.out.println("Could not quote insurance: " + e.getMessage());
            }
        }
        System.out.println("============================================\n");
    }
}