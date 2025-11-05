package service.handlers;

import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import io.javalin.http.Context;
import service.GameService;
import model.requests.CreateGameRequest;
import model.requests.JoinGameRequest;
import model.results.CreateGameResult;
import model.results.ListGamesResult;

public class GameJsonHandler {
    private final GameService gameService;
    Gson gson = new Gson();

    public GameJsonHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public String listGames(Context context) throws DataAccessException {
        ListGamesResult result = gameService.listGames(context.header("authorization"));
        String resultString = gson.toJson(result);
        return resultString;
    }

    public String createGame(Context context) throws DataAccessException {

        CreateGameRequest request = gson.fromJson(context.body(), CreateGameRequest.class);
        CreateGameResult result = gameService.createGame(context.header("authorization"), request);
        return gson.toJson(result);
    }

    public void joinGame(Context context) throws DataAccessException {
        JoinGameRequest request = gson.fromJson(context.body(), JoinGameRequest.class);
        gameService.joinGame(context.header("authorization"), request);
    }
}
