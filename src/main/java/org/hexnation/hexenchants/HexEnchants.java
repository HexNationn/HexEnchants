package org.hexnation.hexenchants;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.hexnation.hexenchants.event.BlockBreak;
import org.hexnation.hexenchants.event.TntUpgradeProcess;

public final class HexEnchants extends JavaPlugin implements Listener {
//    @EventHandler
//    public void itemInteract(PlayerInteractEvent event) {
//        Player player = event.getPlayer();
//        if (event.getHand() == EquipmentSlot.HAND) {
//            if (player.getInventory().getItemInMainHand().equals(ItemStack.of(Material.STICK))) {
////                PersistentDataContainer chunk = player.getChunk().getPersistentDataContainer();
//
//
//            }
//        }
//    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        getServer().getPluginManager().registerEvents(new TntUpgradeProcess(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
