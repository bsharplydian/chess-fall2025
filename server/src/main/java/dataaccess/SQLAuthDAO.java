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
