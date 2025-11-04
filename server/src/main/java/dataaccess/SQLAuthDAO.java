package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.AuthData;
import model.UserData;

import java.sql.Connection;
import java.sql.ResultSet;
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
    public AuthData getAuth(String token) throws DataAccessException {
        String statement = "SELECT * FROM authorization WHERE authToken = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, token);
                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    if(resultSet.next()) {
                        return new AuthData(resultSet.getString("authToken"),
                                resultSet.getString("username"));
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: %s", e.getMessage()));
        }
    }

    @Override
    public void removeAuth(String token) throws DataAccessException {
        if(token == null){
            throw new DataAccessException("Error: invalid authorization");
        }
        String statement = "DELETE FROM authorization WHERE authToken = ?";
        executeUpdate(statement, token);
    }

    @Override
    public void removeAll() throws DataAccessException {
        String statement = "DELETE FROM authorization";
        executeUpdate(statement);
    }


}
