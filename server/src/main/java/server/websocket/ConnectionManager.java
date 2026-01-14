package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.exceptions.DataAccessException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.ConnectCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Session, Session> connections = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Integer, GameManager> games = new ConcurrentHashMap<>();
    AuthDAO authDAO;
    GameDAO gameDAO;
    public ConnectionManager(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void remove(Integer gameID, Session session) {
        games.get(gameID).remove(session);
    }
    public void addToGame(ConnectCommand command, Session session) throws IOException {
        int id = command.getGameID();
        if(games.get(id) == null) {
            games.put(id, new GameManager());
        }

        try {
            ChessGame.TeamColor color = getPlayerColor(command);
            switch (color) {
                case WHITE, BLACK -> games.get(id).addPlayer(session, getPlayerUsername(command), color, getGame(id));
                case null -> games.get(id).addObserver(session, getPlayerUsername(command), getGame(id));
            }
        } catch (BadGameIDException | BadAuthException e) {
            if(Objects.equals(session, null)) {
                return;
            }
            if(session.isOpen()) {
                session.getRemote().sendString(
                        new Gson().toJson(
                                new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage())
                        )
                );
            }
        }


    }


    private ChessGame.TeamColor getPlayerColor(ConnectCommand command) throws IOException {
        try {
            GameData game = gameDAO.getGame(command.getGameID());
            if(game == null) {
                throw new BadGameIDException("Error: game doesn't exist");
            }
            String username = getPlayerUsername(command);
            if(Objects.equals(game.whiteUsername(), username)) {
                return ChessGame.TeamColor.WHITE;
            } else if (Objects.equals(game.blackUsername(), username)) {
                return ChessGame.TeamColor.BLACK;
            } else {
                return null;
            }

        } catch (DataAccessException e) {
            throw new IOException(e.getMessage());
        }
    }

    private String getPlayerUsername(ConnectCommand command) throws IOException {
        try {
            AuthData auth = authDAO.getAuth(command.getAuthToken());
            if(auth == null) {
                throw new BadAuthException("Error: unauthorized");
            }
            return auth.username();
        } catch (DataAccessException e) {
            throw new IOException(e.getMessage());
        }
    }

    private ChessGame getGame(int id) throws IOException {
        try {
            return gameDAO.getGame(id).game();
        } catch (DataAccessException e) {
            throw new IOException(e.getMessage());
        }
    }

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
