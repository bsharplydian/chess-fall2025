package service.handlers;

import com.google.gson.Gson;
import dataaccess.exceptions.*;
import service.UserService;
import service.requests.*;

public class UserJsonHandler {
    private final UserService userService;
    Gson gson = new Gson();
    public UserJsonHandler(UserService userService) {
        this.userService = userService;
    }

    public String register(String registerJson) throws ForbiddenException, BadRequestException {
        RegisterRequest request = gson.fromJson(registerJson, RegisterRequest.class);
        System.out.print(request);
        return gson.toJson(userService.register(request));
    }

    public String login(String loginJson) throws BadRequestException, UnauthorizedException {
        LoginRequest request = gson.fromJson(loginJson, LoginRequest.class);
        System.out.print(request);
        return gson.toJson(userService.login(request));
    }

    public void logout(String logoutJson) throws UnauthorizedException {
        LogoutRequest request = gson.fromJson(logoutJson, LogoutRequest.class);
        System.out.print(request);
        userService.logout(request);
    }
}
