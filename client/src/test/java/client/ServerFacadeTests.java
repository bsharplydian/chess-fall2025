package client;

import model.requests.RegisterRequest;
import org.junit.jupiter.api.*;
import server.Server;
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
    public void reset() {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerTest() {
        RegisterRequest request = new RegisterRequest("tim", "tim", "tim@tim.com");
        var result = facade.register(request);
        Assertions.assertTrue(result.authToken().length() > 10);

    }

    @Test
    public void loginTest() {

    }

}
