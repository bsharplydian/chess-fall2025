package ui;

import serverfacade.ServerFacade;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl {
    ServerFacade facade = new ServerFacade(8080);
    ClientManager clientManager = new ClientManager(facade);
    public void run() {
        System.out.println("Welcome to Chess; sign in to get started");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")) {
            System.out.print(clientManager.getPrompt());
            result = clientManager.eval(scanner.nextLine());
            System.out.print(SET_TEXT_COLOR_BLUE + result);
        }

    }
}
