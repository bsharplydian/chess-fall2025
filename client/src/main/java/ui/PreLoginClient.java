package ui;

import model.requests.RegisterRequest;
import model.results.RegisterResult;
import serverfacade.HttpResponseException;
import serverfacade.ServerFacade;

public class PreLoginClient implements Client {
    ServerFacade facade;
    public PreLoginClient(ServerFacade facade) {
        this.facade = facade;
    }

    public String eval(String input) {
        String[] params = input.split(" ");
        try {
            return switch (params[0]) {
                case "register" -> handleRegister(params);

                default -> throw new HttpResponseException("Invalid Command: " + params[0]);
            };
        } catch (HttpResponseException e) {
            return "Error: " + e.getMessage();
        }
    }
    private String handleRegister(String[] params) throws HttpResponseException {
        RegisterRequest request = new RegisterRequest(params[1], params[2], params[3]);
        RegisterResult result = facade.register(request);
        return "Registered " + result.username();
    }
}
