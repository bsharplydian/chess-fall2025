package ui;

import model.requests.LoginRequest;
import model.requests.RegisterRequest;
import model.results.LoginResult;
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
                case "login" -> handleLogin(params);

                default -> throw new HttpResponseException("Invalid Command: " + params[0]);
            };
        } catch (HttpResponseException e) {
            return e.getMessage();
        }
    }
    private String handleRegister(String[] params) throws HttpResponseException {
        if(params.length != 4) {
            throw new HttpResponseException("Usage: register <name> <pass> <email>");
        }
        RegisterRequest request = new RegisterRequest(params[1], params[2], params[3]);
        RegisterResult result = facade.register(request);
        return "Registered " + result.username();
    }

    private String handleLogin(String[] params) throws HttpResponseException {
        if(params.length != 3) {
            throw new HttpResponseException("Usage: login <name> <pass>");
        }
        LoginRequest request = new LoginRequest(params[1], params[2]);
        LoginResult result = facade.login(request);
        return "Logged in: " + result.username();
    }
}
