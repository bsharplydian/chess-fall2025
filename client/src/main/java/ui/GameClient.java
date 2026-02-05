package ui;

import serverfacade.HttpResponseException;
import serverfacade.ServerFacade;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class GameClient {
    Executor currentExecutor;
    PreLoginExecutor preLoginClient;
    PostLoginExecutor postLoginClient;
    InGameExecutor inGameClient;
    public GameClient(ServerFacade facade) {
        this.preLoginClient = new PreLoginExecutor(facade);
        this.postLoginClient = new PostLoginExecutor(facade);
        this.inGameClient = new InGameExecutor(facade);
        this.currentExecutor = this.preLoginClient;
    }

    public String eval(String input) {
        String[] params = input.split(" ");
        String ret;
        try {
            ret = currentExecutor.eval(input);
            setCurrentClient(params);
        } catch (HttpResponseException e) {
            ret = e.getMessage();
        } catch(Exception e) {
            ret = e.getMessage();
        }
        if(ret == null) {
            ret = "An unknown error occurred";
        }
        return ret;

    }
    public String getPrompt() {
        if(currentExecutor instanceof PreLoginExecutor) {
            return "\n" + RESET_TEXT_COLOR + ">> " + SET_TEXT_COLOR_GREEN;
        } else if (currentExecutor instanceof PostLoginExecutor) {
            return "\n" + RESET_TEXT_COLOR + "logged in >> " + SET_TEXT_COLOR_GREEN;
        }
        return null;
    }
    private void setCurrentClient(String[] params) throws HttpResponseException {
        currentExecutor = switch(params[0]) {
            case "register", "login" -> currentExecutor instanceof PreLoginExecutor ? postLoginClient                          : currentExecutor;
            case "logout" -> currentExecutor instanceof PostLoginExecutor ? preLoginClient                           : currentExecutor;
            case "join", "observe" -> currentExecutor instanceof PostLoginExecutor ? inGameClient.start(params[1], params[2]) : currentExecutor;
            default -> currentExecutor;
        };
    }
}
