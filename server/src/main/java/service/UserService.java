package service;

import service.requests.RegisterRequest;
import service.results.RegisterResult;

public class UserService {
    public UserService() {
    }
    public RegisterResult register(RegisterRequest request) {
        return new RegisterResult("name", "token");
    }
}
