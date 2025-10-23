package service.handlers;

import com.google.gson.Gson;
import dataaccess.exceptions.*;
import io.javalin.http.Context;
import service.UserService;
import service.requests.*;

public class UserJsonHandler {
    private final UserService userService;
    Gson gson = new Gson();
    public UserJsonHandler(UserService userService) {
        this.userService = userService;
    }

    public String register(Context context) throws ForbiddenException, BadRequestException, DataAccessException {
        RegisterRequest request = gson.fromJson(context.body(), RegisterRequest.class);
        return gson.toJson(userService.register(request));
    }

    public String login(Context context) throws BadRequestException, UnauthorizedException, DataAccessException {
        LoginRequest request = gson.fromJson(context.body(), LoginRequest.class);
        return gson.toJson(userService.login(request));
    }

    public void logout(Context context) throws UnauthorizedException, DataAccessException {
        userService.logout(context.header("authorization"));
    }
}
