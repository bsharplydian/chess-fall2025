package service;

import dataaccess.BadRequestException;
import dataaccess.ForbiddenException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
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
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    MemoryUserDAO userDAO = new MemoryUserDAO();
    public UserService() {
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

    public LoginResult login(LoginRequest request) {
        if(request.username().isBlank() || request.password().isBlank()) {
            //bad request
        }
        if(userDAO.getUser(request.username()) == null) {
            // unauthorized: username doesn't exist
        }
        if(!Objects.equals(userDAO.getUser(request.username()).password(), request.password())) {
            // unauthorized: wrong password
        }
        String authToken = createToken();
        authDAO.createAuth(new AuthData(authToken, request.username()));
        return new LoginResult(authToken);
    }

    public void logout(LogoutRequest request) {
        if(authDAO.getAuth(request.authToken()) == null) {
            //unauthorized exception
        }
        authDAO.removeAuth(request.authToken());
    }

    private String createToken() {
        return UUID.randomUUID().toString();
    }
}
