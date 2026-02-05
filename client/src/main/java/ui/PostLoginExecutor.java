package ui;

import chess.ChessBoard;
import chess.ChessGame;
import model.SimpleGameData;
import model.requests.CreateGameRequest;
import model.requests.JoinGameRequest;
import model.results.ListGamesResult;
import serverfacade.HttpResponseException;
import serverfacade.ServerFacade;

import java.util.ArrayList;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class PostLoginExecutor implements Executor {
    ArrayList<Integer> serverGameIDs = new ArrayList<>();
    ServerFacade facade;
    BoardPrinter printer = new BoardPrinter();

    public PostLoginExecutor(ServerFacade facade) {
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
            throw new HttpResponseException("Game names cannot be numerals");
        } catch (NumberFormatException e) {
            // string was not a number
        }

        facade.createGame(new CreateGameRequest(params[1]), facade.getAuth());
        return "Created " + params[1];
    }

    private String handleList(String[] params) throws HttpResponseException {
        if(params.length != 1) {
            throw new HttpResponseException("Usage: list");
        }
        ListGamesResult result = facade.listGames(facade.getAuth());
        StringBuilder gameList = new StringBuilder();
        serverGameIDs.clear();
        for(int i =0; i< result.games().size(); i++) {
            SimpleGameData game = (SimpleGameData) result.games().toArray()[i];
            String whiteUser = game.whiteUsername()!=null ? game.whiteUsername() : "----";
            String blackUser = game.blackUsername()!=null ? game.blackUsername() : "----";
            gameList.append(
                    String.format("%d. %s -> | White: %s | Black: %s |\n", i+1, game.gameName(), whiteUser, blackUser
                    )
            );
            serverGameIDs.add(i, game.gameID());
        }
        return gameList.toString();
    }

    private String handleJoin(String[] params) throws HttpResponseException {
        if(params.length != 3) {
            throw new HttpResponseException("Usage: join [id] [WHITE|BLACK]");
        }
        try{
            Integer.parseInt(params[1]);
        } catch (NumberFormatException e) {
            throw new HttpResponseException("Invalid game ID");
        }
        if(Integer.parseInt(params[1]) > serverGameIDs.size() || Integer.parseInt(params[1]) <= 0) {
            throw new HttpResponseException("Invalid game ID");
        }
        ChessGame.TeamColor color = switch(params[2].toUpperCase()) {
            case "WHITE", "W" -> WHITE;
            case "BLACK", "B" -> BLACK;
            default -> throw new HttpResponseException("Usage: join [id] [WHITE|BLACK]");
        };
        JoinGameRequest request = new JoinGameRequest(color, serverGameIDs.get(Integer.parseInt(params[1])-1));
        facade.joinGame(request, facade.getAuth());
        // add: game board printing in proper color
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        return "Joined " + params[1] + " as " + params[2] + "\n" + printer.printBoard(board, color);
    }

    private String handleObserve(String[] params) throws HttpResponseException {
        if(params.length != 2) {
            throw new HttpResponseException("Usage: observe [id]");
        }
        try{
            Integer.parseInt(params[1]);
        } catch (NumberFormatException e) {
            throw new HttpResponseException("Invalid game ID");
        }
        if(Integer.parseInt(params[1]) > serverGameIDs.size() || Integer.parseInt(params[1]) <= 0) {
            throw new HttpResponseException("Invalid game ID");
        }

        ChessBoard board = new ChessBoard();
        board.resetBoard();
        return printer.printBoard(board, WHITE);
    }

}
