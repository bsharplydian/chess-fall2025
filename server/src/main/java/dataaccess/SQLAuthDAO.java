package dataaccess;

import model.AuthData;

public class SQLAuthDAO implements AuthDAO{
    @Override
    public void createAuth(AuthData authData) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public AuthData getAuth(String token) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void removeAuth(String token) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void removeAll() {
        throw new RuntimeException("Not Implemented");
    }
}
