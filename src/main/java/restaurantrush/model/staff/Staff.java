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

    protected void completeTask(){
        if (!isAvailable()) {
            throw new IllegalStateException(
                "This staff member has already acted this turn."
            );
        }

        taskCompleted = true;
        }

    public void resetForNewturn(){
        taskCompleted = false;
    }
}

    

