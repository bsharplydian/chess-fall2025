package dataaccess;

import chess.ChessGame;
import dataaccess.exceptions.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;


public class DatabaseUnitTests {
    private static SQLUserDAO userDAO;
    private static SQLAuthDAO authDAO;
    private static SQLGameDAO gameDAO;

    @BeforeAll
    public static void init() throws DataAccessException {
        userDAO = new SQLUserDAO();
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();
    }

    @BeforeEach()
    public void reset() throws DataAccessException {
        userDAO.removeAll();
        authDAO.removeAll();
        gameDAO.removeAll();
    }

    /////////////////////////////////////////////////////////////////
    @Test
    @DisplayName("Create User Success")
    public void createUserSuccess() throws DataAccessException {
        UserData data = new UserData("jim", "secret", "jim@jim.com");
        userDAO.createUser(data);
        Assertions.assertEquals(data, userDAO.getUser("jim"));
    }

    @Test
    @DisplayName("Create User Failure")
    public void createUserFailure() {
        UserData data = new UserData("jim", null, "jim@jim.com");
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.createUser(data));
    }

    @Test
    @DisplayName("Get User Success")
    public void getUserSuccess() throws DataAccessException {
        UserData data = new UserData("james", "secret", "james@jim.com");
        userDAO.createUser(data);
        Assertions.assertDoesNotThrow(() -> userDAO.getUser("james"));
    }

    @Test
    @DisplayName("Get User Failure")
    public void getUserDoesntExist() throws DataAccessException {
        Assertions.assertNull(userDAO.getUser("phrederick"));
    }

    @Test
    @DisplayName("Clear Users Success")
    public void clearUsersSuccess() throws DataAccessException {
        UserData data = new UserData("james", "secret", "james@jim.com");
        userDAO.createUser(data);
        userDAO.removeAll();
        Assertions.assertNull(userDAO.getUser("james"));
    }

    /////////////////////////////////////////////////////////////////
    @Test
    @DisplayName("Create Auth Success")
    public void createAuthSuccess() throws DataAccessException {
        AuthData data = new AuthData("token", "james");
        authDAO.createAuth(data);
        Assertions.assertEquals(data, authDAO.getAuth("token"));
    }

    @Test
    @DisplayName("Create Auth Failure")
    public void createAuthFailure() {
        AuthData data = new AuthData(null, "jim");
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.createAuth(data));
    }

    @Test
    @DisplayName("Get Auth Success")
    public void getAuthSuccess() throws DataAccessException {
        AuthData data = new AuthData("token", "james");
        authDAO.createAuth(data);
        Assertions.assertNotNull(authDAO.getAuth("token"));
    }

    @Test
    @DisplayName("Get Auth Failure")
    public void getAuthDoesntExist() throws DataAccessException {
        Assertions.assertNull(authDAO.getAuth("nonexistent_token"));
    }

    @Test
    @DisplayName("Remove Auth Success")
    public void removeAuthSuccess() throws DataAccessException {
        AuthData data = new AuthData("token", "james");
        authDAO.createAuth(data);
        Assertions.assertNotNull(authDAO.getAuth("token"));
        authDAO.removeAuth("token");
        Assertions.assertNull(authDAO.getAuth("token"));
    }

    @Test
    @DisplayName("Remove Auth Failure")
    public void removeAuthFailure() {
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.removeAuth(null));
    }

    @Test
    @DisplayName("Clear Auths Success")
    public void clearAuthsSuccess() throws DataAccessException {
        AuthData data = new AuthData("token", "james");
        authDAO.createAuth(data);
        authDAO.removeAll();
        Assertions.assertNull(authDAO.getAuth("token"));
    }

    /////////////////////////////////////////////////////////
    @Test
    @DisplayName("Add Game Success")
    public void addGameSuccess() throws DataAccessException {
        ChessGame game = new ChessGame();
        int gameID = gameDAO.addGame("first", game);
        Assertions.assertEquals(game, gameDAO.getGame(gameID).game());
    }

    @Test
    @DisplayName("Add Game Failure")
    public void addGameFailure() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.addGame(null, null));
    }

    @Test
    @DisplayName("Get Game Success")
    public void getGameSuccess() throws DataAccessException {
        ChessGame game = new ChessGame();
        int gameID = gameDAO.addGame("first", game);
        Assertions.assertDoesNotThrow(() -> gameDAO.getGame(gameID).game());
    }

    @Test
    @DisplayName("Get Game Failure")
    public void getGameFailure() throws DataAccessException {
        Assertions.assertNull(gameDAO.getGame(4097));
    }

    @Test
    @DisplayName("Update Game Success")
    public void updateGameSuccess() throws DataAccessException {
        ChessGame game = new ChessGame();
        int gameID = gameDAO.addGame("updateSuccess", game);
        gameDAO.updateGame(new GameData(gameID, "newWhite", "newBlack", "updateSuccess", new ChessGame()));
        Assertions.assertEquals(new GameData(gameID, "newWhite", "newBlack", "updateSuccess", new ChessGame()), gameDAO.getGame(gameID));
    }

    @Test
    @DisplayName("Update Game Failure")
    public void updateGameFailure() throws DataAccessException {
        ChessGame game = new ChessGame();
        int gameID = gameDAO.addGame("updateFailure", game);
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGame(new GameData(gameID, null, null, null, null)));
    }

    @Test
    @DisplayName("Get Games Success")
    public void getGamesSuccess() throws DataAccessException {
        gameDAO.addGame("game1", new ChessGame());
        gameDAO.addGame("game2", new ChessGame());
        gameDAO.addGame("game3", new ChessGame());
        var gameList = gameDAO.getGames();
        Assertions.assertEquals(3, gameList.size());
    }

    @Test
    @DisplayName("Get Games Empty")
    public void getGamesEmpty() throws DataAccessException {
        var gameList = gameDAO.getGames();
        Assertions.assertEquals(0, gameList.size());
    }

    @Test
    @DisplayName("Game Exists Success")
    public void gameExistsSuccess() throws DataAccessException {
        int gameID = gameDAO.addGame("game1", new ChessGame());
        Assertions.assertTrue(gameDAO.gameExists(gameID));
    }

    @Test
    @DisplayName("Game Exists Failure")
    public void gameExistsFailure() throws DataAccessException {
        Assertions.assertFalse(gameDAO.gameExists(4096));
    }

    @Test
    @DisplayName("Clear Games Success")
    public void clearGamesSuccess() throws DataAccessException {
        gameDAO.addGame("game1", new ChessGame());
        gameDAO.addGame("game2", new ChessGame());
        gameDAO.addGame("game3", new ChessGame());
        gameDAO.removeAll();
        Assertions.assertEquals(0, gameDAO.getGames().size());
    }

}
