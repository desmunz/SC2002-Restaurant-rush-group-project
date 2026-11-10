package restaurantrush.model.staff;

import restaurantrush.model.Order;
import restaurantrush.model.Table;
import restaurantrush.model.customer.Customer;
import restaurantrush.model.enums.CustomerStatus;
import restaurantrush.model.enums.OrderStatus;

public class Cashier extends Staff {

    public int processPayment(Customer customer, Order order){
      if (!isAvailable()){
        throw new IllegalStateException("This cashier has already acted this turn.");
      }
      if (customer == null || order == null){
        throw new IllegalArgumentException("Customer and order must not be null.");
      }

      if (order.getCustomer() != customer){
        throw new IllegalArgumentException("Order must belong to the selected customer.");
      }

      if (customer.getOrder() != order) {
        throw new IllegalArgumentException("Order must be the customer's assigned order.");
      }

      if (!customer.isReadyToPay()) {
        throw new IllegalStateException("Customer must be ready to pay.");
      }

      if (order.getStatus() != OrderStatus.SERVED) {
        throw new IllegalStateException("Only a served, unpaid order can be charged.");
      }

      Table table = customer.getTable();
      if (table == null || table.getCustomer() != customer) {
        throw new IllegalStateException("Customer must occupy their assigned table.");
      }

      int billCents = order.getBillCents();
      if (billCents < 0) {
        throw new IllegalStateException("Order bill must not be negative.");
      }

      completeTask();
      return billCents;
  }
  
}
