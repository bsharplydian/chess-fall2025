package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData authData);

    AuthData getAuth(String token);

    void removeAuth(String token);
}
