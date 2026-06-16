package org.hexnation.hexenchants.lib;

import org.bukkit.configuration.file.YamlConfiguration;
import org.hexnation.hexenchants.HexEnchants;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class ConfigurationManager {
    private static File ENCHANTMENTS_CONFIG               = new File(getPlugin(HexEnchants.class).getDataFolder(), "enchantments/enchantments.yml");
    private static File VEINMINE_ALLOW_LIST               = new File(getPlugin(HexEnchants.class).getDataFolder(), "enchantments/veinmine_settings/allowed_blocks.yml");

    private static YamlConfiguration ENCHANTMENTS                 = YamlConfiguration.loadConfiguration(ENCHANTMENTS_CONFIG);
    private static YamlConfiguration VEINMINE_ALLOWED_BLOCKS      = YamlConfiguration.loadConfiguration(VEINMINE_ALLOW_LIST);

    public static YamlConfiguration getEnchantmentsConfig() {
        return ENCHANTMENTS;
    }
    public static YamlConfiguration get_veinmine_allowed() { return VEINMINE_ALLOWED_BLOCKS; }

    public static List<String> veinmine_tool_tags(String tool_tag) {
        return get_veinmine_allowed().getStringList(tool_tag+".tags");
    }

    public static List <String> veinmine_material_tags(String tool_tag) {
        return get_veinmine_allowed().getStringList(tool_tag+".materials");
    }

    public static void reloadConfigs() {
        ENCHANTMENTS_CONFIG = new File(getPlugin(HexEnchants.class).getDataFolder(), "enchantments/enchantments.yml");
        VEINMINE_ALLOW_LIST = new File(getPlugin(HexEnchants.class).getDataFolder(), "enchantments/veinmine_settings/allowed_blocks.yml");

        ENCHANTMENTS            = YamlConfiguration.loadConfiguration(ENCHANTMENTS_CONFIG);
        VEINMINE_ALLOWED_BLOCKS = YamlConfiguration.loadConfiguration(VEINMINE_ALLOW_LIST);
    }
}
