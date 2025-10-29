package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import model.GameData;
import model.SimpleGameData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO extends SQLDAO implements GameDAO {
    @Override
    public void removeAll() throws DataAccessException {
        String statement = "DELETE FROM games";
        executeUpdate(statement);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String statement = "SELECT * FROM games WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, gameID);
                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    if(resultSet.next()) {
                        return new GameData(resultSet.getInt("id"),
                                resultSet.getString("whiteUsername"),
                                resultSet.getString("blackUsername"),
                                resultSet.getString("gameName"),
                                new Gson().fromJson(resultSet.getString("chessGameJSON"), ChessGame.class));
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public int addGame(String gameName, ChessGame game) throws DataAccessException {
        String statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, chessGameJson) VALUES (?, ?, ?, ?)";
        String gameJson = new Gson().toJson(game, ChessGame.class);
        return executeUpdate(statement, null, null, gameName, gameJson);
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        String statement = "UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, chessGameJSON = ? WHERE id = ?";
        executeUpdate(statement, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), new Gson().toJson(gameData.game()), gameData.gameID());
    }

    @Override
    public Collection<SimpleGameData> getGames() throws DataAccessException {
        ArrayList<SimpleGameData> games = new ArrayList<>();
        String statement = "SELECT * FROM games";
        try (Connection conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement(statement)) {
                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    while(resultSet.next()) {
                        SimpleGameData game = new SimpleGameData(resultSet.getInt("id"),
                                resultSet.getString("whiteUsername"),
                                resultSet.getString("blackUsername"),
                                resultSet.getString("gameName"));
                        games.add(game);
                    }
                    return games;
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public boolean gameExists(int gameID) throws DataAccessException {
        return getGame(gameID) != null;
    }
}
