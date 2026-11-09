package restaurantrush.model.enums;

//initiate or enumerates all the customer lifecycle states for future uses, add more if needed

public enum CustomerStatus {
    QUEUED,
    SEATED,
    WAITING_FOR_FOOD, 
    SERVED,           
    READY_TO_PAY,
    PAID,
    ABANDONED
}
