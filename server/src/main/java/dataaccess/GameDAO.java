package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public interface GameDAO {
    void removeAll();

    GameData getGame(int gameID);

    void addGame(GameData gameData);

    void updateGame(GameData gameData);

    Collection<GameData> getGames();

    boolean gameExists(int gameID);
}
