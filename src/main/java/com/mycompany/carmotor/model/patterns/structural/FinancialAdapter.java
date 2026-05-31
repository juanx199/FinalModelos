package com.mycompany.carmotor.model.patterns.structural;

import com.mycompany.carmotor.model.domain.BankEntity;

public class FinancialAdapter implements IFinancialAdapter {

    private BankEntity bank;

    public FinancialAdapter(BankEntity bank) {
        this.bank = bank;
    }

    @Override
    public double getSimulatedInstallment(double vehicleValue, int installments) {
        if (installments <= 0) return 0;

        double monthlyRate = 0.015;
        if (bank != null) {
            String name = bank.getNombre().toLowerCase();
            if (name.contains("bancolombia")) monthlyRate = 0.012;
            else if (name.contains("davivienda")) monthlyRate = 0.014;
        }

        double installment = (vehicleValue * monthlyRate) /
                (1 - Math.pow(1 + monthlyRate, -installments));
        return Math.round(installment * 100.0) / 100.0;
    }

    public BankEntity getBank() { return bank; }
    public void setBank(BankEntity bank) { this.bank = bank; }
}