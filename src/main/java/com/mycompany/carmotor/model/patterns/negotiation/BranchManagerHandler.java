package com.mycompany.carmotor.model.patterns.negotiation;

import com.mycompany.carmotor.model.patterns.structural.BranchComposite;

public class BranchManagerHandler extends NegotiationHandler {

    private final BranchComposite branch;

    public BranchManagerHandler(BranchComposite branch) {
        this.branch = branch;
        this.maxDiscountPct = 15.0;
    }

    @Override
    protected boolean canHandle(NegotiationRequest req) {
        return req.getDiscountPct() <= maxDiscountPct && req.getDiscountPct() > 0;
    }

    @Override
    protected NegotiationResult handle(NegotiationRequest req) {
        System.out.println("[Chain] BranchManagerHandler (Branch manager of '"
                + branch.getName() + "') approving discount of "
                + String.format("%.2f", req.getDiscountPct()) + "%...");
        return new NegotiationResult(
                true,
                req.getOfferedPrice(),
                "Branch Manager: " + branch.getName(),
                "Discount approved by branch manager (up to " + maxDiscountPct + "%)."
        );
    }
}