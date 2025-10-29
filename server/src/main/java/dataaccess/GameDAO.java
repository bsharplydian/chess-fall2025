package dataaccess;

import chess.ChessGame;
import dataaccess.exceptions.DataAccessException;
import model.GameData;
import model.SimpleGameData;

import java.util.Collection;

public interface GameDAO {
    void removeAll() throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    int addGame(String gameName, ChessGame game) throws DataAccessException;

    void updateGame(GameData gameData) throws DataAccessException;

    Collection<SimpleGameData> getGames() throws DataAccessException;

    boolean gameExists(int gameID) throws DataAccessException;
}
