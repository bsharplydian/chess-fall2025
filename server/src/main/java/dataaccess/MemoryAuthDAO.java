package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    HashMap<String, AuthData> authMap = new HashMap<>();
    @Override
    public void createAuth(AuthData authData) {
        authMap.put(authData.authToken(), authData);
    }

    @Override
    public AuthData getAuth(String token) {
        return authMap.get(token);
    }

    @Override
    public void removeAuth(String token) {
        authMap.remove(token);
    }

    @Override
    public void removeAll() {
        authMap = new HashMap<>();
    }
}
