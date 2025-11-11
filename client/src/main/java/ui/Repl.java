package ui;

import serverfacade.ServerFacade;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl {
    ServerFacade facade = new ServerFacade(8080);
    PreLoginClient client = new PreLoginClient(facade);

    public void run() {
        System.out.println("Welcome to Chess; sign in to get started");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")) {
            printPrompt();
            result = client.eval(scanner.nextLine());
            System.out.print(SET_TEXT_COLOR_WHITE + result);
        }

    }
    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + ">> " + SET_TEXT_COLOR_GREEN);
    }
}
