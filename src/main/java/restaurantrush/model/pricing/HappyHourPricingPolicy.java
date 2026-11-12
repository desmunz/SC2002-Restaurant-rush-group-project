package restaurantrush.model.pricing;

public class HappyHourPricingPolicy {
    private boolean activatedBefore;
    private int remainingTurns;

    public HappyHourPricingPolicy() {
        this.activatedBefore = false;
        this.remainingTurns = 0;
    }
    public boolean canActivate() {
        return !activatedBefore;
    }

    public void activate() {
        if (!canActivate()) {
            throw new IllegalStateException("Happy Hour can only be activated once per game.");
        }

        activatedBefore = true;
        remainingTurns = 2;
    }

    public double getPolicyDiscountRate() {
        return isActive() ? 0.20 : 0.0;
    }

    public boolean isActive() {
        return remainingTurns > 0;
    }

    public int getRemainingTurns() {
        return remainingTurns;
    }

}
