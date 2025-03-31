package com.hanielcota.kits.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.hanielcota.kits.services.KitService;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@CommandAlias("kit")
@RequiredArgsConstructor
public class DeleteKitCommand extends BaseCommand {

    private final KitService kitService;

    @Subcommand("delete")
    @CommandPermission("kits.delete")
    public void onDelete(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§c§lFLORUIT MC §f➤ §c✘ Uso: /kit delete <kitName>");
            return;
        }

        String kitName = args[0];
        if (kitService.getKit(kitName).isEmpty()) {
            player.sendMessage("§c§lFLORUIT MC §f➤ §c✘ Kit '" + kitName + "' não encontrado.");
            return;
        }

        kitService.deleteKit(kitName);
        player.sendMessage("§a§lFLORUIT MC §f➤ §a✔ Kit '" + kitName + "' deletado com sucesso!");
    }
}