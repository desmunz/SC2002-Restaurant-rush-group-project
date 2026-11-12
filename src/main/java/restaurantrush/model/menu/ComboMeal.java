package restaurantrush.model.menu;

public class ComboMeal extends MenuItem{
    private final MenuItem firstComponent;
    private final MenuItem secondComponent;

    public ComboMeal(String name, MenuItem firstComponent, MenuItem secondComponent) {

        super(name, calculateComboPrice(firstComponent, secondComponent),calculatePreparationUnits(firstComponent, secondComponent));

        this.firstComponent = firstComponent;
        this.secondComponent = secondComponent;
    }

    public MenuItem getFirstComponent() {
        return firstComponent;
    }

    public MenuItem getSecondComponent() {
        return secondComponent;
    }

    private static void validateComponents(MenuItem first, MenuItem second) {

        if (first == null || second == null) {
            throw new IllegalArgumentException("Both combo components must be provided.");
        }
    }

    private static int calculateComboPrice(MenuItem first, MenuItem second) {

        validateComponents(first, second);

        long combinedPrice = (long) first.getPriceCents() + second.getPriceCents();

        long discountedPrice = Math.round(combinedPrice * 0.90);

        if (discountedPrice > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Combo price is too large.");
        }

        return (int) discountedPrice;
    }

     private static int calculatePreparationUnits(MenuItem first, MenuItem second) {

        validateComponents(first, second);

        long totalUnits = (long) first.getPreparationUnits() + second.getPreparationUnits();

        if (totalUnits > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Combo preparation time is too large.");
        }

        return (int) totalUnits;
    }
    
}
