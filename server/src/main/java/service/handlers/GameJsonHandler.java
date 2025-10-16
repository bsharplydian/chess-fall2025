package service.handlers;

import com.google.gson.Gson;
import service.GameService;
import service.requests.CreateGameRequest;
import service.requests.ListGamesRequest;
import service.results.CreateGameResult;
import service.results.ListGamesResult;

public class GameJsonHandler {
    private final GameService gameService;
    Gson gson = new Gson();

    public GameJsonHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public String listGames(String listJson) {
        ListGamesRequest request = gson.fromJson(listJson, ListGamesRequest.class);
        ListGamesResult result = gameService.listGames(request);
        return gson.toJson(result);
    }

    public String createGame(String createJson) {
        CreateGameRequest request = gson.fromJson(createJson, CreateGameRequest.class);
        CreateGameResult result = gameService.createGame(request);
        return gson.toJson(result);
    }
}
