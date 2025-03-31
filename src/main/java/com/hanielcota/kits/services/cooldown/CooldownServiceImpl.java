package com.hanielcota.kits.services.cooldown;

import com.hanielcota.kits.database.connection.DatabaseConnection;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@RequiredArgsConstructor
public class CooldownServiceImpl implements CooldownService {

    private final DatabaseConnection dbConnection;

    @Override
    public long getPlayerCooldown(UUID playerUuid, String kitName) {
        try (Connection conn = dbConnection.getConnection().orElseThrow(SQLException::new);
             PreparedStatement stmt = conn.prepareStatement("SELECT last_used FROM player_kit_cooldowns WHERE player_uuid = ? AND kit_name = ?")) {
            stmt.setString(1, playerUuid.toString());
            stmt.setString(2, kitName);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getLong("last_used") : 0;
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Erro ao verificar cooldown para '" + playerUuid + "': " + e.getMessage());
            return -1;
        }
    }

    @Override
    public void updatePlayerCooldown(UUID playerUuid, String kitName, long lastUsed) {
        try (Connection conn = dbConnection.getConnection().orElseThrow(SQLException::new);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO player_kit_cooldowns (player_uuid, kit_name, last_used) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE last_used = ?")) {
            stmt.setString(1, playerUuid.toString());
            stmt.setString(2, kitName);
            stmt.setLong(3, lastUsed);
            stmt.setLong(4, lastUsed);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Erro ao atualizar cooldown para '" + playerUuid + "': " + e.getMessage());
        }
    }
}