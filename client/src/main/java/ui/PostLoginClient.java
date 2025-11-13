package ui;

import serverfacade.HttpResponseException;
import serverfacade.ServerFacade;

public class PostLoginClient implements Client {

    ServerFacade facade;

    public PostLoginClient(ServerFacade facade) {
        this.facade = facade;
    }

    @Override
    public String eval(String input) throws HttpResponseException {
        String[] params = input.split(" ");
        return switch(params[0]) {
            case "logout" -> handleLogout(params);

            default -> throw new HttpResponseException("Invalid Command: " + params[0]);
        };
    }

    private String handleLogout(String[] params) throws HttpResponseException {
        if(params.length != 1) {
            throw new HttpResponseException("Usage: logout");
        }
        facade.logout(facade.getAuth());
        return "Logged out";
    }
}
