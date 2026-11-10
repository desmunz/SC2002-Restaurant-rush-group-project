
package restaurantrush.model.staff;

import restaurantrush.model.Order;
import restaurantrush.model.Table;
import restaurantrush.model.customer.Customer;
import restaurantrush.model.enums.CustomerStatus;
import restaurantrush.model.menu.Menu;
import restaurantrush.model.menu.MenuItem;
import restaurantrush.model.pricing.PricingPolicy;

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

    public Order takeOrder(Customer customer, Menu menu, PricingPolicy pricingPolicy, int currentTurn){
      if(!isAvailable()){
        throw new IllegalStateException("This waiter has already acted this turn.");
      }

      if (customer == null || menu == null || pricingPolicy == null) {
        throw new IllegalStateException("Cannot take order. Customer, menu and pricing policy must not be null. ");
      }


      if (currentTurn < 1) {
        throw new IllegalArgumentException("Turn number must be at least 1.");
      }

      if (!customer.canOrder()) {
        throw new IllegalStateException("Customer must be seated and must not already have an order.");
      }


      MenuItem item = customer.chooseMenuItem(menu);

      if (item == null) {
        throw new IllegalStateException("Customer did not select a menu item.");
      }

      int billCents = pricingPolicy.calculateBillCents(customer, item);

      if (billCents < 0) {
        throw new IllegalStateException("Calculated bill must not be negative.");
      }

      Order order = new Order(customer, item, billCents, currentTurn);

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
