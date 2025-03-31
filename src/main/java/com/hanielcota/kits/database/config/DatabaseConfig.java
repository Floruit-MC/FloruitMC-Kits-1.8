package com.hanielcota.kits.database.config;

import com.hanielcota.kits.database.connection.DatabaseConnection;
import com.hanielcota.kits.utils.ConfigUtil;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@RequiredArgsConstructor
public class DatabaseConfig {

    private final ConfigUtil configUtil;

    /**
     * Creates a new database connection using the configuration values.
     *
     * @return a new instance of {@link DatabaseConnection}
     * @throws SQLException if there is an error creating the connection
     */
    public DatabaseConnection createConnection() throws SQLException {
        try {
            String host = configUtil.getStringCached("database.host", "localhost");
            int port = configUtil.getIntCached("database.port", 3306);
            String dbName = configUtil.getStringCached("database.name", "kits_db");
            String user = configUtil.getStringCached("database.user", "root");
            String password = configUtil.getStringCached("database.password", "password");

            return new DatabaseConnection(host, port, dbName, user, password);
        } catch (Exception e) {
            String errorMessage = "Failed to create database connection: " + e.getMessage();
            throw new SQLException(errorMessage, e);
        }
    }
}