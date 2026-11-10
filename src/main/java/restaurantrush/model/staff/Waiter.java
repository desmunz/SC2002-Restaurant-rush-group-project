
package restaurantrush.model.staff;

import restaurantrush.model.Order;
import restaurantrush.model.Table;
import restaurantrush.model.customer.Customer;
import restaurantrush.model.enums.CustomerStatus;
import restaurantrush.model.menu.Menu;
import restaurantrush.model.menu.MenuItem;
import restaurantrush.model.pricing.PricingPolicy;

public class Waiter extends Staff {
  public Waiter(){
    super();
  }


  public void seatCustomer(Customer customer, Table table){
      if (!isAvailable()) {
            throw new IllegalStateException(
                "This waiter has already acted this turn."
            );
        }

        if (table == null) {
            throw new IllegalArgumentException(
                "Table must not be null."
            );
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

  public void serveOrder(Order order, int turn) {
    if (!isAvailable()){
      throw new IllegalStateException("This waiter has already acted this turn.");
    }

    if (order == null) 
    {
      throw new IllegalStateException("Order cannot be served. Cannot be null.");
    }

    if (turn < 1){
      throw new IllegalArgumentException("Turn number must be at least 1.");
    }

    if(!order.canServe(turn)){
      throw new IllegalArgumentException("Order must have been ready before this turn.");
    }

    Customer customer = order.getCustomer();

    if (customer == null){
      throw new IllegalStateException("Order must belong to a customer, cant be null.");
    }

    if (!customer.canReceiveOrder(order)){
      throw new IllegalStateException("Customer must be seated and waiting for this order.");
    }
    order.markServed(turn);
    completeTask();
  }








  private void checkAvailable(){
    if (!isAvailable()){
      throw new IllegalStateException("Waiter has already worked this turn.");
    }
  }
}
