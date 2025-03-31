package com.hanielcota.kits.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hanielcota.kits.models.Kit;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public class KitCache {

    private final Cache<String, Kit> cache;

    public KitCache() {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(1000)
                .build();
    }

    public Kit getKit(String name) {
        return cache.getIfPresent(name);
    }

    public void putKit(String name, Kit kit) {
        cache.put(name, kit);
    }

    public void invalidateKit(String name) {
        cache.invalidate(name);
    }
}