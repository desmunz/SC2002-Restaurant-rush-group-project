package main.java.restaurantrush.model;

import restaurantrush.model.customer.Customer;

public class Table {
    private final int tableId;
    private Customer customer;

    public Table(int tableId) {
        this.tableId = tableId;
        this.customer = null;
    }

    
}
