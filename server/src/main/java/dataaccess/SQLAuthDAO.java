package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLAuthDAO extends SQLDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        String statement = "INSERT INTO authorization (authToken, username) VALUES (?, ?)";
        executeUpdate(statement, authData.authToken(), authData.username());
    }

    @Override
    public AuthData getAuth(String token) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void removeAuth(String token) throws DataAccessException {
        String statement = "DELETE FROM authorization WHERE authToken = ?";
        executeUpdate(statement);
    }

    @Override
    public void removeAll() throws DataAccessException {
        String statement = "DELETE FROM authorization";
        executeUpdate(statement);
    }


}
