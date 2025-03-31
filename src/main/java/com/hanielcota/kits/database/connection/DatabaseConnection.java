package com.hanielcota.kits.database.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@Getter
public class DatabaseConnection {

    private final HikariDataSource dataSource;

    public DatabaseConnection(String host, int port, String database, String username, String password) {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&allowPublicKeyRetrieval=true");
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(50);
        config.setMinimumIdle(10);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
    }

    public Optional<Connection> getConnection() {
        try {
            return Optional.of(dataSource.getConnection());
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Erro ao obter conexão: " + e.getMessage());
            return Optional.empty();
        }
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) dataSource.close();
    }
}