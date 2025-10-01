package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.exceptions.UnauthorizedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.requests.ListGamesRequest;
import service.requests.RegisterRequest;
import service.results.CreateGameResult;
import service.results.ListGamesResult;
import service.results.RegisterResult;

public class GameTests {
    static MemoryAuthDAO authDAO = new MemoryAuthDAO();
    private static UserService userService =  new UserService(authDAO, new MemoryUserDAO());
    private static GameService gameService = new GameService(authDAO, new MemoryGameDAO());
    private static String authToken1;
    private static String authToken2;

    @BeforeEach
    void reset() {
        userService.clear();
        gameService.clear();
        RegisterRequest user1 = new RegisterRequest("user1", "pw", "user1@mailmail.com");
        RegisterResult registerResult1 = userService.register(user1);
        authToken1 = registerResult1.authToken();

        RegisterRequest user2 = new RegisterRequest("user2", "pw", "user2@mailmail.com");
        RegisterResult registerResult2 = userService.register(user2);
        authToken2 = registerResult2.authToken();
    }

    @Test
    @DisplayName("create game success")
    public void CreateGameSuccess() {
        CreateGameRequest request = new CreateGameRequest(authToken1, "newGame");
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
        CreateGameRequest request1 = new CreateGameRequest(authToken1, "game1");
        gameService.createGame(request1);
        CreateGameRequest request2 = new CreateGameRequest(authToken1, "game2");
        gameService.createGame(request2);

        ListGamesRequest listRequest = new ListGamesRequest(authToken1);
        ListGamesResult listResult = Assertions.assertDoesNotThrow(() -> gameService.listGames(listRequest));

        Assertions.assertEquals(2, listResult.games().size(), "listGames returned incorrect number of games");
    }

    @Test
    @DisplayName("list games unauthorized")
    public void ListGamesNoAuth() {
        CreateGameRequest request1 = new CreateGameRequest(authToken1, "game1");
        gameService.createGame(request1);
        CreateGameRequest request2 = new CreateGameRequest(authToken1, "game2");
        gameService.createGame(request2);

        ListGamesRequest listRequest = new ListGamesRequest("");
        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.listGames(listRequest));
    }

    @Test
    @DisplayName("join game success (white)")
    public void JoinGameSuccess() {
        CreateGameRequest createRequest = new CreateGameRequest(authToken1, "newGame");
        int gameID = Assertions.assertDoesNotThrow(() -> gameService.createGame(createRequest)).gameID();

        JoinGameRequest request = new JoinGameRequest(authToken1, chess.ChessGame.TeamColor.WHITE, gameID);
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(request));
        Assertions.assertNotNull(gameService.gameDAO.getGame(gameID).whiteUser());
    }
    //join game taken (white)
    //join game success (black)
    //join game taken (black)
    //join game unauthorized

}
