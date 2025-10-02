package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void removeAll();

    GameData getGame(int gameID);

    void addGame(GameData gameData);

    void updateGame(GameData gameData);

    ArrayList<GameData> getGames();

    boolean gameExists(int gameID);
}
