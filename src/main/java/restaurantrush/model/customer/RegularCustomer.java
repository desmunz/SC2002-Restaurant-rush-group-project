package restaurantrush.model.customer;

import java.util.List;
import java.util.Random;
import restaurantrush.model.menu.Menu;
import restaurantrush.model.menu.MenuItem;

public class RegularCustomer extends Customer{
    
    public RegularCustomer(int id){
        super(id);
    }

    protected int getSatisfactionLossRate(){
        return 8;
    }
    
}
