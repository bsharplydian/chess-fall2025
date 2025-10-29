package dataaccess;

import chess.ChessGame;
import dataaccess.exceptions.DataAccessException;
import model.GameData;
import model.SimpleGameData;

import java.util.*;

public class MemoryGameDAO implements GameDAO {
    Map<Integer, GameData> games = new HashMap<>();
    Integer nextID = 0;
    @Override
    public void removeAll() {
        games.clear();
    }

    @Override
    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public int addGame(String gameName, ChessGame game) {
        int gameID = generateGameID();
        games.put(gameID, new GameData(gameID, null, null, gameName, game));
        return gameID;
    }

    @Override
    public void updateGame(GameData gameData) {
        games.put(gameData.gameID(), gameData);
    }

    @Override
    public Collection<SimpleGameData> getGames() {
        ArrayList<SimpleGameData> simpleList = new ArrayList<>();
        for(GameData game : games.values()) {
            simpleList.add(new SimpleGameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
        }
        return simpleList;
    }
    private int generateGameID() {
        int newID = nextID;
        nextID += 1;
        return newID;
    }

    @Override
    public boolean gameExists(int gameID) {
        return getGame(gameID) != null;
    }
}
