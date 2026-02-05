package ui;

import model.requests.LoginRequest;
import model.requests.RegisterRequest;
import model.results.LoginResult;
import model.results.RegisterResult;
import serverfacade.HttpResponseException;
import serverfacade.ServerFacade;

public class PreLoginExecutor implements Executor {
    ServerFacade facade;
    PreLoginValidator validator = new PreLoginValidator();
    public PreLoginExecutor(ServerFacade facade) {
        this.facade = facade;
    }

    public String eval(String input) throws HttpResponseException {
        String[] params = input.split(" ");
        return switch (validator.parseCommand(params[0])){
            case REGISTER -> handleRegister(params);
            case LOGIN -> handleLogin(params);
            case HELP -> """
                    help -> display this menu
                    register [name] [password] [email] -> register a new user
                    login [name] [password] -> login an existing user
                    quit -> exit the application""";
            case QUIT -> "quit";
            default -> throw new SyntaxException("Invalid Command: " + params[0]);
        };
    }
    private String handleRegister(String[] params) throws HttpResponseException {
        validator.validateRegister(params);

        RegisterRequest request = new RegisterRequest(params[1], params[2], params[3]);
        RegisterResult result = facade.register(request);
        return "Registered " + result.username();
    }

    private String handleLogin(String[] params) throws HttpResponseException {
        validator.validateLogin(params);

        LoginRequest request = new LoginRequest(params[1], params[2]);
        LoginResult result = facade.login(request);
        return "Logged in: " + result.username();
    }
}
