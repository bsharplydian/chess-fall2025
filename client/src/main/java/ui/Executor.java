package ui;

import serverfacade.HttpResponseException;

public interface Executor {
    public String eval(String input) throws HttpResponseException;
}
