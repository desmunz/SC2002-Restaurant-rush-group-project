package restaurantrush.model.staff;

import restaurantrush.model.Order;
import restaurantrush.model.customer.Customer;
import restaurantrush.model.enums.CustomerStatus;

public class Cashier extends Staff {

    public int processPayment(Customer customer, int turn) 
  {
    if (!isAvailable() || customer == null || customer.getState() != CustomerStatus.READY_TO_PAY || customer.getReadyToPayTurn() >= turn)
    {
      throw new IllegalStateException("Customer cannot pay yet.");
    }
    
    Order order = customer.getOrder();
    
    if (order == null)
    {
      throw new IllegalStateException("Customer has no order.");
    }

    order.markPaid(turn);
    customer.markPaid();
    completeTask();

    return order.getBillCents();
    
    }
}
