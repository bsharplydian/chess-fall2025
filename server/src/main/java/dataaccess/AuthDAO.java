package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData authData) throws DataAccessException;

    AuthData getAuth(String token) throws DataAccessException;

    void removeAuth(String token) throws DataAccessException;

    void removeAll() throws DataAccessException;
}
