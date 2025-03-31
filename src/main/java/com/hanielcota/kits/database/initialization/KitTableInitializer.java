package com.hanielcota.kits.database.initialization;

import com.hanielcota.kits.database.connection.DatabaseConnection;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RequiredArgsConstructor
public class KitTableInitializer {

    private final DatabaseConnection databaseConnection;

    public void initialize() {
        var connOptional = databaseConnection.getConnection();
        if (connOptional.isEmpty()) {
            Bukkit.getLogger().severe("Falha ao obter conexão ao inicializar tabelas");
            return;
        }

        try (Connection conn = connOptional.get()) {
            String createKitsTableQuery = "CREATE TABLE IF NOT EXISTS kits ("
                    + "kit_name VARCHAR(50) PRIMARY KEY, "
                    + "cooldown BIGINT NOT NULL DEFAULT 0, "
                    + "items TEXT"
                    + ")";

            try (PreparedStatement stmt = conn.prepareStatement(createKitsTableQuery)) {
                stmt.executeUpdate();
                Bukkit.getLogger().info("Tabela 'kits' criada ou verificada com sucesso.");
            }

            String createCooldownsTableQuery = "CREATE TABLE IF NOT EXISTS player_kit_cooldowns ("
                    + "player_uuid VARCHAR(36), "
                    + "kit_name VARCHAR(50), "
                    + "last_used BIGINT NOT NULL, "
                    + "PRIMARY KEY (player_uuid, kit_name), "
                    + "FOREIGN KEY (kit_name) REFERENCES kits(kit_name) ON DELETE CASCADE"
                    + ")";
            
            try (PreparedStatement stmt = conn.prepareStatement(createCooldownsTableQuery)) {
                stmt.executeUpdate();
                Bukkit.getLogger().info("Tabela 'player_kit_cooldowns' criada ou verificada com sucesso.");
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Erro ao criar tabelas: " + e.getMessage());
        }
    }
}