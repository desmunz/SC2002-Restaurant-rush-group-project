package main.java.restaurantrush.model;

import restaurantrush.model.customer.Customer;

public class Table {
    private final int tableId;
    private Customer customer;

    public Table(int tableId) {
        this.tableId = tableId;
        this.customer = null;
    }

    public int getTableId() {
        return tableId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public boolean isAvailable() {
        return customer == null;
    }

    public boolean isFree() {
        return isAvailable();
    }
}
