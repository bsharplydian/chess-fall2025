package client;

import model.requests.CreateGameRequest;
import model.requests.LoginRequest;
import model.requests.RegisterRequest;
import model.results.CreateGameResult;
import model.results.LoginResult;
import model.results.RegisterResult;
import org.junit.jupiter.api.*;
import server.Server;
import serverfacade.HttpResponseException;
import serverfacade.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    private RegisterResult existingUserResult;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void reset() throws HttpResponseException {
        facade.clear();
        RegisterRequest request = new RegisterRequest("tim", "tim", "tim@tim.com");
        existingUserResult = facade.register(request);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerSuccess() throws HttpResponseException {
        RegisterRequest request = new RegisterRequest("jim", "jim", "jim@jim.com");
        RegisterResult result = facade.register(request);
        Assertions.assertTrue(result.authToken().length() > 10);

    }

    @Test
    public void registerAlreadyTaken() throws HttpResponseException {
        RegisterRequest request = new RegisterRequest("jim", "jim", "jim@jim.com");
        facade.register(request);
        Assertions.assertThrows(HttpResponseException.class, ()->facade.register(request));
    }

    @Test
    public void logoutSuccess() throws HttpResponseException {
        Assertions.assertDoesNotThrow(()->facade.logout(existingUserResult.authToken()));

    }

    @Test
    public void logoutUnauthorized() throws HttpResponseException {
        Assertions.assertThrows(HttpResponseException.class, ()->facade.logout("nonexistent_token"));
    }

    @Test
    public void loginSuccess() throws HttpResponseException {
        facade.logout(existingUserResult.authToken());
        LoginRequest loginRequest = new LoginRequest("tim", "tim");
        LoginResult loginResult = facade.login(loginRequest);
        Assertions.assertTrue(loginResult.authToken().length() > 10);
    }

    @Test
    public void loginWrongPassword() throws HttpResponseException {
        facade.logout(existingUserResult.authToken());
        LoginRequest loginRequest = new LoginRequest("tim", "wrongPassword");
        Assertions.assertThrows(HttpResponseException.class, ()->facade.login(loginRequest));
    }

    @Test
    public void createGameSuccess() throws HttpResponseException {
        CreateGameRequest request = new CreateGameRequest("timsgame");
        CreateGameResult result = facade.createGame(request, existingUserResult.authToken());
        Assertions.assertTrue(result.gameID() > -1);
    }
}
