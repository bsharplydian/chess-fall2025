package ui;

import serverfacade.HttpResponseException;

public interface Client {
    public String eval(String input) throws HttpResponseException;
}
