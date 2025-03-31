package com.hanielcota.kits;

import co.aikar.commands.PaperCommandManager;
import com.hanielcota.kits.cache.KitCache;
import com.hanielcota.kits.commands.*;
import com.hanielcota.kits.database.config.DatabaseConfig;
import com.hanielcota.kits.database.connection.DatabaseConnection;
import com.hanielcota.kits.database.initialization.KitTableInitializer;
import com.hanielcota.kits.database.repository.KitRepository;
import com.hanielcota.kits.database.repository.KitRepositoryImpl;
import com.hanielcota.kits.services.KitService;
import com.hanielcota.kits.services.KitServiceImpl;
import com.hanielcota.kits.services.cooldown.CooldownService;
import com.hanielcota.kits.services.cooldown.CooldownServiceImpl;
import com.hanielcota.kits.utils.ConfigUtil;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

@Getter
public final class KitsPlugin extends JavaPlugin {

    private DatabaseConnection databaseConnection;
    private KitCache kitCache;
    private KitService kitService;
    private ConfigUtil configUtil;

    @Override
    public void onEnable() {
        setupConfig();

        if (!setupDatabase()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        setupCache();
        setupServices();
        setupCommands();

        getLogger().info("KitsPlugin habilitado com sucesso!");
    }

    @Override
    public void onDisable() {
        if (databaseConnection != null) {
            databaseConnection.close();
        }

        getLogger().info("KitsPlugin desativado.");
    }

    private void setupConfig() {
        configUtil = new ConfigUtil(this, "config.yml");
        configUtil.createDefaultConfig(this, "config.yml");
    }

    private boolean setupDatabase() {
        try {
            databaseConnection = new DatabaseConfig(configUtil).createConnection();
            new KitTableInitializer(databaseConnection).initialize();
            return true;
        } catch (SQLException e) {
            getLogger().severe("Erro ao conectar ao banco de dados: " + e.getMessage());
            return false;
        }
    }

    private void setupCache() {
        kitCache = new KitCache();
    }

    private void setupServices() {
        KitRepository kitRepository = new KitRepositoryImpl(databaseConnection);
        CooldownService cooldownService = new CooldownServiceImpl(databaseConnection);
        kitService = new KitServiceImpl(kitRepository, cooldownService, kitCache);
    }

    private void setupCommands() {
        PaperCommandManager commandManager = new PaperCommandManager(this);

        commandManager.registerCommand(new CreateKitCommand(kitService));
        commandManager.registerCommand(new GetKitCommand(kitService));
        commandManager.registerCommand(new GiveKitCommand(kitService));
        commandManager.registerCommand(new ViewKitCommand(kitService));
        commandManager.registerCommand(new DeleteKitCommand(kitService));
    }
}
