package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import server.websocket.messagedata.NotificationData;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class GameManager {
    public Session whitePlayer = null;
    public Session blackPlayer = null;
    public final ConcurrentHashMap<Session, Session> observers = new ConcurrentHashMap<>();

    public void addPlayer(Session session, NotificationData data) throws IOException {
        switch(data.color()) {
            case WHITE -> whitePlayer = session;
            case BLACK -> blackPlayer = session;
        }
        NotificationMessage message = new NotificationMessage(
                ServerMessage.ServerMessageType.NOTIFICATION,
                String.format("%s joined as %s", data.username(), data.color().toString().toLowerCase())
        );
        allMessageExcept(session, message);
        sendMessage(session, new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, data.game()));
        // need to get actual game from db
    }

    public void addObserver(Session session, NotificationData data) throws IOException {
        observers.put(session, session);
        NotificationMessage message = new NotificationMessage(
                ServerMessage.ServerMessageType.NOTIFICATION,
                String.format("%s is observing", data.username())
        );
        allMessageExcept(session, message);
        sendMessage(session, new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, data.game()));
    }
    public void remove(Session session, NotificationData data) throws IOException{
        if(session.equals(whitePlayer)) {
            whitePlayer = null;
        } else if (session.equals(blackPlayer)) {
            blackPlayer = null;
        } else {
            observers.remove(session);
        }
        NotificationMessage message = new NotificationMessage(
                ServerMessage.ServerMessageType.NOTIFICATION,
                String.format("%s left the game", data.username())
        );
        allMessageExcept(session, message);
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
