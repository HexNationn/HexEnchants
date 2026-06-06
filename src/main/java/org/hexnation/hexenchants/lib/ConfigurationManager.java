package org.hexnation.hexenchants.lib;

import org.bukkit.configuration.file.YamlConfiguration;
import org.hexnation.hexenchants.HexEnchants;

import java.io.File;
import java.io.IOException;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class ConfigurationManager {
    private static final File ENCHANTMENTS_CONFIG           = new File(getPlugin(HexEnchants.class).getDataFolder(), "enchantments/enchantments.yml");
    private static final YamlConfiguration ENCHANTMENTS     = YamlConfiguration.loadConfiguration(ENCHANTMENTS_CONFIG);


    public static YamlConfiguration getEnchantmentsConfig() {
        return ENCHANTMENTS;
    }

    public static void saveEnchanementsConfig()  {
        try {
            ENCHANTMENTS.save(ENCHANTMENTS_CONFIG);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
