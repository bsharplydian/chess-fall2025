package service;

import dataaccess.*;
import dataaccess.exceptions.*;
import model.AuthData;
import model.UserData;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.requests.RegisterRequest;
import service.results.LoginResult;
import service.results.RegisterResult;

import java.util.Objects;
import java.util.UUID;

public class UserService {
    AuthDAO authDAO;
    UserDAO userDAO;
    public UserService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public RegisterResult register(RegisterRequest request) throws ForbiddenException, BadRequestException {
        if(request.username().isBlank() || request.password().isBlank() || request.email().isBlank()) {
            throw new BadRequestException("Error: bad request");
        }
        if(userDAO.getUser(request.username()) != null) {
            throw new ForbiddenException("Error: already taken");
        }

        userDAO.createUser(new UserData(request.username(), request.password(), request.email()));
        String authToken = createToken();
        authDAO.createAuth(new AuthData(authToken, request.username()));

        return new RegisterResult(request.username(), authToken);
    }

    public LoginResult login(LoginRequest request) throws BadRequestException, UnauthorizedException {
        if(request.username().isBlank() || request.password().isBlank()) {
            throw new BadRequestException("Error: bad request");
        }
        if(userDAO.getUser(request.username()) == null) {
            throw new UnauthorizedException("Error: user doesn't exist");
        }
        if(!Objects.equals(userDAO.getUser(request.username()).password(), request.password())) {
            // TODO: add password security
            throw new UnauthorizedException("Error: incorrect password");
        }
        String authToken = createToken();
        authDAO.createAuth(new AuthData(authToken, request.username()));
        return new LoginResult(request.username(), authToken);
    }

    public void logout(LogoutRequest request) throws UnauthorizedException {
        if(authDAO.getAuth(request.authToken()) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        authDAO.removeAuth(request.authToken());
    }

    public void clear() {
        authDAO.removeAll();
        userDAO.removeAll();
    }

    private String createToken() {
        return UUID.randomUUID().toString();
    }
}
