package com.mycompany.carmotor.model.patterns.insurance;

import com.mycompany.carmotor.model.domain.IVehicle;

public class InsuranceQuote {

    private final String insurerName;
    private final IVehicle vehicle;
    private final double baseCost;
    private final double riskCost;
    private final double discount;
    private final double totalPremium;

    public InsuranceQuote(String insurerName, IVehicle vehicle, double baseCost,
        double riskCost, double discount, double totalPremium) {
        this.insurerName = insurerName;
        this.vehicle = vehicle;
        this.baseCost = baseCost;
        this.riskCost = riskCost;
        this.discount = discount;
        this.totalPremium = totalPremium;
    }

    public String getInsurerName() { return insurerName; }
    public IVehicle getVehicle() { return vehicle; }
    public double getBaseCost() { return baseCost; }
    public double getRiskCost() { return riskCost; }
    public double getDiscount() { return discount; }
    public double getTotalPremium() { return totalPremium; }

    @Override
    public String toString() {
        return "Quote [" + insurerName + "] for " + vehicle.getBrand()
                + " - Total Premium: $" + totalPremium
                + " (Base: $" + baseCost + ", Risk: $" + riskCost
                + ", Discount: $" + discount + ")";
    }
}