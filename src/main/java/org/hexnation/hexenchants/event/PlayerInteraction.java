package org.hexnation.hexenchants.event;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
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
        if (!player.getCurrentInput().isSneak()) return;
        if (tool == null) return;
        if (tool.getEnchantments().isEmpty()) return;
        if (!tool.getEnchantments().containsKey(Enchantments.VEINMINE.get())) return;

        NamespacedKey vein_toggled = new NamespacedKey(HexEnchants.getPlugin(HexEnchants.class), "vein_toggle");
        PersistentDataContainer pdc = player.getPersistentDataContainer();


        if (!pdc.has(vein_toggled)) {
            pdc.set(vein_toggled, PersistentDataType.BOOLEAN, false);
            return;
        }

        Boolean current_setting = pdc.getOrDefault(vein_toggled, PersistentDataType.BOOLEAN, null);
        pdc.set(vein_toggled, PersistentDataType.BOOLEAN, !current_setting);

        player.sendMessage("You have set vein mining to " + !current_setting);
    }
}
