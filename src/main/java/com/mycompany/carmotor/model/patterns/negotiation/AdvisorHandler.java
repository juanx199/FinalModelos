package com.mycompany.carmotor.model.patterns.negotiation;

import com.mycompany.carmotor.model.domain.Advisor;

public class AdvisorHandler extends NegotiationHandler {

    private final Advisor advisor;

    public AdvisorHandler(Advisor advisor) {
        this.advisor = advisor;
        this.maxDiscountPct = 5.0;
    }

    @Override
    protected boolean canHandle(NegotiationRequest req) {
        return req.getDiscountPct() <= maxDiscountPct && req.getDiscountPct() >= 0;
    }

    @Override
    protected NegotiationResult handle(NegotiationRequest req) {
        System.out.println("[Chain] AdvisorHandler (" + advisor.getNombre()
                + ") approving discount of "
                + String.format("%.2f", req.getDiscountPct()) + "%...");
        return new NegotiationResult(
                true,
                req.getOfferedPrice(),
                "Advisor: " + advisor.getNombre(),
                "Discount approved by advisor (up to " + maxDiscountPct + "%)."
        );
    }
}