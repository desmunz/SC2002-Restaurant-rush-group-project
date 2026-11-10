package restaurantrush.model.staff;

public abstract class Staff {

    private boolean taskCompleted;

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

    public void resetForNewTurn(){
        taskCompleted = false;
    }
}

    

