package ui;

import serverfacade.HttpResponseException;
import serverfacade.WebSocketFacade;

public class InGameClient implements Client {
    WebSocketFacade ws;
    @Override
    public String eval(String input) throws HttpResponseException {
        return "";
    }

    public InGameClient start() {
        return this;
    }
}
