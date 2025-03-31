package com.hanielcota.kits.database.repository;

import com.hanielcota.kits.models.Kit;

import java.util.List;
import java.util.Optional;

public interface KitRepository {

    Optional<Kit> findByName(String name);
    void save(Kit kit);
    void delete(String name);
    List<Kit> findAll();
}