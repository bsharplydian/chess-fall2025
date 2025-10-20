package dataaccess;

import model.UserData;

public class SQLUserDAO implements UserDAO {
    @Override
    public UserData getUser(String username) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void createUser(UserData userData) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void removeAll() {
        throw new RuntimeException("Not Implemented");
    }
}
