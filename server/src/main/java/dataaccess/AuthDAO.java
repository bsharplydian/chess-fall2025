package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData authData) throws DataAccessException;

    AuthData getAuth(String token);

    void removeAuth(String token);

    void removeAll();
}
