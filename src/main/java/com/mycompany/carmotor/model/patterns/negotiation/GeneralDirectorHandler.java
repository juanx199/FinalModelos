package com.mycompany.carmotor.model.patterns.negotiation;

public class GeneralDirectorHandler extends NegotiationHandler {

    public GeneralDirectorHandler() {
        this.maxDiscountPct = 30.0;
    }

    @Override
    protected boolean canHandle(NegotiationRequest req) {
        return req.getDiscountPct() <= maxDiscountPct;
    }

    @Override
    protected NegotiationResult handle(NegotiationRequest req) {
        System.out.println("[Chain] GeneralDirectorHandler approving special discount of "
                + String.format("%.2f", req.getDiscountPct()) + "%...");
        return new NegotiationResult(
                true,
                req.getOfferedPrice(),
                "General Director",
                "Special discount approved by General Director (up to " + maxDiscountPct + "%)."
        );
    }

    @Override
    public NegotiationResult process(NegotiationRequest req) {
        if (canHandle(req)) {
            return handle(req);
        } else {
            System.out.println("[Chain] GeneralDirectorHandler: discount of "
                    + String.format("%.2f", req.getDiscountPct())
                    + "% exceeds maximum allowed (" + maxDiscountPct + "%).");
            return new NegotiationResult(
                    false, 0, "General Director",
                    "Request rejected. Requested discount ("
                            + String.format("%.2f", req.getDiscountPct())
                            + "%) exceeds maximum authorized limit (" + maxDiscountPct + "%)."
            );
        }
    }
}