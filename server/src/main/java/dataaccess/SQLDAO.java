package dataaccess;

import dataaccess.exceptions.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLDAO {

    protected int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case Integer p -> ps.setInt(i + 1, p);
                        case null -> ps.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

//    protected ResultSet executeQuery(String statement, Object... params) throws DataAccessException {
//        try(Connection conn = DatabaseManager.getConnection()) {
//            try (PreparedStatement ps = conn.prepareStatement(statement)) {
//                for (int i = 0; i < params.length; i++) {
//                    Object param = params[i];
//                    switch (param) {
//                        case String p -> ps.setString(i + 1, p);
//                        case Integer p -> ps.setInt(i + 1, p);
//                        case null -> ps.setNull(i + 1, NULL);
//                        default -> {
//                        }
//                    }
//                }
//                try(ResultSet rs = ps.executeQuery()) {
//
//                }
//            }
//        } catch (SQLException e) {
//            throw new DataAccessException(String.format("unable to query database: %s, %s", statement, e.getMessage()));
//        }
//    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS authorization (
            `id` int NOT NULL AUTO_INCREMENT,
            `authToken` varchar(256) NOT NULL,
            `username` varchar(256) NOT NULL,
            PRIMARY KEY (`authtoken`),
            INDEX(`id`)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS users (
            `id` int NOT NULL AUTO_INCREMENT,
            `username` varchar(256) NOT NULL,
            `password` varchar(256) NOT NULL,
            `email` varchar(256) NOT NULL,
            PRIMARY KEY (`username`),
            INDEX(`id`)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS games (
            `id` int NOT NULL AUTO_INCREMENT,
            `whiteUsername` varchar(256),
            `blackUsername` varchar(256),
            `gameName` varchar(256) NOT NULL,
            `chessGameJson` varchar(4096) NOT NULL,
            PRIMARY KEY (`id`)
            )
            """
    };
    protected void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to configure database: %s", e.getMessage()));
        }
    }
}
