package service;

import dataaccess.MemoryGameDAO;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.requests.ListGamesRequest;
import service.results.CreateGameResult;
import service.results.ListGamesResult;

public class GameService {
    MemoryGameDAO gameDAO = new MemoryGameDAO();
    public ListGamesResult listGames(ListGamesRequest request) {
        throw new RuntimeException("not implemented");
    }

    public CreateGameResult createGame(CreateGameRequest request) {
        throw new RuntimeException("not implemented");
    }

    public void joinGame(JoinGameRequest request) {
        throw new RuntimeException("not implemented");
    }

    public void clear() {
        gameDAO.removeAll();
    }
}
