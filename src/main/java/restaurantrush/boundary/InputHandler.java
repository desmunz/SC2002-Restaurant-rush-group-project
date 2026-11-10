
package restaurantrush.boundary;

import java.util.Scanner;
import java.io.EOFException;

public class InputHandler {

    private final Scanner scanner;

    public InputHandler() {
        scanner = new Scanner(System.in);
    }
    public String readLine(String prompt) throws EOFException {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            throw new EOFException("Terminal input ended.");
        }
    }
}
