package com.mycompany.carmotor.model.patterns.negotiation;

import com.mycompany.carmotor.model.domain.IVehicle;

public class NegotiationRequest {

    private IVehicle vehicle;
    private double offeredPrice;
    private String client;
    private double discountPct;

    public NegotiationRequest(IVehicle vehicle, double offeredPrice, String client) {
        this.vehicle = vehicle;
        this.offeredPrice = offeredPrice;
        this.client = client;
        if (vehicle != null && vehicle.getPrice() > 0) {
            this.discountPct = ((vehicle.getPrice() - offeredPrice) / vehicle.getPrice()) * 100.0;
        } else {
            this.discountPct = 0;
        }
    }

    public IVehicle getVehicle() { return vehicle; }
    public void setVehicle(IVehicle vehicle) { this.vehicle = vehicle; }
    public double getOfferedPrice() { return offeredPrice; }
    public void setOfferedPrice(double offeredPrice) { this.offeredPrice = offeredPrice; }
    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }
    public double getDiscountPct() { return discountPct; }
    public void setDiscountPct(double discountPct) { this.discountPct = discountPct; }

    @Override
    public String toString() {
        return "NegotiationRequest [Client: " + client
                + " | Vehicle: " + (vehicle != null ? vehicle.getBrand() + " " + vehicle.getModel() : "N/A")
                + " | List Price: $" + (vehicle != null ? vehicle.getPrice() : 0)
                + " | Offered Price: $" + offeredPrice
                + " | Discount: " + String.format("%.2f", discountPct) + "%]";
    }
}