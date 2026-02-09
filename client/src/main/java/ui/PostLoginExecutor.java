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

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class PostLoginExecutor implements Executor {
    ArrayList<Integer> serverGameIDs = new ArrayList<>();
    ServerFacade facade;
    BoardPrinter printer = new BoardPrinter();
    PostLoginValidator validator = new PostLoginValidator();
    public PostLoginExecutor(ServerFacade facade) {
        this.facade = facade;
    }

    @Override
    public String eval(String input) throws HttpResponseException {
        String[] params = input.split(" ");
        return switch(validator.parseCommand(params[0])) {
            case HELP -> """
                    help -> display this menu
                    logout -> logout
                    create [name] -> make a new game
                    list -> list the games and ids
                    join [id] [WHITE|BLACK] -> join a game
                    observe -> observe a game""";
            case LOGOUT -> handleLogout(params);
            case CREATE -> handleCreate(params);
            case LIST -> handleList(params);
            case JOIN -> handleJoin(params);
            case OBSERVE -> handleObserve(params);
            default -> throw new SyntaxException("Invalid Command: " + params[0]);
        };
    }

    @Override
    public String getPrompt() {
        return "\n" + RESET_TEXT_COLOR + "logged in >> " + SET_TEXT_COLOR_GREEN;
    }

    private String handleLogout(String[] params) throws HttpResponseException {
        validator.validateLogout(params);

        facade.logout(facade.getAuth());
        return "Logged out";
    }

    private String handleCreate(String[] params) throws HttpResponseException {
        validator.validateCreate(params);
        facade.createGame(new CreateGameRequest(params[1]), facade.getAuth());
        return "Created " + params[1];
    }

    private String handleList(String[] params) throws HttpResponseException {
        validator.validateList(params);

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
        validator.validateJoin(params, serverGameIDs.size());

        ChessGame.TeamColor color = switch(params[2].toUpperCase()) {
            case "WHITE", "W" -> WHITE;
            case "BLACK", "B" -> BLACK;
            default -> null; // not possible after validation
        };

        JoinGameRequest request = new JoinGameRequest(color, serverGameIDs.get(Integer.parseInt(params[1])-1));
        facade.joinGame(request, facade.getAuth());
        // add: game board printing in proper color
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        return "Joined " + params[1] + " as " + params[2] + "\n" + printer.printBoard(board, color);
    }

    private String handleObserve(String[] params) throws SyntaxException {
        validator.validateObserve(params, serverGameIDs.size());

        ChessBoard board = new ChessBoard();
        board.resetBoard();
        return printer.printBoard(board, WHITE);
    }

}
