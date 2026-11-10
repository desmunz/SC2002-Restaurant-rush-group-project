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

    public void advancePreparation(int currentTurn){
        validateTurn(currentTurn);
        
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Only pending orders can be prepared.");
        }

        preparationProgress++;
        
        if (preparationProgress >= item.getPreparationUnits()) {
            status = OrderStatus.READY;
            readyTurn = currentTurn;
        }
    }
    
    public boolean canServe(int currentTurn){
        validateTurn(currentTurn);
        return status == OrderStatus.READY && readyTurn < currentTurn;
    }

    public void markServed(int currentTurn){
        if (!canServe(currentTurn)) {
            throw new IllegalStateException("Order is not ready to be served.");
        }
        status = OrderStatus.SERVED;
        servedTurn = currentTurn;
    }

    public void markPaid(int currentTurn) 
    {
        validateTurn(currentTurn);

        if (status != OrderStatus.SERVED) {
            throw new IllegalStateException("Only served orders can be paid.");
        }

        if (currentTurn < servedTurn + 2) {
            throw new IllegalStateException("Payment is allowed no earlier than two turns after serving");
        }

        status = OrderStatus.PAID;
    }

    public void cancel(){
        if (status == OrderStatus.PAID || status == OrderStatus.CANCELLED){
            throw new IllegalStateException("Order cannot be cancelled.");
        }
        status = OrderStatus.CANCELLED;
    }

    private void validateTurn(int currentTurn){
        if (currentTurn < 1) {
            throw new IllegalArgumentException("Turn must be positive.");
        }
    }

}
