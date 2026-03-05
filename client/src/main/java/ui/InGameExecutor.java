package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import serverfacade.HttpResponseException;
import serverfacade.MessageHandler;
import serverfacade.ServerFacade;
import serverfacade.WebSocketFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.util.ArrayList;
import java.util.Collection;

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
    InGameValidator validator = new InGameValidator();

    public InGameExecutor(ServerFacade facade, String url, Repl repl) {
        this.facade = facade;
        this.ws = new WebSocketFacade(url, this);
        this.repl = repl;
    }
    @Override
    public String eval(String input) throws HttpResponseException {
        String[] params = input.split(" ");
        return switch(validator.parseCommand(params[0])) {
            case HELP      -> """
                    help                         -> display this menu
                    draw                         -> draw the board
                    leave                        -> leave the game
                    move <positionA> <positionB> -> move from position A to position B
                    resign                       -> resign and admit defeat
                    hl <position>                -> highlight legal moves""";
            case DRAW      -> drawHandler();
            case LEAVE     -> leaveHandler();
            case MOVE      -> moveHandler(params);
            case RESIGN    -> resignHandler();
            case HIGHLIGHT -> hlHandler(params);
            default -> throw new HttpResponseException("Invalid Command: " + params[0]);
        };
    }

    @Override
    public String getPrompt() {
        return "\n" + RESET_TEXT_COLOR + "in game >> " + SET_TEXT_COLOR_GREEN;
    }

    private String drawHandler() {
        return (printer.printBoard(game.getBoard(), this.color, null));
    }
    private String leaveHandler() throws HttpResponseException {
        ws.leave(facade.getAuth(), gameID);
        return "leaving game...";
    }
    private String moveHandler(String[] params) throws SyntaxException, HttpResponseException {
        // move e2 e4
        ChessPosition startPos = validator.readPosition(params[1]);
        ChessPosition endPos = validator.readPosition(params[2]);
        ChessPiece.PieceType promotionPiece = params.length == 4 ? validator.readPiece(params[3]) : null;
        ws.makeMove(facade.getAuth(), gameID, new ChessMove(startPos, endPos, promotionPiece));
        return "making move...";
    }

    private String resignHandler() throws HttpResponseException {
        ws.resign(facade.getAuth(), gameID);
        return "resigning...";
    }
    private String hlHandler(String[] params) {
        ChessPosition thisPosition = validator.readPosition(params[1]);
        if(game.getBoard().getPiece(thisPosition) == null) {
            return printer.printBoard(game.getBoard(), this.color, null);
        }
        Collection<ChessMove> moves = game.validMoves(thisPosition);
        Collection<ChessPosition> highlights=new ArrayList<>();
        for(ChessMove move : moves) {
            highlights.add(move.getEndPosition());
        }
        highlights.add(thisPosition);
        return printer.printBoard(game.getBoard(), this.color, highlights);
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
        repl.print("\n"+printer.printBoard(game.getBoard(), color, null));
        repl.print(getPrompt());
    }

    @Override
    public void handleMessage(ErrorMessage errorMessage) {
        repl.print("\n"+errorMessage.getMessage());
        repl.print(getPrompt());
    }
}
