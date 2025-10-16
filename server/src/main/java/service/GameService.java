package service;

import chess.ChessGame;
import dataaccess.*;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.ForbiddenException;
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
        if(authDAO.getAuth(request.authToken()) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return new ListGamesResult(gameDAO.getGames());
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
        if(authDAO.getAuth(request.authToken()) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        if(!gameDAO.gameExists(request.gameID())) {
            throw new BadRequestException("Error: bad request");
        }
        GameData existingData = gameDAO.getGame(request.gameID());
        if((request.playerColor() == ChessGame.TeamColor.WHITE && existingData.whiteUser() != null) ||
        request.playerColor() == ChessGame.TeamColor.BLACK && existingData.blackUser() != null) {
            throw new ForbiddenException("Error: already taken");
        }
        String username = authDAO.getAuth(request.authToken()).username();
        GameData newData = switch(request.playerColor()){
            case WHITE -> new GameData(existingData.gameID(), username, existingData.blackUser(), existingData.gameName(), existingData.game());
            case BLACK -> new GameData(existingData.gameID(), existingData.whiteUser(), username, existingData.gameName(), existingData.game());
        };
        gameDAO.updateGame(newData);
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
