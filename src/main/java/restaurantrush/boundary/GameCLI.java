//aidan this is for you
public class GameCLI {
    private final GameController controller;
    private final InputHandler inputHandler;

    public GameCLI(GameController controller) {
        this.controller = controller;
        this.inputHandler = new InputHandler();
    }
}