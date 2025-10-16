package service.handlers;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.GameService;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.results.CreateGameResult;
import service.results.ListGamesResult;

public class GameJsonHandler {
    private final GameService gameService;
    Gson gson = new Gson();

    public GameJsonHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public String listGames(Context context) {
        ListGamesResult result = gameService.listGames(context.header("authorization"));
        return gson.toJson(result);
    }

    public String createGame(Context context) {

        CreateGameRequest request = gson.fromJson(context.body(), CreateGameRequest.class);
        CreateGameResult result = gameService.createGame(context.header("authorization"), request);
        return gson.toJson(result);
    }

    public void joinGame(Context context) {
        JoinGameRequest request = gson.fromJson(context.body(), JoinGameRequest.class);
        gameService.joinGame(context.header("authorization"), request);
    }
}
