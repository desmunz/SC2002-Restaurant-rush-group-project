package restaurantrush.model.menu;

public class ComboMeal extends MenuItem{
    private final MenuItem firstComponent;
    private final MenuItem secondComponent;

    public ComboMeal(String name, MenuItem firstComponent, MenuItem secondComponent) {

        super(name, calculateComboPrice(firstComponent, secondComponent),calculatePreparationUnits(firstComponent, secondComponent));

        this.firstComponent = firstComponent;
        this.secondComponent = secondComponent;
    }

    //need to write methods for calculate combo price 
    // need tpo write methods for calculate prep units
    
}
