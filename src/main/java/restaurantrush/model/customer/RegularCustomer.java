package restaurantrush.model.customer;

import java.util.List;
import java.util.Random;
import restaurantrush.model.menu.Menu;
import restaurantrush.model.menu.MenuItem;

public class RegularCustomer extends Customer{
    private final Random random = new Random(12345);
    //add seed 12345 to make repeatable cause why not
    
    public RegularCustomer(int id){
        super(id);
    }

    protected int getSatisfactionLossRate(){
        return 8;
    }
    
    public double getDiscountRate(){
        return 0.0;
    }

    @Override
    public String getCustomerType() {
        return "Regular";
    }

    public MenuItem chooseMenuItem(Menu menu){
        List<MenuItem> available = getAvailableMenuItems(menu);
        return available.get(random.nextInt(available.size()));
    }
}
