package restaurantrush;

import java.util.List;

import restaurantrush.boundary.GameCLI;
import restaurantrush.control.GameController;
import restaurantrush.control.GameState;
import restaurantrush.model.Table;
import restaurantrush.model.customer.RegularCustomer;
import restaurantrush.model.menu.Menu;
import restaurantrush.model.pricing.StandardPricingPolicy;
import restaurantrush.model.staff.Cashier;
import restaurantrush.model.staff.Chef;
import restaurantrush.model.staff.Waiter;
import restaurantrush.model.customer.Customer;
import restaurantrush.model.customer.VIPCustomer;
import restaurantrush.model.customer.CriticCustomer;

public class Main {
    public static void main(String[] args) {
        List<Table> tables = List.of(new Table(1), new Table(2));

        Waiter waiter = new Waiter();
        Chef chef = new Chef();
        Cashier cashier = new Cashier();

        GameState gameState = new GameState();
        gameState.configureRestaurant(tables, waiter, chef, cashier);

        int[] arrivalTurns = {1, 4, 7, 10, 13, 16};

        Customer[] customers = {
            new RegularCustomer(1),
            new VIPCustomer(2),
            new RegularCustomer(3),
            new CriticCustomer(4),
            new RegularCustomer(5),
            new RegularCustomer(6)
        };

        for (int i = 0; i < arrivalTurns.length; i++) {
            gameState.scheduleCustomerArrival(arrivalTurns[i], customers[i]);
        }

        Menu menu = new Menu();
        StandardPricingPolicy pricingPolicy = new StandardPricingPolicy();

        GameController controller = new GameController(gameState, menu, pricingPolicy);

        GameCLI cli = new GameCLI(controller);
        cli.run();
    }
}
