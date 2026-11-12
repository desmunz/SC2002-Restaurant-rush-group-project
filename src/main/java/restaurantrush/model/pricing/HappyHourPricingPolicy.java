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

}
