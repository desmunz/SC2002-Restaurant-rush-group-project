package restaurantrush.model;

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

    public void seatCustomer(Customer customer)
    {
        if (customer == null) 
        {
            throw new IllegalArgumentException("Customer cannot be null.");
        }

        if (!isAvailable()) 
        {
            throw new IllegalStateException("Table is already occupied.");
        }
        
        this.customer = customer;
    }
    
    public void releaseTable(){
        if (customer == null){
            return;
        }

        customerStatus state = customer.getState();
        if (state != CustomerStatus.PAID && state != CustomerStatus.ABANDONED){
            throw new IllegalStateException("Table can only be released after payment or abandonment");
        }

        customer.clearTable();
        this.customer = null;
    }
}
