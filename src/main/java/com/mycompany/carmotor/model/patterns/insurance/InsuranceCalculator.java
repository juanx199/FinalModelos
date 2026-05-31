package com.mycompany.carmotor.model.patterns.insurance;

import com.mycompany.carmotor.model.domain.IVehicle;

public abstract class InsuranceCalculator {

    protected String insurerName;
    protected IVehicle vehicle;

    public InsuranceCalculator(String insurerName, IVehicle vehicle) {
        this.insurerName = insurerName;
        this.vehicle = vehicle;
    }

    // Template Method
    public final InsuranceQuote calculatePolicy() {
        if (!validateVehicle()) {
            throw new IllegalStateException("Invalid vehicle for insurance quote.");
        }
        double baseRate = getBaseRate();
        double baseCost = vehicle.getPrice() * baseRate;
        double riskCost = assessRisk();
        double discount = applyDiscount();
        double totalPremium = Math.max(baseCost + riskCost - discount, 0);
        return buildQuote(baseCost, riskCost, discount, totalPremium);
    }

    public boolean validateVehicle() {
        return vehicle != null && vehicle.getPrice() > 0;
    }

    public InsuranceQuote buildQuote(double baseCost, double riskCost,
        double discount, double totalPremium) {
        return new InsuranceQuote(insurerName, vehicle, baseCost, riskCost, discount, totalPremium);
    }

    protected abstract double assessRisk();
    protected abstract double applyDiscount();
    protected abstract double getBaseRate();
}