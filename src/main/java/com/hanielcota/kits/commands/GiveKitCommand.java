package com.hanielcota.kits.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.hanielcota.kits.models.Kit;
import com.hanielcota.kits.services.KitService;
import com.hanielcota.kits.utils.CooldownUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@CommandAlias("kit")
@RequiredArgsConstructor
public class GiveKitCommand extends BaseCommand {

    private final KitService kitService;

    @Subcommand("give")
    @CommandPermission("kits.give")
    public void onGive(Player sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§c§lFLORUIT MC §f➤ §c✘ Uso: /kit give <kitName> <player>");
            return;
        }

        String kitName = args[0];
        String targetPlayerName = args[1];

        var kitOptional = kitService.getKit(kitName);
        if (kitOptional.isEmpty()) {
            sender.sendMessage("§c§lFLORUIT MC §f➤ §c✘ Kit '" + kitName + "' não encontrado.");
            return;
        }

        Player target = Bukkit.getPlayerExact(targetPlayerName);
        if (target == null) {
            sender.sendMessage("§c§lFLORUIT MC §f➤ §c✘ Jogador '" + targetPlayerName + "' não está online.");
            return;
        }

        Kit kit = kitOptional.get();
        if (CooldownUtil.isOnCooldown(target, kit, kitService)) {
            long lastUsed = kitService.getPlayerCooldown(target.getUniqueId(), kit.getName());
            long remainingMillis = (lastUsed + kit.getCooldown()) - System.currentTimeMillis();
            sender.sendMessage("§c§lFLORUIT MC §f➤ §c✘ O jogador '" + target.getName() + "' deve esperar " + CooldownUtil.formatCooldown(remainingMillis) + " para usar o kit '" + kitName + "'.");
            return;
        }

        Map<Integer, ItemStack> leftovers = target.getInventory().addItem(kit.getItems().toArray(new ItemStack[0]));
        if (!leftovers.isEmpty()) {
            sender.sendMessage("§c§lFLORUIT MC §f➤ §c✘ O inventário de '" + target.getName() + "' está cheio. Alguns itens não foram adicionados.");
            return;
        }

        kitService.updatePlayerCooldown(target.getUniqueId(), kitName, System.currentTimeMillis());
        target.sendMessage("§a§lFLORUIT MC §f➤ §a✔ Você recebeu o kit " + kit.getName() + "!");
        sender.sendMessage("§a§lFLORUIT MC §f➤ §a✔ Kit '" + kit.getName() + "' dado ao jogador '" + target.getName() + "' com sucesso!");
    }
}