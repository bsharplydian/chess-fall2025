package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.exceptions.UnauthorizedException;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.requests.CreateGameRequest;
import service.requests.ListGamesRequest;
import service.requests.RegisterRequest;
import service.results.CreateGameResult;
import service.results.ListGamesResult;
import service.results.RegisterResult;

public class GameTests {
    static MemoryAuthDAO authDAO = new MemoryAuthDAO();
    private static UserService userService =  new UserService(authDAO, new MemoryUserDAO());
    private static GameService gameService = new GameService(authDAO, new MemoryGameDAO());
    private static String authToken;

    @BeforeEach
    void reset() {
        userService.clear();
        gameService.clear();
        RegisterRequest existingUser = new RegisterRequest("existingUser", "pw", "iexist@mailmail.com");
        RegisterResult registerResult = userService.register(existingUser);
        authToken = registerResult.authToken();
    }

    @Test
    @DisplayName("create game success")
    public void CreateGameSuccess() {
        CreateGameRequest request = new CreateGameRequest(authToken, "newGame");
        CreateGameResult result = Assertions.assertDoesNotThrow(() -> gameService.createGame(request));
        Assertions.assertNotNull(gameService.gameDAO.getGame(result.gameID()));
    }

    @Test
    @DisplayName("create game unauthorized")
    public void CreateGameNoAuth() {
        CreateGameRequest request = new CreateGameRequest("", "newGame");
        Assertions.assertThrows(UnauthorizedException.class, () ->
                gameService.createGame(request)
        );
        Assertions.assertEquals(0, gameService.gameDAO.getGames().size());
    }

    @Test
    @DisplayName("list games success")
    public void ListGamesSuccess() {
        CreateGameRequest request1 = new CreateGameRequest(authToken, "game1");
        gameService.createGame(request1);
        CreateGameRequest request2 = new CreateGameRequest(authToken, "game2");
        gameService.createGame(request2);

        ListGamesRequest listRequest = new ListGamesRequest(authToken);
        ListGamesResult listResult = Assertions.assertDoesNotThrow(() -> gameService.listGames(listRequest));

        Assertions.assertEquals(2, listResult.games().size(), "listGames returned incorrect number of games");
    }
}
