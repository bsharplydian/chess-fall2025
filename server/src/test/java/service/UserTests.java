package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.exceptions.ForbiddenException;
import dataaccess.exceptions.UnauthorizedException;
import model.UserData;
import org.junit.jupiter.api.*;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.requests.RegisterRequest;
import service.results.LoginResult;
import service.results.RegisterResult;

public class UserTests {
    private static UserService userService;
    @BeforeAll
    static void init() {

    }
    @BeforeEach
    void resetService() {
        userService = new UserService(new MemoryAuthDAO(), new MemoryUserDAO());
    }
    @Test
    @DisplayName("register creates a user")
    public void registerSuccess() {
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
    public void registerAlreadyExists() {
        RegisterRequest request = new RegisterRequest("jim", "secret", "jim@jim.com");
        userService.register(request);

        RegisterRequest request2 = new RegisterRequest("jim", "mynameisalsojim", "jim@jim2.com");
        Assertions.assertThrows(ForbiddenException.class, () -> userService.register(request2));
    }

    // logout success
    @Test
    @DisplayName("logout logs the user out")
    public void logoutSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("james", "secret", "james@jim.com");
        RegisterResult result = userService.register(registerRequest);

        LogoutRequest request = new LogoutRequest(result.authToken());
        Assertions.assertDoesNotThrow(() -> userService.logout(request));

    }
    // logout unauthorized
    @Test
    @DisplayName("logout throws error if there is no valid authToken")
    public void logoutUnauthorized() {
        LogoutRequest request = new LogoutRequest("");
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.logout(request));
    }

    // login success
    @Test
    @DisplayName("login success")
    public void loginSuccess() {
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
    public void loginWrongPassword() {
        RegisterRequest registerRequest = new RegisterRequest("jim", "secret", "jim@jim.com");
        userService.register(registerRequest);

        LoginRequest request = new LoginRequest("jim", "1234");
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.login(request));
    }
}
