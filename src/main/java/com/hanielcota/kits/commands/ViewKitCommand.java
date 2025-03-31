package com.hanielcota.kits.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import com.hanielcota.kits.models.Kit;
import com.hanielcota.kits.services.KitService;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@CommandAlias("kit")
@RequiredArgsConstructor
public class ViewKitCommand extends BaseCommand {

    private final KitService kitService;

    @Subcommand("view")
    public void onView(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§c§lFLORUIT MC §f➤ §c✘ Uso: /kit view <kitName>");
            return;
        }

        String kitName = args[0];
        var kitOptional = kitService.getKit(kitName);
        if (kitOptional.isEmpty()) {
            player.sendMessage("§c§lFLORUIT MC §f➤ §c✘ Kit '" + kitName + "' não encontrado.");
            return;
        }

        Kit kit = kitOptional.get();
        player.sendMessage("§e§lFLORUIT MC §f➤ §e✔ Itens do kit " + kit.getName() + ":");
        kit.getItems().forEach(item -> player.sendMessage("§7- " + item.getType().toString()));
    }
}