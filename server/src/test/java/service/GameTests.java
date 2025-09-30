package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.requests.CreateGameRequest;

public class GameTests {
    private static UserService userService;
    private static GameService gameService;


    @BeforeEach
    void resetService() {


    }

    @Test
    @DisplayName("create game success")
    public void CreateGameSuccess() {

        CreateGameRequest request = new CreateGameRequest("", "");
    }
}
