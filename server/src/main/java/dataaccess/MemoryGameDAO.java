package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    Map<Integer, GameData> games = new HashMap<>();

    @Override
    public void removeAll() {
        games.clear();
    }

    @Override
    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public void addGame(GameData gameData) {
        games.put(gameData.gameID(), gameData);
    }

    @Override
    public void updateGame(GameData gameData) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public ArrayList<GameData> getGames() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public boolean gameExists(int gameID) {
        return getGame(gameID) != null;
    }
}
