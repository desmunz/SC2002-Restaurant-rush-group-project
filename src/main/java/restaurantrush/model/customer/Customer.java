package restaurantrush.model.customer;

import java.util.List;
import restaurantrush.model.Order;
import restaurantrush.model.Table;
import restaurantrush.model.enums.CustomerStatus;
import restaurantrush.model.enums.OrderStatus;
import restaurantrush.model.menu.Menu;
import restaurantrush.model.menu.MenuItem;

public abstract class Customer {
    private final int id;
    private int satisfaction = 100;
    private CustomerStatus state = CustomerStatus.QUEUED;
    private Order currentOrder;
    private Table table;
    private int servedTurn = -1;

    protected abstract int getSatisfactionLossRate();
    public abstract double getDiscountRate();
    public abstract MenuItem chooseMenuItem(Menu menu);

    public Customer(int id) {
        if (id < 1) {
            throw new IllegalArgumentException("Customer ID must be positive.");
        }
        this.id = id;
    }
    
    public int getCustomerId() {
        return id;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public CustomerStatus getState() {
        return state;
    }

    public Order getOrder() {
        return currentOrder;
    }

    public Table getTable() {
        return table;
    }

    public int getWaitingLoss() {
        return getSatisfactionLossRate();
    }

    public void seatAt(Table table) {
        this.table = table;
        state = CustomerStatus.SEATED;
    }

    public void placeOrder(Order order) {
        currentOrder = order;
        state = CustomerStatus.WAITING_FOR_FOOD;
    }

    public void receiveOrder(Order order, int currentTurn) {
        servedTurn = currentTurn;
        state = CustomerStatus.SERVED;
    }

    public void markReadyToPay() {
        state = CustomerStatus.READY_TO_PAY;
    }

    public void markPaid() {
        state = CustomerStatus.PAID;
    }

    public void loseSatisfaction(int amount) {
        satisfaction = Math.max(0, satisfaction - amount);
        if (satisfaction == 0) {
            state = CustomerStatus.ABANDONED;
        }
    }

    public void processWaitingTurn() {
        loseSatisfaction(getWaitingLoss());
    }

    public void clearTable() {
        table = null;
    }
    
    public boolean isWaitingForService() {
        return state == CustomerStatus.SEATED || state == CustomerStatus.WAITING_FOR_FOOD;
    }

    public void markDeparted() {
        state = CustomerStatus.ABANDONED;
    }
}
