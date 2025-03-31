package com.hanielcota.kits.services.cooldown;

import java.util.UUID;

public interface CooldownService {

    long getPlayerCooldown(UUID playerUuid, String kitName);
    void updatePlayerCooldown(UUID playerUuid, String kitName, long lastUsed);
}