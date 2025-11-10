package ui;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl {
    public void run() {
        System.out.println("Welcome to C h e s s; sign in to get started");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")) {
            printPrompt();
            result = "quit";
        }

    }
    private void printPrompt() {
        System.out.print(RESET_TEXT_COLOR + ">> " + SET_TEXT_COLOR_GREEN);
    }
}
