package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        String statement = "INSERT INTO authorization (id, authToken, username) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
//        throw new RuntimeException("Not Implemented");
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
    public void removeAll() throws DataAccessException {
        String statement = "INSERT INTO authorization (id, authToken, username) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            try(var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS authorization (
            `id` int NOT NULL AUTO_INCREMENT,
            `authToken` varchar(256) NOT NULL,
            `username` varchar(256) NOT NULL,
            PRIMARY KEY (`authtoken`),
            INDEX(`id`)
            )
            """
    };
    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }
}
