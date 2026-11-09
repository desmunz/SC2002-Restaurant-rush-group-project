package main.java.restaurantrush.model.staff;

public class Staff {

    private boolean taskCompleted;

    public Staff(boolean b){
        this.taskCompleted = b;

    }

    protected Staff() {
        this.taskCompleted = false;
    }

    public boolean isAvailable() {
        return !taskCompleted;
    }
}
