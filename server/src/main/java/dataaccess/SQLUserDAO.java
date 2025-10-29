package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.UserData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO extends SQLDAO implements UserDAO {
    @Override
    public UserData getUser(String username) throws DataAccessException {
        String statement = "SELECT * FROM users WHERE username=?";
        try (Connection conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    if(resultSet.next()) {
                        return new UserData(resultSet.getString("username"),
                                resultSet.getString("password"),
                                resultSet.getString("email"));
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
    public void createUser(UserData userData) throws DataAccessException {
        String statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, userData.username(), userData.password(), userData.email());
    }

    @Override
    public void removeAll() throws DataAccessException {
        String statement = "DELETE FROM users";
        executeUpdate(statement);
    }
}
