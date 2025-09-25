package service;

import dataaccess.exceptions.ForbiddenException;
import org.junit.jupiter.api.*;
import service.requests.RegisterRequest;
import service.results.RegisterResult;

public class UserTests {
    private static UserService userService;
    @BeforeAll
    static void init() {
        userService = new UserService();
    }
    @Test
    @Order(1)
    @DisplayName("register creates a user")
    public void registerSuccess() {
        RegisterRequest request = new RegisterRequest("jim", "secret", "jim@jim.com");
        RegisterResult result = userService.register(request);
        Assertions.assertEquals(result.username(), "jim");
        Assertions.assertEquals(result.authToken().length(), 36);
        //also check to see if the DAO actually contains the item

    }

    @Test
    @Order(2) //dependent on registerSuccess to properly test. change?
    @DisplayName("register throws exception if username exists")
    public void registerAlreadyExists() {
        RegisterRequest request = new RegisterRequest("jim", "mynameisalsojim", "jim@jim2.com");
        Assertions.assertThrows(ForbiddenException.class, () -> userService.register(request));
    }

    // login success
    // login has invalid username
    // login has invalid password

    // logout success
    // logout unauthorized

    // create game success
}
