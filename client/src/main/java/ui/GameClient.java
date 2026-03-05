package ui;

import serverfacade.HttpResponseException;
import serverfacade.ServerFacade;

public class GameClient {
    Executor currentExecutor;
    PreLoginExecutor preLoginExecutor;
    PostLoginExecutor postLoginExecutor;
    InGameExecutor inGameExecutor;
    Repl repl;

    public GameClient(ServerFacade facade, int port, Repl repl) {
        this.preLoginExecutor = new PreLoginExecutor(facade);
        this.postLoginExecutor = new PostLoginExecutor(facade);
        this.inGameExecutor = new InGameExecutor(facade, String.format("http://localhost:%d", port), repl);
        this.currentExecutor = this.preLoginExecutor;
        this.repl = repl;
    }

    public boolean eval(String input) {
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
        if(ret.equals("quit")) {
            repl.print("quitting application...");
            return false;
        } else {
            repl.print(ret);
            return true;
        }

    }
    public String getPrompt() {
        return currentExecutor.getPrompt();
    }
    private void setCurrentExecutor(String[] params) throws SyntaxException, HttpResponseException {
        currentExecutor = switch(params[0]) {
            case "register", "login" -> currentExecutor instanceof PreLoginExecutor ? postLoginExecutor : currentExecutor;
            case "logout" -> currentExecutor instanceof PostLoginExecutor ? preLoginExecutor : currentExecutor;
            case "join" -> currentExecutor instanceof PostLoginExecutor ? inGameExecutor.start(params[1], params[2]) : currentExecutor;
            case "observe" -> currentExecutor instanceof PostLoginExecutor ? inGameExecutor.start(params[1], "") : currentExecutor;
            case "leave" -> currentExecutor instanceof InGameExecutor ? postLoginExecutor : currentExecutor;
            default -> currentExecutor;
        };
    }
}
