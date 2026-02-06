package ui;

import serverfacade.HttpResponseException;
import serverfacade.ServerFacade;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class GameClient {
    Executor currentExecutor;
    PreLoginExecutor preLoginExecutor;
    PostLoginExecutor postLoginExecutor;
    InGameExecutor inGameExecutor;
    public GameClient(ServerFacade facade) {
        this.preLoginExecutor = new PreLoginExecutor(facade);
        this.postLoginExecutor = new PostLoginExecutor(facade);
        this.inGameExecutor = new InGameExecutor(facade);
        this.currentExecutor = this.preLoginExecutor;
    }

    public String eval(String input) {
        String[] params = input.split(" ");
        String ret;
        try {
            ret = currentExecutor.eval(input);
            setCurrentExecutor(params);
        } catch (SyntaxException | HttpResponseException e) {
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
    private void setCurrentExecutor(String[] params) throws SyntaxException, HttpResponseException {
        currentExecutor = switch(params[0]) {
            case "register", "login" -> currentExecutor instanceof PreLoginExecutor ? postLoginExecutor : currentExecutor;
            case "logout" -> currentExecutor instanceof PostLoginExecutor ? preLoginExecutor : currentExecutor;
            case "join", "observe" -> currentExecutor instanceof PostLoginExecutor ? inGameExecutor.start(params[1], params[2]) : currentExecutor;
            default -> currentExecutor;
        };
    }
}
