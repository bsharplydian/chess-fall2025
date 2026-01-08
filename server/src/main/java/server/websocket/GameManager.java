package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.concurrent.ConcurrentHashMap;

public class GameManager {
    public Session whitePlayer = null;
    public Session blackPlayer = null;
    public final ConcurrentHashMap<Session, Session> observers = new ConcurrentHashMap<>();

    public void addWhite(Session session){
        whitePlayer = session;
    }
    public void addBlack(Session session) {
        blackPlayer = session;
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

    public void singleMessage(Session session) {

    }
    public void allMessageExcept(Session session) {

    }
    public void allMessage() {

    }
}
