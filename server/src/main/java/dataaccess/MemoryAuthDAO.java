package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    HashMap<String, AuthData> authMap = new HashMap<>();
    @Override
    public void createAuth(AuthData authData) {
        authMap.put(authData.authToken(), authData);
    }
}
