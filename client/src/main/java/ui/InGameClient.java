package ui;

import serverfacade.HttpResponseException;
import serverfacade.ServerFacade;
import serverfacade.WebSocketFacade;

public class InGameClient implements Client {
    WebSocketFacade ws;
    ServerFacade facade;

    public InGameClient(ServerFacade facade) {
        this.facade = facade;
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

    public InGameClient start(String gameID) throws HttpResponseException {
        int id;
        try{
            id = Integer.parseInt(gameID);
        } catch (NumberFormatException e) {
            throw new HttpResponseException(e.getMessage());
        }
        ws.startServerConnection();
        ws.connectToGame(facade.getAuth(), id);
        return this;
    }
}
