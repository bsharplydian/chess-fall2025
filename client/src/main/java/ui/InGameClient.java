package ui;

import serverfacade.HttpResponseException;

public class InGameClient implements Client {
    @Override
    public String eval(String input) throws HttpResponseException {
        return "";
    }

    public InGameClient start() {
        return this;
    }
}
