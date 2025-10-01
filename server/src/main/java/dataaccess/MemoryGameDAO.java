package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {
    @Override
    public void removeAll() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public GameData getGame(int GameID) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void addGame(GameData gameData) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void updateGame(GameData gameData) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public ArrayList<GameData> getGames() {
        throw new RuntimeException("not implemented");
    }
}
