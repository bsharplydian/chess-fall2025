package service.handlers;

import com.google.gson.Gson;
import dataaccess.BadRequestException;
import dataaccess.ForbiddenException;
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
}
