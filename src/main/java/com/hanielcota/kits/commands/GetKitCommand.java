package com.hanielcota.kits.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.hanielcota.kits.models.Kit;
import com.hanielcota.kits.services.KitService;
import com.hanielcota.kits.utils.CooldownUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;

@CommandAlias("kit")
@RequiredArgsConstructor
public class GetKitCommand extends BaseCommand {

    private final KitService kitService;

    @Default
    public void onDefault(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§c§lFLORUIT MC §f➤ §c✘ Uso: /kit <kitName>");
            return;
        }

        String kitName = args[0];
        Optional<Kit> kitOptional = kitService.getKit(kitName);
        if (kitOptional.isEmpty()) {
            player.sendMessage("§c§lFLORUIT MC §f➤ §c✘ Kit '" + kitName + "' não encontrado.");
            return;
        }

        Kit kit = kitOptional.get();
        if (CooldownUtil.isOnCooldown(player, kit, kitService)) {
            long lastUsed = kitService.getPlayerCooldown(player.getUniqueId(), kit.getName());
            long remainingMillis = (lastUsed + kit.getCooldown()) - System.currentTimeMillis();
            player.sendMessage("§c§lFLORUIT MC §f➤ §c✘ Você deve esperar " + CooldownUtil.formatCooldown(remainingMillis) + " para usar o kit '" + kitName + "'.");
            return;
        }

        Map<Integer, ItemStack> leftovers = player.getInventory().addItem(kit.getItems().toArray(new ItemStack[0]));
        if (!leftovers.isEmpty()) {
            player.sendMessage("§c§lFLORUIT MC §f➤ §c✘ Seu inventário está cheio. Alguns itens não foram adicionados.");
            return;
        }

        kitService.updatePlayerCooldown(player.getUniqueId(), kitName, System.currentTimeMillis());
        player.sendMessage("§a§lFLORUIT MC §f➤ §a✔ Você recebeu o kit " + kit.getName() + "!");
    }
}