package service;

import dataaccess.AlreadyTakenException;
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
    public RegisterResult register(RegisterRequest request) throws AlreadyTakenException {
        if(userDAO.getUser(request.username()) != null) {
            throw new AlreadyTakenException("Username already taken");
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
