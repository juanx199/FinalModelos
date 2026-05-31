package com.mycompany.carmotor.model.patterns.negotiation;

public class NegotiationResult {

    private boolean approved;
    private double finalPrice;
    private String approvedBy;
    private String message;

    public NegotiationResult(boolean approved, double finalPrice, String approvedBy, String message) {
        this.approved = approved;
        this.finalPrice = finalPrice;
        this.approvedBy = approvedBy;
        this.message = message;
    }

    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public double getFinalPrice() { return finalPrice; }
    public void setFinalPrice(double finalPrice) { this.finalPrice = finalPrice; }
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    @Override
    public String toString() {
        if (approved) {
            return "NegotiationResult [✓ APPROVED | Final Price: $" + finalPrice
                    + " | Approved by: " + approvedBy + " | " + message + "]";
        } else {
            return "NegotiationResult [✗ REJECTED | " + message
                    + " | Last evaluator: " + approvedBy + "]";
        }
    }
}