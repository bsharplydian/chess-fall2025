package ui;

import serverfacade.ServerFacade;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl {
    ServerFacade facade = new ServerFacade(8080);
    GameClient gameClient = new GameClient(facade, 8080, this);
    public void run() {
        System.out.println("Welcome to Chess; sign in to get started");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")) {
            System.out.print(gameClient.getPrompt());
            result = gameClient.eval(scanner.nextLine());
        }

    }
    public void print(String text) {
        System.out.print(SET_TEXT_COLOR_BLUE + text);
        // your presenter should use this instead of the return statement to print to the console
    }
}
