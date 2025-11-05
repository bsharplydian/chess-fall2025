package service;

import dataaccess.SQLAuthDAO;
import dataaccess.SQLUserDAO;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.ForbiddenException;
import dataaccess.exceptions.UnauthorizedException;
import model.UserData;
import org.junit.jupiter.api.*;
import model.requests.LoginRequest;
import model.requests.RegisterRequest;
import model.results.LoginResult;
import model.results.RegisterResult;

public class UserTests {
    private static UserService userService;
    @BeforeAll
    static void init() {

    }
    @BeforeEach
    void resetService() throws DataAccessException {
        SQLAuthDAO authDAO = new SQLAuthDAO();
        SQLUserDAO userDAO = new SQLUserDAO();
        authDAO.removeAll();
        userDAO.removeAll();
        userService = new UserService(authDAO, userDAO);
    }
    @Test
    @DisplayName("register creates a user")
    public void registerSuccess() throws DataAccessException {
        //ensure that the proper UserData object is returned
        RegisterRequest request = new RegisterRequest("jim", "secret", "jim@jim.com");
        RegisterResult result = userService.register(request);
        Assertions.assertEquals("jim", result.username());
        Assertions.assertEquals(result.authToken().length(), 36);

        //also check to see if the DAO actually contains the item
        UserData userData = userService.userDAO.getUser("jim");
        Assertions.assertNotNull(userData);
        Assertions.assertEquals("jim", userData.username());

    }

    @Test
    @DisplayName("register throws exception if username exists")
    public void registerAlreadyExists() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("jim", "secret", "jim@jim.com");
        userService.register(request);

        RegisterRequest request2 = new RegisterRequest("jim", "mynameisalsojim", "jim@jim2.com");
        Assertions.assertThrows(ForbiddenException.class, () -> userService.register(request2));
    }

    // logout success
    @Test
    @DisplayName("logout logs the user out")
    public void logoutSuccess() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("james", "secret", "james@jim.com");
        RegisterResult result = userService.register(registerRequest);

        Assertions.assertDoesNotThrow(() -> userService.logout(result.authToken()));

    }
    // logout unauthorized
    @Test
    @DisplayName("logout throws error if there is no valid authToken")
    public void logoutUnauthorized() {
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.logout(""));
    }

    // login success
    @Test
    @DisplayName("login success")
    public void loginSuccess() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("jim", "secret", "jim@jim.com");
        userService.register(registerRequest);

        LoginRequest request = new LoginRequest("jim", "secret");
        LoginResult result = userService.login(request);
        Assertions.assertEquals(36, result.authToken().length());
    }
    // login has invalid username
    @Test
    @DisplayName("login invalid username")
    public void loginInvalidUsername() {
        LoginRequest request = new LoginRequest("dne", "secret");
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.login(request));
    }
    // login has invalid password
    @Test
    @DisplayName("login wrong password")
    public void loginWrongPassword() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("jim", "secret", "jim@jim.com");
        userService.register(registerRequest);

        LoginRequest request = new LoginRequest("jim", "1234");
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.login(request));
    }
}
