package ui;

import model.requests.CreateGameRequest;
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
            case "help" -> """
                    help -> display this menu
                    logout -> logout
                    create [name] -> make a new game
                    list -> list the games and ids
                    join [id] [WHITE|BLACK] -> join a game
                    observe -> observe a game""";
            case "logout" -> handleLogout(params);
            case "create" -> handleCreate(params);
            case "list" -> handleList(params);
            case "join" -> handleJoin(params);
            case "observe" -> handleObserve(params);
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

    private String handleCreate(String[] params) throws HttpResponseException {
        if(params.length != 2) {
            throw new HttpResponseException("Usage: create [name]");
        }
        try{
            Integer.parseInt(params[1]);
        } catch (NumberFormatException e) {
            throw new HttpResponseException("Game names cannot be numerals");
        }

        facade.createGame(new CreateGameRequest(params[1]), facade.getAuth());
        return "Created " + params[1];
    }

    private String handleList(String[] params) {
        return "list games not implemented";
    }

    private String handleJoin(String[] params) {
        return "join game not implemented";
    }

    private String handleObserve(String[] params) {
        return "observe not implemented";
    }

}
