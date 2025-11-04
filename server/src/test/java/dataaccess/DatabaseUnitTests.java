package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.AuthData;
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
    public void addGameSuccess() {

    }

    @Test
    @DisplayName("Add Game Failure")
    public void addGameFailure() {

    }

    @Test
    @DisplayName("Get Game Success")
    public void getGameSuccess() {

    }

    @Test
    @DisplayName("Get Game Failure")
    public void getGameFailure() {

    }

    @Test
    @DisplayName("Update Game Success")
    public void updateGameSuccess() {

    }

    @Test
    @DisplayName("Update Game Failure")
    public void updateGameFailure() {

    }

    @Test
    @DisplayName("Get Games Success")
    public void getGamesSuccess() {

    }

    @Test
    @DisplayName("Get Games Failure")
    public void getGamesFailure() {

    }

    @Test
    @DisplayName("Game Exists Success")
    public void gameExistsSuccess() {

    }

    @Test
    @DisplayName("Game Exists Failure")
    public void gameExistsFailure() {

    }

    @Test
    @DisplayName("Clear Games Success")
    public void clearGamesSuccess() {

    }

}
