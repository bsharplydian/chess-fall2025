package service;

import chess.ChessGame;
import dataaccess.*;
import dataaccess.exceptions.UnauthorizedException;
import model.GameData;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.requests.ListGamesRequest;
import service.results.CreateGameResult;
import service.results.ListGamesResult;

import java.util.Random;

public class GameService {
    AuthDAO authDAO;
    GameDAO gameDAO;
    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
    public ListGamesResult listGames(ListGamesRequest request) {
        throw new RuntimeException("not implemented");
    }

    public CreateGameResult createGame(CreateGameRequest request) {
        if(authDAO.getAuth(request.authToken()) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        int gameID = generateGameID();
        gameDAO.addGame(new GameData(gameID, null, null, request.gameName(), new ChessGame()));

        return new CreateGameResult(gameID);

    }

    public void joinGame(JoinGameRequest request) {
        throw new RuntimeException("not implemented");
    }

    public void clear() {
        gameDAO.removeAll();
    }

    private int generateGameID() {
        Random random = new Random();
        int gameID;
        do {
            gameID = random.nextInt(10000);
        } while (gameDAO.gameExists(gameID));
        return gameID;
    }
}
