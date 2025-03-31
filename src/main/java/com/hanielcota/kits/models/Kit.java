package com.hanielcota.kits.models;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
public class Kit {

    private String name;
    private List<ItemStack> items;
    private long cooldown;

}