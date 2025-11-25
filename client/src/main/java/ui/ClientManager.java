package ui;

import serverfacade.HttpResponseException;
import serverfacade.ServerFacade;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class ClientManager {
    Client currentClient;
    PreLoginClient preLoginClient;
    PostLoginClient postLoginClient;
    InGameClient inGameClient;
    public ClientManager(ServerFacade facade) {
        this.preLoginClient = new PreLoginClient(facade);
        this.postLoginClient = new PostLoginClient(facade);
        this.inGameClient = new InGameClient(facade);
        this.currentClient = this.preLoginClient;
    }

    public String eval(String input) {
        String[] params = input.split(" ");
        String ret;
        try {
            ret = currentClient.eval(input);
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
        if(currentClient instanceof PreLoginClient) {
            return "\n" + RESET_TEXT_COLOR + ">> " + SET_TEXT_COLOR_GREEN;
        } else if (currentClient instanceof PostLoginClient) {
            return "\n" + RESET_TEXT_COLOR + "logged in >> " + SET_TEXT_COLOR_GREEN;
        }
        return null;
    }
    private void setCurrentClient(String[] params) throws HttpResponseException {
        currentClient = switch(params[0]) {
            case "register", "login" -> currentClient instanceof PreLoginClient ? postLoginClient      : currentClient;
            case "logout" -> currentClient instanceof PostLoginClient           ? preLoginClient       : currentClient;
            case "join", "observe" -> currentClient instanceof PostLoginClient  ? inGameClient.start(params[1], params[2]) : currentClient;
            default -> currentClient;
        };
    }
}
