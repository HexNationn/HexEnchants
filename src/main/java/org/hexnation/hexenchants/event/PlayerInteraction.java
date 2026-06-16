package org.hexnation.hexenchants.event;

import io.papermc.paper.entity.PlayerGiveResult;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.hexnation.hexenchants.HexEnchants;
import org.hexnation.hexenchants.enums.Enchantments;

public class PlayerInteraction implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = event.getItem();

        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) return;
        if (tool == null) return;
        if (!tool.getEnchantments().containsKey(Enchantments.VEINMINE.get())) return;
        if (!player.getCurrentInput().isSneak()) return;

        MiniMessage mm = MiniMessage.miniMessage();

        if (player.getCooldown(tool) > 0) {
            event.setCancelled(true);
            return;
        }

        NamespacedKey vein_toggled = new NamespacedKey(HexEnchants.getPlugin(HexEnchants.class), "vein_toggle");
        PersistentDataContainer pdc = player.getPersistentDataContainer();

        if (!pdc.has(vein_toggled)) {
            pdc.set(vein_toggled, PersistentDataType.BOOLEAN, false);
            return;
        }

        Boolean current_setting = pdc.getOrDefault(vein_toggled, PersistentDataType.BOOLEAN, null);
        pdc.set(vein_toggled, PersistentDataType.BOOLEAN, !current_setting);

        player.setCooldown(tool.getType(), 20);

        if (!current_setting) {
            player.sendActionBar(mm.deserialize("You have <green>ENABLED</green> <gradient:#00d6ff:#9f00ff>VeinMining</gradient>!"));
        } else {
            player.sendActionBar(mm.deserialize("You have <red>DISABLED</red> <gradient:#00d6ff:#9f00ff>VeinMining</gradient>!"));
        }
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack current_tool = player.getInventory().getItem(event.getNewSlot());

        if (current_tool == null) return;
        if (current_tool.getEnchantments().isEmpty()) return;
        if (!current_tool.getEnchantments().containsKey(Enchantments.VEINMINE.get())) return;

        PersistentDataContainer pdc = player.getPersistentDataContainer();
        Boolean veinmine_setting = pdc.getOrDefault(NamespacedKey.fromString("hexenchants:vein_toggle"), PersistentDataType.BOOLEAN, true);

        MiniMessage mm = MiniMessage.miniMessage();

        if (veinmine_setting) {
            player.sendActionBar(mm.deserialize("<gradient:#00d6ff:#9f00ff>VeinMine</gradient> Status: <green>ON</green>"));
        } else {
            player.sendActionBar(mm.deserialize("<gradient:#00d6ff:#9f00ff>VeinMine</gradient> Status: <red>OFF</red>"));
        }


    }
}
