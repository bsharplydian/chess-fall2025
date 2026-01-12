package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class GameManager {
    public Session whitePlayer = null;
    public Session blackPlayer = null;
    public final ConcurrentHashMap<Session, Session> observers = new ConcurrentHashMap<>();

    public void addPlayer(Session session, String username, ChessGame.TeamColor color) throws IOException {
        switch(color) {
            case WHITE -> whitePlayer = session;
            case BLACK -> blackPlayer = session;
        }
        NotificationMessage message = new NotificationMessage(
                ServerMessage.ServerMessageType.NOTIFICATION,
                String.format("%s joined as %s", username, color.toString().toLowerCase())
        );
        allMessageExcept(session, message);
        sendMessage(session, new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME));
    }

    public void addObserver(Session session) {
        observers.put(session, session);
    }
    public void remove(Session session) {
        if(session.equals(whitePlayer)) {
            whitePlayer = null;
        } else if (session.equals(blackPlayer)) {
            blackPlayer = null;
        } else {
            observers.remove(session);
        }
    }

    public void sendMessage(Session session, ServerMessage msg) throws IOException {
        if(Objects.equals(session, null)) {
            return;
        }
        if(session.isOpen()) {
            session.getRemote().sendString(new Gson().toJson(msg));
        }
    }
    public void allMessageExcept(Session session, ServerMessage msg) throws IOException{
        if(!Objects.equals(whitePlayer, session)) {
            sendMessage(whitePlayer, msg);
        }
        if (!Objects.equals(blackPlayer, session)) {
            sendMessage(blackPlayer, msg);
        }
        for(Session observer : observers.values()) {
            if (!Objects.equals(observer, session)) {
                sendMessage(observer, msg);
            }
        }
    }
    public void allMessage(ServerMessage msg) throws IOException{
        sendMessage(whitePlayer, msg);
        sendMessage(blackPlayer, msg);
        for(Session observer : observers.values()) {
            sendMessage(observer, msg);
        }
    }
}
