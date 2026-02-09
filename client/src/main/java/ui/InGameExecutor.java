package ui;

import chess.ChessGame;
import serverfacade.HttpResponseException;
import serverfacade.MessageHandler;
import serverfacade.ServerFacade;
import serverfacade.WebSocketFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class InGameExecutor implements Executor, MessageHandler {
    WebSocketFacade ws;
    ServerFacade facade;
    ChessGame game = new ChessGame();

    public InGameExecutor(ServerFacade facade, String url) {
        this.facade = facade;
        this.ws = new WebSocketFacade(url, this);
    }
    @Override
    public String eval(String input) throws HttpResponseException {
        String[] params = input.split(" ");
        return switch(params[0]) {
            case "help" -> """
                    help                         -> display this menu
                    draw                         -> draw the board
                    leave                        -> leave the game
                    move <positionA> <positionB> -> move from position A to position B
                    resign                       -> resign and admit defeat
                    hl <position>                -> highlight legal moves""";
            case "draw"   -> drawHandler();
            case "leave"  -> leaveHandler();
            case "move"   -> moveHandler(params);
            case "resign" -> resignHandler();
            case "hl"     -> hlHandler(params);
            default -> throw new HttpResponseException("Invalid Command: " + params[0]);
        };
    }

    @Override
    public String getPrompt() {
        return "\n" + RESET_TEXT_COLOR + "in game >> " + SET_TEXT_COLOR_GREEN;
    }

    private String drawHandler() {

        return "draw not implemented";
    }
    private String leaveHandler() {
        return "leave not implemented";
    }
    private String moveHandler(String[] params) {
        return "move not implemented";
    }
    private String resignHandler() {
        return "resign not implemented";
    }
    private String hlHandler(String[] params) {
        return "hl not implemented";
    }

    public InGameExecutor start(String gameID, String color) throws SyntaxException, HttpResponseException {
        int id;
        try{
            id = Integer.parseInt(gameID);
        } catch (NumberFormatException e) {
            throw new SyntaxException("invalid game id");
        }
        ChessGame.TeamColor teamColor = switch(color.toUpperCase()) {
            case "WHITE", "W" -> ChessGame.TeamColor.WHITE;
            case "BLACK", "B" -> ChessGame.TeamColor.BLACK;
            case "" -> null;
            default -> throw new SyntaxException("Usage: join [id] [WHITE|BLACK]");
        };
        ws.startServerConnection();
        ws.connectToGame(facade.getAuth(), id);
        return this;
    }

    @Override
    public void handleMessage(NotificationMessage notificationMessage) {
       System.out.print(notificationMessage.getMessage());

    }

    @Override
    public void handleMessage(LoadGameMessage loadGameMessage) {
        System.out.print(loadGameMessage.getGame());
    }

    @Override
    public void handleMessage(ErrorMessage errorMessage) {
        System.out.print(errorMessage.getMessage());
    }
}
