package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.exceptions.ForbiddenException;
import dataaccess.exceptions.UnauthorizedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
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
    public void createGameSuccess() {
        CreateGameRequest request = new CreateGameRequest("newGame");
        CreateGameResult result = Assertions.assertDoesNotThrow(() -> gameService.createGame(authToken1, request));
        Assertions.assertNotNull(gameService.gameDAO.getGame(result.gameID()));
    }

    @Test
    @DisplayName("create game unauthorized")
    public void createGameNoAuth() {
        CreateGameRequest request = new CreateGameRequest("newGame");
        Assertions.assertThrows(UnauthorizedException.class, () ->
                gameService.createGame("", request)
        );
        Assertions.assertEquals(0, gameService.gameDAO.getGames().size());
    }

    @Test
    @DisplayName("list games success")
    public void listGamesSuccess() {
        CreateGameRequest request1 = new CreateGameRequest("game1");
        gameService.createGame(authToken1, request1);
        CreateGameRequest request2 = new CreateGameRequest("game2");
        gameService.createGame(authToken1, request2);

        ListGamesResult listResult = Assertions.assertDoesNotThrow(() -> gameService.listGames(authToken1));

        Assertions.assertEquals(2, listResult.games().size(), "listGames returned incorrect number of games");
    }

    @Test
    @DisplayName("list games unauthorized")
    public void listGamesNoAuth() {
        CreateGameRequest request1 = new CreateGameRequest("game1");
        gameService.createGame(authToken1, request1);
        CreateGameRequest request2 = new CreateGameRequest("game2");
        gameService.createGame(authToken1, request2);

        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.listGames(""));
    }

    @Test
    @DisplayName("join game success")
    public void joinGameSuccess() {
        CreateGameRequest createRequest = new CreateGameRequest("newGame");
        int gameID = Assertions.assertDoesNotThrow(() -> gameService.createGame(authToken1, createRequest)).gameID();

        JoinGameRequest requestWhite = new JoinGameRequest(chess.ChessGame.TeamColor.WHITE, gameID);
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(authToken1, requestWhite));
        JoinGameRequest requestBlack = new JoinGameRequest(chess.ChessGame.TeamColor.BLACK, gameID);
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(authToken2, requestBlack));

        Assertions.assertNotNull(gameService.gameDAO.getGame(gameID).whiteUsername());
        Assertions.assertNotNull(gameService.gameDAO.getGame(gameID).blackUsername());
    }

    @Test
    @DisplayName("join game taken (white)")
    public void joinGameTakenW() {
        CreateGameRequest createRequest = new CreateGameRequest("newGame");
        int gameID = Assertions.assertDoesNotThrow(() -> gameService.createGame(authToken1, createRequest)).gameID();

        JoinGameRequest requestWhite = new JoinGameRequest(chess.ChessGame.TeamColor.WHITE, gameID);
        gameService.joinGame(authToken1, requestWhite);

        JoinGameRequest requestWhite2 = new JoinGameRequest(chess.ChessGame.TeamColor.WHITE, gameID);
        Assertions.assertThrows(ForbiddenException.class, () -> gameService.joinGame(authToken2, requestWhite2));
    }

    @Test
    @DisplayName("join game taken (black)")
    public void joinGameTakenB() {
        CreateGameRequest createRequest = new CreateGameRequest("newGame");
        int gameID = Assertions.assertDoesNotThrow(() -> gameService.createGame(authToken1, createRequest)).gameID();

        JoinGameRequest requestBlack = new JoinGameRequest(chess.ChessGame.TeamColor.BLACK, gameID);
        gameService.joinGame(authToken1, requestBlack);

        JoinGameRequest requestBlack2 = new JoinGameRequest(chess.ChessGame.TeamColor.BLACK, gameID);
        Assertions.assertThrows(ForbiddenException.class, () -> gameService.joinGame(authToken2, requestBlack2));
    }

    @Test
    @DisplayName("join game unauthorized")
    public void joinGameNoAuth() {
        CreateGameRequest createRequest = new CreateGameRequest("newGame");
        int gameID = gameService.createGame(authToken1, createRequest).gameID();

        JoinGameRequest request = new JoinGameRequest(chess.ChessGame.TeamColor.WHITE, gameID);
        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.joinGame("", request));

        Assertions.assertNull(gameService.gameDAO.getGame(gameID).whiteUsername());
    }

}
