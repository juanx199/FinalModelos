package com.mycompany.carmotor.model.patterns.negotiation;

public abstract class NegotiationHandler {

    protected NegotiationHandler nextHandler;
    protected double maxDiscountPct;

    public NegotiationHandler setNext(NegotiationHandler next) {
        this.nextHandler = next;
        return next;
    }

    protected abstract boolean canHandle(NegotiationRequest req);
    protected abstract NegotiationResult handle(NegotiationRequest req);

    public NegotiationResult process(NegotiationRequest req) {
        if (canHandle(req)) {
            return handle(req);
        } else {
            return passToNext(req);
        }
    }

    protected NegotiationResult passToNext(NegotiationRequest req) {
        if (nextHandler != null) {
            System.out.println("[Chain] " + getClass().getSimpleName()
                    + " cannot handle discount of "
                    + String.format("%.2f", req.getDiscountPct())
                    + "%. Passing to " + nextHandler.getClass().getSimpleName() + "...");
            return nextHandler.process(req);
        } else {
            return new NegotiationResult(false, 0, getClass().getSimpleName(),
                    "Request rejected. No authorization level can approve this discount ("
                            + String.format("%.2f", req.getDiscountPct()) + "%).");
        }
    }
}