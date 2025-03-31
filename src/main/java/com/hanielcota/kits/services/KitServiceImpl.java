package com.hanielcota.kits.services;

import com.hanielcota.kits.cache.KitCache;
import com.hanielcota.kits.database.repository.KitRepository;
import com.hanielcota.kits.models.Kit;
import com.hanielcota.kits.services.cooldown.CooldownService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class KitServiceImpl implements KitService {

    private final KitRepository kitRepository;
    private final CooldownService cooldownService;
    private final KitCache kitCache;

    @Override
    public Optional<Kit> getKit(String name) {
        Kit cachedKit = kitCache.getKit(name);
        if (cachedKit != null) return Optional.of(cachedKit);

        Optional<Kit> kit = kitRepository.findByName(name);
        kit.ifPresent(k -> kitCache.putKit(name, k));
        return kit;
    }

    @Override
    public void saveKit(Kit kit) {
        kitRepository.save(kit);
        kitCache.putKit(kit.getName(), kit);
    }

    @Override
    public void deleteKit(String name) {
        kitRepository.delete(name);
        kitCache.invalidateKit(name);
    }

    @Override
    public List<Kit> getAllKits() {
        return kitRepository.findAll();
    }

    @Override
    public long getPlayerCooldown(UUID playerUuid, String kitName) {
        return cooldownService.getPlayerCooldown(playerUuid, kitName);
    }

    @Override
    public void updatePlayerCooldown(UUID playerUuid, String kitName, long lastUsed) {
        cooldownService.updatePlayerCooldown(playerUuid, kitName, lastUsed);
    }
}