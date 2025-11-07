package client;

import model.requests.LoginRequest;
import model.requests.RegisterRequest;
import model.results.LoginResult;
import model.results.RegisterResult;
import org.junit.jupiter.api.*;
import server.Server;
import serverfacade.HttpResponseException;
import serverfacade.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

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
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerSuccess() throws HttpResponseException {
        RegisterRequest request = new RegisterRequest("tim", "tim", "tim@tim.com");
        var result = facade.register(request);
        Assertions.assertTrue(result.authToken().length() > 10);

    }

    @Test
    public void registerAlreadyTaken() throws HttpResponseException {
        RegisterRequest request = new RegisterRequest("tim", "tim", "tim@tim.com");
        facade.register(request);
        Assertions.assertThrows(HttpResponseException.class, ()->facade.register(request));
    }

    @Test
    public void logoutSuccess() throws HttpResponseException {
        RegisterRequest request = new RegisterRequest("tim", "tim", "tim@tim.com");
        RegisterResult result = facade.register(request);
        Assertions.assertDoesNotThrow(()->facade.logout(result.authToken()));

    }

    @Test
    public void logoutUnauthorized() throws HttpResponseException {
        Assertions.assertThrows(HttpResponseException.class, ()->facade.logout("nonexistent_token"));
    }

    @Test
    public void loginSuccess() throws HttpResponseException {
        RegisterRequest registerRequest = new RegisterRequest("tim", "tim", "tim@tim.com");
        RegisterResult registerResult = facade.register(registerRequest);
        facade.logout(registerResult.authToken());
        LoginRequest loginRequest = new LoginRequest("tim", "tim");
        LoginResult loginResult = facade.login(loginRequest);
        Assertions.assertTrue(loginResult.authToken().length() > 10);
    }

    @Test
    public void loginWrongPassword() throws HttpResponseException {
        RegisterRequest registerRequest = new RegisterRequest("tim", "tim", "tim@tim.com");
        RegisterResult registerResult = facade.register(registerRequest);
        facade.logout(registerResult.authToken());
        LoginRequest loginRequest = new LoginRequest("tim", "wrongPassword");
        Assertions.assertThrows(HttpResponseException.class, ()->facade.login(loginRequest));
    }


}
