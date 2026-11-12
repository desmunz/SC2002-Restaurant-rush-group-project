package restaurantrush.model.staff;

public class Host extends Staff{

    public Host(){
        super();
    }

    public void seatCustomer(Customer customer, Table table){
        if (!isAvailable()){
            throw new IllegalStateException("This host has already acted this turn.");
        }

        if(table == null){
            throw new IllegalArgumentException("Table must not be null.");
        }

        table.seatCustomer(customer);
        completeTask();
    }
    
}
