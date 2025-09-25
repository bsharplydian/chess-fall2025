package service;

import dataaccess.BadRequestException;
import dataaccess.ForbiddenException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import service.requests.RegisterRequest;
import service.results.RegisterResult;

import java.util.UUID;

public class UserService {
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    MemoryUserDAO userDAO = new MemoryUserDAO();
    public UserService() {
    }
    public RegisterResult register(RegisterRequest request) throws ForbiddenException, BadRequestException {
        if(userDAO.getUser(request.username()) != null) {
            throw new ForbiddenException("Error: already taken");
        }
        if(request.username().isBlank() || request.password().isBlank() || request.email().isBlank()) {
            throw new BadRequestException("Error: bad request");
        }
        userDAO.createUser(new UserData(request.username(), request.password(), request.email()));
        String authToken = createToken();
        authDAO.createAuth(new AuthData(authToken, request.username()));

        return new RegisterResult(request.username(), authToken);
    }

    private String createToken() {
        return UUID.randomUUID().toString();
    }
}
