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

    public String eval(String input) throws HttpResponseException {
        String[] params = input.split(" ");
        return switch (params[0]) {
            case "register" -> handleRegister(params);
            case "login" -> handleLogin(params);
            case "help" -> """
                    help -> display this menu
                    register [name] [password] [email] -> register a new user
                    login [name] [password] -> login an existing user
                    quit -> exit the application""";
            case "quit" -> "quit";
            default -> throw new HttpResponseException("Invalid Command: " + params[0]);
        };
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
