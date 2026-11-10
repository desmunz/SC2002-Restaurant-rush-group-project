
package restaurantrush.model.staff;

import restaurantrush.model.Order;
import restaurantrush.model.Table;
import restaurantrush.model.customer.Customer;
import restaurantrush.model.enums.CustomerStatus;
import restaurantrush.model.menu.Menu;
import restaurantrush.model.menu.MenuItem;

public class Waiter extends Staff {

    public void seatCustomer(Customer customer, Table table) 
  {
        checkAvailable();

        if (customer == null || table == null || customer.getState() != CustomerStatus.QUEUED || !table.isAvailable()) 
        {
          throw new IllegalStateException("Cannot seat customer.");
        }

        table.seatCustomer(customer);
        completeTask();
    }

    public Order takeOrder(Customer customer, Menu menu, int turn)
  {
		checkAvailable();
    if (customer == null || menu == null || customer.getState() != CustomerStatus.SEATED) 
    {
      throw new IllegalStateException("Cannot take order.");
    }
    MenuItem item = customer.chooseMenuItem(menu);

    int bill = (int) Math.round(item.getPriceCents() * (1.0 - customer.getDiscountRate()));

    Order order = new Order(customer, item, bill, turn);

    customer.placeOrder(order);
    completeTask();

    return order;
  }

    public void serveOrder(Order order, int turn) 
  {
    checkAvailable();

    if (order == null || !order.canServe(turn)) 
    {
      throw new IllegalStateException("Order cannot be served.");
    }

    Customer customer = order.getCustomer();

    if (customer.getState() != CustomerStatus.WAITING_FOR_FOOD)
    {
      throw new IllegalStateException("Customer is not waiting for food.");
    }
    order.markServed(turn);
    customer.receiveOrder(order, turn);
    completeTask();
  }

    private void checkAvailable() 
  {
    if (!isAvailable()) 
    {
      throw new IllegalStateException("Waiter has already worked this turn.");
    }
  }
}
