package server.websocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.ConnectCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Session, Session> connections = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Integer, GameManager> games = new ConcurrentHashMap<>();

    public void addWhite(Integer gameID, Session session) {
//        games.get(gameID).addWhite(session);
    }
    public void addBlack(Integer gameID, Session session) {
//        games.get(gameID).addBlack(session);
    }
    public void addObserver(Integer gameID, Session session) {
        games.get(gameID).addObserver(session);
    }
    public void remove(Integer gameID, Session session) {
        games.get(gameID).remove(session);
    }
    public void addToGame(ConnectCommand command, Session session, ChessGame.TeamColor color, String username) throws IOException{
        int id = command.getGameID();
        if(games.get(id) == null) {
            games.put(id, new GameManager());
        }
        switch(color) {
            case WHITE, BLACK -> games.get(id).addPlayer(session, username, color);
            case null -> games.get(id).addObserver(session);
            // error handling?
        }


    }
    // how should I organize this? having addWhite and addBlack repeated in the Connection- and GameManager classes seems redundant.
    // Maybe logic for deciding where each connection goes should be in this class, leaving the WebSocketHandler to just call into here

    public void broadcast(Session excludeSession, ServerMessage serverMessage) throws IOException {
        String msg = serverMessage.toString();
        for (Session c : connections.values()) {
            if(c.isOpen()) {
                if(!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}
