package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
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
    int gameID = -1;
    ChessGame game = new ChessGame();
    ChessGame.TeamColor color = null;
    BoardPrinter printer = new BoardPrinter();
    Repl repl;

    public InGameExecutor(ServerFacade facade, String url, Repl repl) {
        this.facade = facade;
        this.ws = new WebSocketFacade(url, this);
        this.repl = repl;
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
        return (printer.printBoard(game.getBoard(), this.color));
    }
    private String leaveHandler() throws HttpResponseException {
        ws.leave(facade.getAuth(), gameID);
        return "leaving game...";
    }
    private String moveHandler(String[] params) throws SyntaxException, HttpResponseException {
        // move e2 e4
        ChessPosition startPos = readPosition(params[1]);
        ChessPosition endPos = readPosition(params[2]);
        ws.makeMove(facade.getAuth(), gameID, new ChessMove(startPos, endPos, null));
        return "making move...";
    }
    private ChessPosition readPosition(String position) throws SyntaxException{
        int col = switch(position.substring(0, 1)) {
            case "a" -> 1;
            case "b" -> 2;
            case "c" -> 3;
            case "d" -> 4;
            case "e" -> 5;
            case "f" -> 6;
            case "g" -> 7;
            case "h" -> 8;
            default -> {
                throw new SyntaxException("column not formatted properly: " + position);
            }
        };
        int row;
        try {
            row = Integer.parseInt(position.substring(1, 2));
        } catch (NumberFormatException e) {
            throw new SyntaxException("row not formatted properly: " + position);
        }
        if(row < 1 || row > 8) {
            throw new SyntaxException("row should be between 1 and 8: " + position);
        }
        return new ChessPosition(row, col);
    }
    private String resignHandler() {
        ws.resign();
        return "resigning...";
    }
    private String hlHandler(String[] params) {
        return "highlight not implemented";
    }

    public InGameExecutor start(String gameID, String color) throws SyntaxException, HttpResponseException {
        int id;
        try{
            id = Integer.parseInt(gameID);
        } catch (NumberFormatException e) {
            throw new SyntaxException("invalid game id");
        }
        this.gameID = id;
        this.color = switch(color.toUpperCase()) {
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
        repl.print("\n"+notificationMessage.getMessage());
//        System.out.print("\n"+notificationMessage.getMessage());
        repl.print(getPrompt());

    }

    @Override
    public void handleMessage(LoadGameMessage loadGameMessage) {
        this.game = loadGameMessage.getGame();
        repl.print("\n"+printer.printBoard(game.getBoard(), color));
        repl.print(getPrompt());
    }

    @Override
    public void handleMessage(ErrorMessage errorMessage) {
        repl.print("\n"+errorMessage.getMessage());
        repl.print(getPrompt());
    }
}
