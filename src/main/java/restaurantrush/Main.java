package restaurantrush;

import java.util.List;

import restaurantrush.boundary.GameCLI;
import restaurantrush.control.GameController;
import restaurantrush.control.GameState;
import restaurantrush.model.Table;
import restaurantrush.model.customer.RegularCustomer;
import restaurantrush.model.customer.VIPCustomer;
import restaurantrush.model.customer.CriticCustomer;
import restaurantrush.model.menu.Menu;
import restaurantrush.model.pricing.StandardPricingPolicy;
import restaurantrush.model.staff.Cashier;
import restaurantrush.model.staff.Chef;
import restaurantrush.model.staff.Waiter;

public class Main {
    public static void main(String[] args) {
        List<Table> tables = List.of(new Table(1), new Table(2));

        Waiter waiter = new Waiter();
        Chef chef = new Chef();
        Cashier cashier = new Cashier();

        GameState gameState = new GameState();
        gameState.configureRestaurant(tables, waiter, chef, cashier);


        gameState.scheduleCustomerArrival(
            1, new RegularCustomer(1)
        );
        
        gameState.scheduleCustomerArrival(
            4, new RegularCustomer(2)
        );

        gameState.scheduleCustomerArrival(
            7, new VIPCustomer(3)
        );

        gameState.scheduleCustomerArrival(
            10, new CriticCustomer(4)
        );

        gameState.scheduleCustomerArrival(
            13, new RegularCustomer(5)
        );

        gameState.scheduleCustomerArrival(
            16, new RegularCustomer(6)
        );


        Menu menu = new Menu();
        StandardPricingPolicy pricingPolicy = new StandardPricingPolicy();

        GameController controller = new GameController(gameState, menu, pricingPolicy);

        GameCLI cli = new GameCLI(controller);
        cli.run();
    }
}
