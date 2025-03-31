package com.hanielcota.kits.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.hanielcota.kits.models.Kit;
import com.hanielcota.kits.services.KitService;
import com.hanielcota.kits.utils.CooldownUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("kit")
@RequiredArgsConstructor
public class CreateKitCommand extends BaseCommand {

    private final KitService kitService;

    @Subcommand("create")
    @CommandPermission("kits.create")
    public void onCreate(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§c§lFLORUIT MC §f➤ §c✘ Uso: /kit create <kitName> <cooldown>");
            return;
        }

        String kitName = args[0];
        String cooldownInput = args[1];

        if (kitService.getKit(kitName).isPresent()) {
            player.sendMessage("§c§lFLORUIT MC §f➤ §c✘ Já existe um kit com o nome '" + kitName + "'.");
            return;
        }

        long cooldownMillis = CooldownUtil.parseCooldown(cooldownInput);
        if (cooldownMillis < 0) {
            player.sendMessage("§c§lFLORUIT MC §f➤ §c✘ Formato de cooldown inválido. Use: <numero><unidade> (ex.: 1d, 1h, 1m, 1s)");
            return;
        }

        List<ItemStack> items = new ArrayList<>();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) continue;
            items.add(item.clone());
        }

        if (items.isEmpty()) {
            player.sendMessage("§c§lFLORUIT MC §f➤ §c✘ Seu inventário está vazio. Adicione itens para criar o kit.");
            return;
        }

        Kit kit = new Kit();
        kit.setName(kitName);
        kit.setItems(items);
        kit.setCooldown(cooldownMillis);

        kitService.saveKit(kit);
        player.sendMessage("§a§lFLORUIT MC §f➤ §a✔ Kit '" + kitName + "' criado com cooldown de " + CooldownUtil.formatCooldown(cooldownMillis) + "!");
    }
}