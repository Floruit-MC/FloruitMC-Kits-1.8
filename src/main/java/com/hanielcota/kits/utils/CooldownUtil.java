package com.hanielcota.kits.utils;

import com.hanielcota.kits.models.Kit;
import com.hanielcota.kits.services.KitService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CooldownUtil {

    public static long parseCooldown(String input) {
        Pattern pattern = Pattern.compile("(\\d+)([dhms])");
        Matcher matcher = pattern.matcher(input.toLowerCase());
        if (!matcher.matches()) return -1;

        long value = Long.parseLong(matcher.group(1));
        String unit = matcher.group(2);

        long result;
        switch (unit) {
            case "d":
                result = value * 24 * 60 * 60 * 1000;
                break;
            case "h":
                result = value * 60 * 60 * 1000;
                break;
            case "m":
                result = value * 60 * 1000;
                break;
            case "s":
                result = value * 1000;
                break;
            default:
                result = -1;
        }
        return result;
    }


    public static String formatCooldown(long millis) {
        if (millis >= 24 * 60 * 60 * 1000) return (millis / (24 * 60 * 60 * 1000)) + "d";
        if (millis >= 60 * 60 * 1000) return (millis / (60 * 60 * 1000)) + "h";
        if (millis >= 60 * 1000) return (millis / (60 * 1000)) + "m";
        return (millis / 1000) + "s";
    }

    public static boolean isOnCooldown(Player player, Kit kit, KitService kitService) {
        long lastUsed = kitService.getPlayerCooldown(player.getUniqueId(), kit.getName());
        long currentTime = System.currentTimeMillis();

        return currentTime < (lastUsed + kit.getCooldown());
    }



}