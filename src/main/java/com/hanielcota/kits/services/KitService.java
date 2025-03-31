package com.hanielcota.kits.services;

import com.hanielcota.kits.models.Kit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KitService {

    Optional<Kit> getKit(String name);
    void saveKit(Kit kit);
    void deleteKit(String name);
    List<Kit> getAllKits();
    long getPlayerCooldown(UUID playerUuid, String kitName);
    void updatePlayerCooldown(UUID playerUuid, String kitName, long lastUsed);
}