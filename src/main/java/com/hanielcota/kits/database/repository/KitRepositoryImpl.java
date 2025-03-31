package com.hanielcota.kits.database.repository;

import com.hanielcota.kits.database.connection.DatabaseConnection;
import com.hanielcota.kits.models.Kit;
import com.hanielcota.kits.serializer.ItemSerializer;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class KitRepositoryImpl implements KitRepository {

    private final DatabaseConnection dbConnection;

    @Override
    public Optional<Kit> findByName(String name) {
        try (Connection conn = dbConnection.getConnection().orElseThrow(SQLException::new);
             PreparedStatement stmt = conn.prepareStatement("SELECT kit_name, cooldown, items FROM kits WHERE kit_name = ?")) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) return Optional.empty();

            Kit kit = new Kit();
            kit.setName(rs.getString("kit_name"));
            kit.setCooldown(rs.getLong("cooldown"));
            kit.setItems(ItemSerializer.deserializeItems(rs.getString("items")));
            return Optional.of(kit);
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Erro ao buscar kit '" + name + "': " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void save(Kit kit) {
        try (Connection conn = dbConnection.getConnection().orElseThrow(SQLException::new);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO kits (kit_name, cooldown, items) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE cooldown = ?, items = ?")) {
            String serializedItems = ItemSerializer.serializeItems(kit.getItems());
            stmt.setString(1, kit.getName());
            stmt.setLong(2, kit.getCooldown());
            stmt.setString(3, serializedItems);
            stmt.setLong(4, kit.getCooldown());
            stmt.setString(5, serializedItems);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Erro ao salvar kit '" + kit.getName() + "': " + e.getMessage());
        }
    }

    @Override
    public void delete(String name) {
        try (Connection conn = dbConnection.getConnection().orElseThrow(SQLException::new);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM kits WHERE kit_name = ?")) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Erro ao deletar kit '" + name + "': " + e.getMessage());
        }
    }

    @Override
    public List<Kit> findAll() {
        List<Kit> kits = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection().orElseThrow(SQLException::new);
             PreparedStatement stmt = conn.prepareStatement("SELECT kit_name, cooldown, items FROM kits");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Kit kit = new Kit();
                kit.setName(rs.getString("kit_name"));
                kit.setCooldown(rs.getLong("cooldown"));
                kit.setItems(ItemSerializer.deserializeItems(rs.getString("items")));
                kits.add(kit);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Erro ao buscar todos os kits: " + e.getMessage());
        }
        return kits;
    }
}