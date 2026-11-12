package restaurantrush.control;

import restaurantrush.model.Order;
import restaurantrush.model.enums.OrderStatus;
import restaurantrush.model.Table;
import restaurantrush.model.customer.Customer;
import restaurantrush.model.menu.Menu;
import restaurantrush.model.pricing.PricingPolicy;
// import restaurantrush.model.pricing.HappyHourPricingPolicy;
import restaurantrush.model.staff.Cashier;
import restaurantrush.model.staff.Chef;
import restaurantrush.model.staff.Waiter;
import restaurantrush.model.staff.Host;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class GameController {
    private static final int FINAL_TURN = 18;
    private static final int MIN_PAID_CUSTOMERS = 4;
    private static final int MIN_REVENUE_CENTS = 4_500;
    private static final double MIN_AVERAGE_SATISFACTION = 60.0;

    private final GameState gameState;
    private final Menu menu;
    private final PricingPolicy pricingPolicy;
   //happy hour add later on

    private boolean gameStarted;
    private boolean turnStarted;
    //happy hour rejection add later on too
    private int managerChoicePhase;
    private String lastErrorMessage = "";

    private final Map<Order, Integer> servedTurnByOrder = new IdentityHashMap<>();
    private final Map<Customer, Integer> readyToPayTurnByCustomer = new IdentityHashMap<>();

    public GameController(
            GameState gameState,
            Menu menu,
            PricingPolicy pricingPolicy) {
        this.gameState = Objects.requireNonNull(gameState, "gameState");
        this.menu = Objects.requireNonNull(menu, "menu");
        this.pricingPolicy = Objects.requireNonNull(pricingPolicy, "pricingPolicy");
    }

    public void startGame() {
        if (gameStarted) {
            throw new IllegalStateException("startGame() has already been called.");
        }
        if (!gameState.isConfigured()) {
            throw new IllegalStateException(
                    "Configure GameState with the restaurant tables and staff before starting.");
        }
        if (gameState.getTables().size() != 2) {
            throw new IllegalStateException("The restaurant must start with exactly two tables.");
        }
        if (gameState.getHost() == null) {
            throw new IllegalStateException("Stage 3 requires a Host.");
        }
        gameStarted = true;
    }
    public void beginTurn() {
        requireStarted();
        if (isGameOver()) {
            throw new IllegalStateException("The game has ended.");
        }
        if (turnStarted) {
            throw new IllegalStateException("The current turn has already started.");
        }

        int currentTurn = gameState.getCurrentTurn();
        if (currentTurn < 1 || currentTurn > FINAL_TURN) {
            throw new IllegalStateException("Turn number must be between 1 and 18.");
        }

        resetActiveStaffForTurn();
        processArrivals(currentTurn);
        managerChoicePhase = 0;
        turnStarted = true;
        lastErrorMessage = "";
    }
    public boolean processManagerChoice(String choice) {
        if (!turnStarted) {
            return reject("Begin the turn before submitting an action.");
        }
        if (managerChoicePhase >= getPhaseCount()) {
            return reject("All staff phases for this turn are complete.");
        }
        if (choice == null || choice.trim().isEmpty()) {
            return reject("Enter a staff action or PHASE:PASS.");
        }

        String[] parts = choice.trim().split("\\s*:\\s*");
        if (parts.length == 2
                && parts[0].equalsIgnoreCase("PHASE")
                && parts[1].equalsIgnoreCase("PASS")) {
            managerChoicePhase++;
            lastErrorMessage = "";
            return true;
        }

        String phase = getCurrentPhase();
        boolean successful;

        if ("HOST".equals(phase)) {
            successful = processHostChoice(parts);
        } else if ("WAITER".equals(phase)) {
            successful = processWaiterChoice(parts);
        } else {
            successful = processChefChoice(parts);
        }

        if (!successful) {
            if (lastErrorMessage.isEmpty()) {
                lastErrorMessage = "That action is not valid for the current phase or game state.";
            }
            return false;
        }

        managerChoicePhase++;
        lastErrorMessage = "";
        return true;
    }

    public void finishTurn() {
        requireStarted();
        if (!turnStarted || managerChoicePhase < getPhaseCount()) {
            throw new IllegalStateException("Complete or pass every active staff phase first.");
        }

        int currentTurn = gameState.getCurrentTurn();
        processEatingAndPayment(currentTurn);
        processAbandonment();
        gameState.incrementTurn();
        turnStarted = false;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public boolean isTurnComplete() {
        return turnStarted && managerChoicePhase >= getPhaseCount();
    }

    public int getCurrentTurn() {
        return gameState.getCurrentTurn();
    }

    public List<Table> getTables() {
        return gameState.getTables();
    }

    public List<Customer> getWaitingCustomers() {
        return gameState.getWaitingCustomers();
    }

    public List<Order> getOrders() {
        return gameState.getOrders();
    }

    public int getRevenueCents() {
        return gameState.getRevenueCents();
    }

    public int getPaidCustomerCount() {
        return gameState.getPaidCustomerCount();
    }

    public double getAveragePaidSatisfaction() {
        return gameState.getAveragePaidSatisfaction();
    }

    public int getServedCustomerCount() {
        return gameState.getServedCustomerCount();
    }

    public int getUnhappyDepartureCount() {
        return gameState.getUnhappyDepartureCount();
    }

    public int getTurnsUsed() {
        return gameState.getTurnsUsed();
    }

    public boolean isGameOver() {
        return gameState.getCurrentTurn() > FINAL_TURN;
    }

    public GameResult checkResult() {
        if (!isGameOver()) {
            return GameResult.IN_PROGRESS;
        }
        return gameState.getPaidCustomerCount() >= MIN_PAID_CUSTOMERS
                && gameState.getRevenueCents() >= MIN_REVENUE_CENTS
                && gameState.getAveragePaidSatisfaction() >= MIN_AVERAGE_SATISFACTION
                ? GameResult.VICTORY : GameResult.DEFEAT;
    }

    private void resetActiveStaffForTurn() {
        gameState.getHost().resetForNewTurn();
        gameState.getWaiter().resetForNewTurn();
        gameState.getChef().resetForNewTurn();
        gameState.getCashier().resetForNewTurn();
    }

    private int getPhaseCount() {
        return 3;
    }

    public String getCurrentPhase() {
        if (managerChoicePhase == 0) {
            return "HOST";
        }
        if (managerChoicePhase == 1) {
            return "WAITER";
        }
        return "CHEF";
    }

    private boolean processHostChoice(String[] parts) {
        if (parts.length != 3
                || !parts[0].equalsIgnoreCase("HOST")
                || !parts[1].equalsIgnoreCase("SEAT")) {
            return reject("Use HOST:SEAT:<customerId> or PHASE:PASS.");
        }

        Customer customer = findActiveCustomer(parsePositiveId(parts[2]));

        if (customer == null) {
            return reject("That customer is not an active member of this restaurant.");
        }

        if (!gameState.isInWaitingQueue(customer)
                || !customer.isWaitingForTable()) {
            return reject("Select a customer who is waiting for a table.");
        }

        Table table = gameState.getFreeTable();

        if (table == null) {
            return reject("There is no free table.");
        }

        if (!gameState.containsTable(table)) {
            return reject("The selected table does not belong to this restaurant.");
        }

        try {
            gameState.getHost().seatCustomer(customer, table);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return reject(ex.getMessage() == null
                    ? "Host could not seat that customer."
                    : ex.getMessage());
        }
        validateSuccessfulSeating(customer, table);

        if (!gameState.removeFromWaitingQueue(customer)) {
            throw new IllegalStateException("Customer was seated but was not removable from the waiting queue.");}

        return true;
    }

    private boolean processWaiterChoice(String[] parts) {
        if (parts.length < 2 || !parts[0].equalsIgnoreCase("WAITER")) {
            return reject("Use WAITER:SEAT:<customerId>, WAITER:TAKE_ORDER:<customerId>, "
                    + "WAITER:SERVE:<orderId>, or PHASE:PASS.");
        }

        String action = parts[1].toUpperCase(Locale.ROOT);
        if ("SEAT".equals(action)) {
            if (parts.length != 3) {
                return reject("Use WAITER:SEAT:<customerId>.");
            }
            Customer customer = findActiveCustomer(parsePositiveId(parts[2]));
            if (customer == null) {
                return reject("That customer is not an active member of this restaurant.");
            }
            if (!gameState.isInWaitingQueue(customer) || !customer.isWaitingForTable()) {
                return reject("Select a customer who is waiting for a table.");
            }
            Table table = gameState.getFreeTable();
            if (table == null) {
                return reject("There is no free table.");
            }
            if (!gameState.containsTable(table)) {
                return reject("The selected table does not belong to this restaurant.");
            }

            try {
                gameState.getWaiter().seatCustomer(customer, table);
            } catch (IllegalArgumentException | IllegalStateException ex) {
                return reject(ex.getMessage() == null ? "Waiter could not seat that customer." : ex.getMessage());
            }
            validateSuccessfulSeating(customer, table);
            if (!gameState.removeFromWaitingQueue(customer)) {
                throw new IllegalStateException("Customer was seated but was not removable from the waiting queue.");
            }
            return true;
        }

        if ("TAKE_ORDER".equals(action)) {
            if (parts.length != 3) {
                return reject("Use WAITER:TAKE_ORDER:<customerId>.");
            }
            Customer customer = findActiveCustomer(parsePositiveId(parts[2]));
            if (customer == null) {
                return reject("That customer is not an active member of this restaurant.");
            }
            if (!customer.canOrder()) {
                return reject("That customer is not seated and eligible to order.");
            }
            Table customerTable = customer.getTable();
            if (customerTable == null || !gameState.containsTable(customerTable)) {
                return reject("That customer's table does not belong to this restaurant.");
            }

            int currentTurn = gameState.getCurrentTurn();
            Order order;
            try {
                order = gameState.getWaiter().takeOrder(
                        customer, menu, pricingPolicy, currentTurn);
            } catch (IllegalArgumentException | IllegalStateException ex) {
                return reject(ex.getMessage() == null ? "Waiter could not take that order." : ex.getMessage());
            }
            if (order == null) {
                throw new IllegalStateException("Waiter.takeOrder() returned null.");
            }
            if (order.getCustomer() != customer) {
                throw new IllegalStateException("Waiter returned an order owned by a different customer.");
            }
            if (gameState.containsOrder(order)) {
                throw new IllegalStateException("Waiter returned an order that was already registered.");
            }
            if (order.getStatus() != OrderStatus.PENDING) {
                throw new IllegalStateException("Waiter.takeOrder() must return a PENDING order.");
            }

            gameState.addOrder(order);
            return true;
        }

        if ("SERVE".equals(action)) {
            if (parts.length != 3) {
                return reject("Use WAITER:SERVE:<orderId>.");
            }
            Order order = findOrder(parsePositiveId(parts[2]));
            if (order == null) {
                return reject("That order does not belong to this restaurant.");
            }
            int currentTurn = gameState.getCurrentTurn();
            Customer customer = order.getCustomer();
            if (!gameState.isActiveCustomer(customer)) {
                return reject("The order's customer is not active in this restaurant.");
            }
            Table customerTable = customer.getTable();
            if (customerTable == null || !gameState.containsTable(customerTable)) {
                return reject("The order's customer is not seated at a restaurant table.");
            }

            if (!order.canServe(currentTurn)) {
                return reject("The order must have been READY since an earlier turn.");
            }
            if (!customer.canReceiveOrder(order)) {
                return reject("The customer is not eligible to receive this order.");
            }

            try {
                gameState.getWaiter().serveOrder(order, currentTurn);
            } catch (IllegalArgumentException | IllegalStateException ex) {
                return reject(ex.getMessage() == null ? "Waiter could not serve that order." : ex.getMessage());
            }
            if (order.getStatus() != OrderStatus.SERVED) {
                throw new IllegalStateException("Waiter.serveOrder() must mark the Order SERVED.");
            }
            customer.receiveOrder(order, currentTurn);
            servedTurnByOrder.put(order, currentTurn);
            return true;
        }

        return reject("Unknown Waiter action: " + action + ".");
    }

    private boolean processChefChoice(String[] parts) {
        if (parts.length != 3
                || !parts[0].equalsIgnoreCase("CHEF")
                || !parts[1].equalsIgnoreCase("PREPARE")) {
            return reject("Use CHEF:PREPARE:<orderId> or PHASE:PASS.");
        }
        Order order = findOrder(parsePositiveId(parts[2]));
        if (order == null) {
            return reject("That order does not belong to this restaurant.");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            return reject("Only a PENDING order can be prepared.");
        }

        try {
            gameState.getChef().prepareOrder(order, gameState.getCurrentTurn());
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return reject(ex.getMessage() == null ? "Chef could not prepare that order." : ex.getMessage());
        }
        return true;
    }

    private void validateSuccessfulSeating(Customer customer, Table table) {
        if (table.isAvailable() || customer.getTable() != table) {
            throw new IllegalStateException(
                    "Staff seating must occupy the table and assign it to the customer.");
        }
    }

    private void processArrivals(int currentTurn) {
        for (Customer customer : gameState.takeScheduledArrivals(currentTurn)) {
            gameState.enqueueArrivedCustomer(customer);
        }
    }

    private void processEatingAndPayment(int currentTurn) {
        for (Map.Entry<Order, Integer> entry : servedTurnByOrder.entrySet()) {
            Order order = entry.getKey();
            int servedTurn = entry.getValue();
            if (currentTurn >= servedTurn + 1
                    && order.getStatus() == OrderStatus.SERVED) {
                Customer customer = order.getCustomer();
                if (!readyToPayTurnByCustomer.containsKey(customer)) {
                    customer.markReadyToPay();
                    readyToPayTurnByCustomer.put(customer, currentTurn);
                }
            }
        }

        Customer oldestEligible = null;
        Order payableOrder = null;
        int earliestReadyTurn = Integer.MAX_VALUE;
        for (Customer customer : gameState.getCustomers()) {
            Integer readyTurn = readyToPayTurnByCustomer.get(customer);
            if (readyTurn == null || currentTurn < readyTurn + 1) {
                continue;
            }
            Order customerOrder = findServedUnpaidOrderFor(customer);
            if (customerOrder == null) {
                continue;
            }
            Integer servedTurn = servedTurnByOrder.get(customerOrder);
            if (servedTurn == null || currentTurn < servedTurn + 2) {
                continue;
            }
            if (readyTurn < earliestReadyTurn) {
                earliestReadyTurn = readyTurn;
                oldestEligible = customer;
                payableOrder = customerOrder;
            }
        }
        if (oldestEligible == null || payableOrder == null) {
            return;
        }

        int expectedBillCents = payableOrder.getBillCents();
        Table table = oldestEligible.getTable();
        if (table == null || !gameState.containsTable(table)) {
            throw new IllegalStateException(
                    "A customer must remain seated at a restaurant table until payment.");
        }
        gameState.validatePaymentRecord(oldestEligible, expectedBillCents);

        int chargedCents = gameState.getCashier().processPayment(oldestEligible, payableOrder);
        if (chargedCents != expectedBillCents) {
            throw new IllegalStateException(
                    "Cashier must charge the order's locked bill in integer cents.");
        }

        payableOrder.markPaid(currentTurn);
        oldestEligible.markPaid();
        gameState.recordPayment(oldestEligible, chargedCents);
        if (table != null) {
            table.releaseTable();
        }
    }

    private void processAbandonment() {
        for (Customer customer : gameState.getCustomers()) {
            if (!customer.isWaitingForService()) {
                continue;
            }

            customer.processWaitingTurn();

            if (customer.getSatisfaction() == 0) {
                abandonCustomer(customer);
            }
        }
    }

    private void abandonCustomer(Customer customer) {
        customer.markDeparted();
        gameState.removeFromWaitingQueue(customer);

        for (Order order : gameState.getOrders()) {
            if (order.getCustomer() == customer
                    && (order.getStatus() == OrderStatus.PENDING
                    || order.getStatus() == OrderStatus.READY)) {
                order.cancel();
            }
        }

        Table table = customer.getTable();
        if (table != null) {
            if (!gameState.containsTable(table)) {
                throw new IllegalStateException("Departing customer's table is not part of this restaurant.");
            }
            table.releaseTable();
        }
    }

    private Customer findActiveCustomer(int customerId) {
        if (customerId < 1) {
            return null;
        }
        for (Customer customer : gameState.getCustomers()) {
            if (customer.getCustomerId() == customerId) {
                return customer;
            }
        }
        return null;
    }

    private Order findOrder(int orderId) {
        if (orderId < 1) {
            return null;
        }
        for (Order order : gameState.getOrders()) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }
        return null;
    }

    private Order findServedUnpaidOrderFor(Customer customer) {
        for (Order order : gameState.getOrders()) {
            if (order.getCustomer() == customer
                    && order.getStatus() == OrderStatus.SERVED
                    && servedTurnByOrder.containsKey(order)) {
                return order;
            }
        }
        return null;
    }

    private int parsePositiveId(String raw) {
        try {
            int id = Integer.parseInt(raw.trim());
            if (id > 0) {
                return id;
            }
        } catch (NumberFormatException ignored) {
            // rejected below with the same safe validation path as other bad choices.
        }
        return -1;
    }
 
    private boolean reject(String message) {
        lastErrorMessage = message;
        return false;
    }

    private void requireStarted() {
        if (!gameStarted) {
            throw new IllegalStateException("Call startGame() before this operation.");
        }
    }
}
