package service;

import dataaccess.*;
import dataaccess.exceptions.*;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import service.requests.LoginRequest;
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
    public RegisterResult register(RegisterRequest request) throws ForbiddenException, BadRequestException, DataAccessException {
        if(request.username() == null || request.password() == null || request.email() == null) {
            throw new BadRequestException("Error: bad request");
        }
        if(userDAO.getUser(request.username()) != null) {
            throw new ForbiddenException("Error: already taken");
        }
        String hashedPass = BCrypt.hashpw(request.password(), BCrypt.gensalt());
        userDAO.createUser(new UserData(request.username(), hashedPass, request.email()));
        String authToken = createToken();
        authDAO.createAuth(new AuthData(authToken, request.username()));
        return new RegisterResult(request.username(), authToken);
    }

    public LoginResult login(LoginRequest request) throws BadRequestException, UnauthorizedException, DataAccessException {
        if(request.username() == null || request.password() == null) {
            throw new BadRequestException("Error: bad request");
        }
        if(userDAO.getUser(request.username()) == null) {
            throw new UnauthorizedException("Error: user doesn't exist");
        }
        if(!BCrypt.checkpw(request.password(), userDAO.getUser(request.username()).password())) {
            throw new UnauthorizedException("Error: incorrect password");
        }
        String authToken = createToken();
        authDAO.createAuth(new AuthData(authToken, request.username()));
        return new LoginResult(request.username(), authToken);
    }

    public void logout(String authToken) throws UnauthorizedException, DataAccessException {
        if(authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        authDAO.removeAuth(authToken);
    }

    public void clear() throws DataAccessException {
        authDAO.removeAll();
        userDAO.removeAll();
    }

    private String createToken() {
        return UUID.randomUUID().toString();
    }
}
