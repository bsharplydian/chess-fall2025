package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO {
    @Override
    public void removeAll() {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public GameData getGame(int gameID) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void addGame(GameData gameData) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void updateGame(GameData gameData) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public Collection<GameData> getGames() {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean gameExists(int gameID) {
        throw new RuntimeException("Not Implemented");
    }
}
