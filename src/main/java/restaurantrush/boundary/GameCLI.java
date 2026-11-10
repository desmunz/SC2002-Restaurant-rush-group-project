//aidan this is for you

import java.io.EOFException;
import java.util.List;
import java.util.Locale;

import restaurantrush.control.GameController;
import restaurantrush.control.GameResult;
import restaurantrush.model.Order;
import restaurantrush.model.Table;
import restaurantrush.model.customer.Customer;

public class GameCLI {
    private final GameController controller;
    private final InputHandler inputHandler;

    public GameCLI(GameController controller) {
        this.controller = controller;
        this.inputHandler = new InputHandler();
    }

    public void run(){
        controller.startGame();

        while (!controller.isGameOver()){
            controller.beginTurn();

            while (!controller.isTurnComplete()){
                displayState();

                String choice;
                try {
                    choice = getManagerChoice();
                } catch (EOFException ex) {
                    displayMessage("\nInput ended. Game stopped before completion.");
                    return;
                }
                boolean accepted = controller.processManagerChoice(choice);

                if (!accepted) {
                    displayMessage(controller.getLastErrorMessage());
                }
            }

            controller.finishTurn();
            displayTurnSummary();
        }

        displayResult(controller.checkResult());
    }

    public void displayState() {
        System.out.printf("%n=== Restaurant Rush | Turn %d of 18 ===%n", controller.getCurrentTurn());

        System.out.printf(Locale.US, "Revenue: $%.2f | Paid customers: %d%n", controller.getRevenueCents() / 100.0, controller.getPaidCustomerCount());

        if (controller.getPaidCustomerCount() == 0) {
            System.out.println("Average paid satisfaction: N/A");
        } else {
            System.out.printf(Locale.US, "Average paid satisfaction: %.1f%n", controller.getAveragePaidSatisfaction());
        }

        System.out.println("\nWaiting queue:");

        List<Customer> waiting = controller.getWaitingCustomers();

        if (waiting.isEmpty()) {
            System.out.println("None");
        }

        for (Customer customer : waiting) {
            System.out.printf("Customer %d | Satisfaction: %d%n", customer.getCustomerId(), customer.getSatisfaction());
        }

        System.out.println("\nTables:");
        for (Table table : controller.getTables()) {
            Customer customer = table.getCustomer();
            if (customer == null) {
                System.out.printf("Table %d | Available%n", table.getTableId());
            } else {
                System.out.printf(
                    "Table %d | Customer %d | %s | Satisfaction: %d%n", table.getTableId(), customer.getCustomerId(), customer.getState(), customer.getSatisfaction());
            }
        }

        System.out.println("\nOrders:");
        List<Order> orders = controller.getOrders();
        if (orders.isEmpty()) {
            System.out.println("None");
        }
        for (Order order : orders) {
            System.out.printf(
                "Order %d | Customer %d | %s | %s | Preparation: %d/%d%n", order.getOrderId(), order.getCustomer().getCustomerId(), order.getItem().getName(), order.getStatus(), order.getPreparationProgress(), order.getItem().getPreparationUnits());
        }
    }

    private int readPositiveId(String prompt) throws EOFException {
        while (true) {
            int id = inputHandler.readInt(prompt);
            if (id > 0) {
                return id;
            }
            displayMessage("Please enter an ID greater than zero.");
        }
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayResult(GameResult result) {
        System.out.println("Game over! Result: " + result);
    }
    
}