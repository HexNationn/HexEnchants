package org.hexnation.hexenchants;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.hexnation.hexenchants.event.BlockBreak;
import org.hexnation.hexenchants.event.TntUpgradeProcess;

public final class HexEnchants extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();

            saveResource("enchantments/enchantments.yml", true);
            saveResource("recipes/recipes.yml", true);
        }

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        getServer().getPluginManager().registerEvents(new TntUpgradeProcess(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
