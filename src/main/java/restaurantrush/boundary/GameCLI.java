//aidan this is for you

import java.io.EOFException;

import restaurantrush.control.GameController;

public class GameCLI {
    private final GameController controller;
    private final InputHandler inputHandler;

    public GameCLI(GameController controller) {
        this.controller = controller;
        this.inputHandler = new InputHandler();
    }

    public void run(){
        controller.startGame();

        while (!controller.isGameOver()){
            controller.beginTurn();

            while (!controller.isTurnComplete()){
                displayState();

                String choice;
                try {
                    choice = getManagerChoice();
                } catch (EOFException ex) {
                    displayMessage("\nInput ended. Game stopped before completion.");
                    return;
                }
                boolean accepted = controller.processManagerChoice(choice);

                if (!accepted) {
                    displayMessage(controller.getLastErrorMessage());
                }
            }

            controller.finishTurn();
            displayTurnSummary();
        }

        displayResult(controller.checkResult());
    }
}