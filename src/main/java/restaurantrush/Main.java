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

public class Main {
    public static void main(String[] args) {
        List<Table> tables = List.of(new Table(1), new Table(2));

        Waiter waiter = new Waiter();
        Chef chef = new Chef();
        Cashier cashier = new Cashier();

        GameState gameState = new GameState();
        gameState.configureRestaurant(tables, waiter, chef, cashier);
