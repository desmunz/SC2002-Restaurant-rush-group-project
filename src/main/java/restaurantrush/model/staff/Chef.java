package restaurantrush.model.staff;

import restaurantrush.model.Order;
import restaurantrush.model.enums.OrderStatus;

public class Chef extends Staff {
  
  public void prepareOrder(Order order, int turn) 
  {
    if (!isAvailable()) 
    {
      throw new IllegalStateException("Chef has already worked this turn.");
    }
		
		if (order == null || order.getStatus() != OrderStatus.PENDING) 
		{
			throw new IllegalStateException("Order cannot be prepared.");
		}
		
		order.advancePreparation(turn);
		completeTask();
	}
}
