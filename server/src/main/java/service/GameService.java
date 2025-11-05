package service;

import chess.ChessGame;
import dataaccess.*;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.ForbiddenException;
import dataaccess.exceptions.UnauthorizedException;
import model.GameData;
import model.requests.CreateGameRequest;
import model.requests.JoinGameRequest;
import model.results.CreateGameResult;
import model.results.ListGamesResult;

public class GameService {
    AuthDAO authDAO;
    GameDAO gameDAO;
    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
    public ListGamesResult listGames(String authToken) throws DataAccessException {
        if(authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return new ListGamesResult(gameDAO.getGames());
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest request) throws DataAccessException {
        if(request.gameName() == null) {
            throw new BadRequestException("Error: bad request");
        }
        if(authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        int gameID = gameDAO.addGame( request.gameName(), new ChessGame());

        return new CreateGameResult(gameID);

    }

    public void joinGame(String authToken, JoinGameRequest request) throws DataAccessException {
        if(authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        if(!gameDAO.gameExists(request.gameID()) || request.playerColor() == null) {
            throw new BadRequestException("Error: bad request");
        }
        GameData existingData = gameDAO.getGame(request.gameID());
        if((request.playerColor() == ChessGame.TeamColor.WHITE && existingData.whiteUsername() != null) ||
        request.playerColor() == ChessGame.TeamColor.BLACK && existingData.blackUsername() != null) {
            throw new ForbiddenException("Error: already taken");
        }
        String username = authDAO.getAuth(authToken).username();
        GameData newData = switch(request.playerColor()){
            case WHITE -> new GameData(existingData.gameID(), username, existingData.blackUsername(), existingData.gameName(), existingData.game());
            case BLACK -> new GameData(existingData.gameID(), existingData.whiteUsername(), username, existingData.gameName(), existingData.game());
        };
        gameDAO.updateGame(newData);
    }

    public void clear() throws DataAccessException {
        gameDAO.removeAll();
    }

}
