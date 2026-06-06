package org.hexnation.hexenchants.lib;

import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.hexnation.hexenchants.HexEnchants;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class RecipesManager {
    private static final File CUSTOM_RECIPES = new File(getPlugin(HexEnchants.class).getDataFolder(), "recipes/recipes.yml");
    private static YamlConfiguration RECIPES = YamlConfiguration.loadConfiguration(CUSTOM_RECIPES);

    public static YamlConfiguration getRecipesConfig() { return RECIPES; }


    static Set<String> getRecipeKeys() {
        MemorySection test = (MemorySection) getRecipesConfig().get("recipes.tnt_explosion");
        return test.getKeys(false);
    }


    public static Map<String, List<?>> getHammerRecipes() {
        RECIPES = YamlConfiguration.loadConfiguration(CUSTOM_RECIPES);

//        List<?> inputs = getRecipesConfig().getList("recipes.tnt_explosion");
//        List<?> output = getRecipesConfig().getList("recipes.tnt_explosion.hammer_3.output");

        Map<String, List<?>> recipes = new HashMap<>();

        Set<String> recipe_keys = getRecipeKeys();

        for (String key : recipe_keys) {
            List<?> input = getRecipesConfig().getList("recipes.tnt_explosion." + key + ".input");

            recipes.put(key, input);
        }
        return recipes;
    }

    public static ItemStack getReward(String recipe) {
        List<?> reward = getRecipesConfig().getList("recipes.tnt_explosion." + recipe + ".output");
        ItemStack item = (ItemStack) reward.getFirst();

        return item;
    }

    public static void saveCustomRecipes() {
        try {
            RECIPES.save(CUSTOM_RECIPES);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
