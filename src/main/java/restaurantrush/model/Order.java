package restaurantrush.model;

import restaurantrush.model.customer.Customer;
import restaurantrush.model.enums.OrderStatus;
import restaurantrush.model.menu.MenuItem;

public class Order {
    private static int nextOrderId = 1;
    private final int orderId;
    private final Customer customer;
    private final MenuItem item;
    private final int billCents;
    private final int orderTurn;

    private OrderStatus status;
    private int preparationProgress;
    private int readyTurn;
    private int servedTurn;

    public Order(Customer customer, MenuItem item, int billCents, int currentTurn) {

        if (customer == null || item == null) {
            throw new IllegalArgumentException("Customer and menu item must not be null.");
        }
        if (billCents < 0) {
            throw new IllegalArgumentException("Bill must not be negative.");
        }
        if (currentTurn < 1) {
            throw new IllegalArgumentException("Turn number must be at least 1.");
        }

        this.orderId = nextOrderId++;
        this.customer = customer;
        this.item = item;
        this.billCents = billCents;
        this.orderTurn = currentTurn;

        this.status = OrderStatus.PENDING;
        this.preparationProgress = 0;
        this.readyTurn = -1;
        this.servedTurn = -1;
    }

    public int getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public MenuItem getItem() {
        return item;
    }

    public int getBillCents() {
        return billCents;
    }

    public int getOrderTurn() {
        return orderTurn;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public int getPreparationProgress() {
        return preparationProgress;
    }

    public int getReadyTurn() {
        return readyTurn;
    }

    public int getServedTurn() {
        return servedTurn;
    }

    public void advancePreparation(int currentTurn) {
        
    }

    public boolean canServe(int currentTurn) {

    }

    public void markServed(int currentTurn) {

    }

    public void markPaid(int currentTurn) {

    }

    public void cancel() {

    }

    private void validateTurn(int currentTurn) {
        
    }

}
