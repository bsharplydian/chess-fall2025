package server.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.exceptions.DataAccessException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import server.websocket.messagedata.NotificationData;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage.ServerMessageType;

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

    public void addToGame(ConnectCommand command, Session session) throws IOException {
        int id = command.getGameID();
        if(games.get(id) == null) {
            games.put(id, new GameManager());
        }

        try {
            ChessGame.TeamColor color = getPlayerColor(command);
            switch (color) {
                case WHITE, BLACK -> games.get(id).addPlayer(session, getNotifData(command));
                case null -> games.get(id).addObserver(session, getNotifData(command));
            }
        } catch (BadGameIDException | BadAuthException e) {
            games.get(id).sendMessage(session, new ErrorMessage(ServerMessageType.ERROR, e.getMessage()));
        }
    }

    public void removeFromGame(LeaveCommand command, Session session) throws IOException {
        int id = command.getGameID();
        try {
            games.get(id).remove(session, getNotifData(command));
            GameData oldGameData = gameDAO.getGame(command.getGameID());
            ChessGame.TeamColor color = getPlayerColor(command);
            GameData newGameData = switch(color) {
                case WHITE -> new GameData(id, null, oldGameData.blackUsername(), oldGameData.gameName(), oldGameData.game());
                case BLACK -> new GameData(id, oldGameData.whiteUsername(), null, oldGameData.gameName(), oldGameData.game());
                case null -> oldGameData;
            };
            gameDAO.updateGame(newGameData);
        } catch (DataAccessException e) {
            throw new IOException(e);
        }
    }

    public void makeMove(MakeMoveCommand command, Session session) throws IOException {
        int id = command.getGameID();
        try{
            getPlayerUsername(command);
        } catch (BadAuthException e) {
            games.get(id).sendMessage(session, new ErrorMessage(ServerMessageType.ERROR, e.getMessage()));
            return;
        }
        try {
            GameData gameData = gameDAO.getGame(command.getGameID());
            if(getPlayerColor(command) == null) {
                throw new InvalidMoveException("you are not a player");
            }
            if(gameData.game().getTeamTurn() != getPlayerColor(command)) {
                throw new InvalidMoveException("it is not your turn");
            }

            gameData.game().makeMove(command.getMove());

            gameDAO.updateGame(gameData);

            games.get(id).allMessage(new LoadGameMessage(ServerMessageType.LOAD_GAME, gameData.game()));
            games.get(id).allMessageExcept(session,
                    new NotificationMessage(
                            ServerMessageType.NOTIFICATION,
                            String.format("%s moved from %s to %s",
                                    getPlayerUsername(command),
                                    command.getMove().getStartPosition(),
                                    command.getMove().getEndPosition()
                            )
                    )
            );
            ChessGame.TeamColor opponentColor = gameData.game().invertColor(getPlayerColor(command));

            if(gameData.game().isInCheckmate(opponentColor)) {
                games.get(id).allMessage(new NotificationMessage(ServerMessageType.NOTIFICATION, "OPPONENT USERNAME is in checkmate"));
            } else if(gameData.game().isInCheck(opponentColor)) {
                games.get(id).allMessage(new NotificationMessage(ServerMessageType.NOTIFICATION, "OPPONENT USERNAME is in check"));
            } else if(gameData.game().isInStalemate(opponentColor)) {
                games.get(id).allMessage(new NotificationMessage(ServerMessageType.NOTIFICATION, "OPPONENT USERNAME is in stalemate"));
            }

        } catch (DataAccessException e) {
            throw new IOException(e);
        } catch (InvalidMoveException e) {
            games.get(id).sendMessage(session, new ErrorMessage(ServerMessageType.ERROR, e.getMessage()));
        }
    }

    private NotificationData getNotifData(UserGameCommand command) throws IOException{
        return new NotificationData(
                getPlayerUsername(command),
                getPlayerColor(command),
                getGame(command.getGameID()),
                null,
                null);
        //make a way to get the opponent username
    }

    private NotificationData getNotifData(MakeMoveCommand command) throws IOException {
        return new NotificationData(
                getPlayerUsername(command),
                getPlayerColor(command),
                getGame(command.getGameID()),
                command.getMove(),
                null);
    }

    private ChessGame.TeamColor getPlayerColor(UserGameCommand command) throws IOException {
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

    private String getPlayerUsername(UserGameCommand command) throws IOException {
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


}
