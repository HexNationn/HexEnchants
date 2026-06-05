package org.hexnation.hexenchants.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.hexnation.hexenchants.lib.RecipesManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TntUpgradeProcess implements Listener {

    @EventHandler
    public void preventItemDestruction(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity victim  = event.getEntity();

        if (damager.getType() == EntityType.TNT && victim.getType() == EntityType.ITEM) {
            event.setCancelled(true);
        }
    }

    static String isRecipeValid(List<ItemStack> items) {
        Map<String, List<?>> recipes = RecipesManager.getHammerRecipes();

        String reward_key = "";

        for (String key : recipes.keySet()) {
            if (items.containsAll(recipes.get(key))) {
                reward_key = key;
            }
        }

        return reward_key;
    }

    @EventHandler
    public void tntExplosion(EntityExplodeEvent event) {
        Entity exploder = event.getEntity();
        if (exploder.getType() == EntityType.TNT) {
            List<Entity> exploded_entities = exploder.getNearbyEntities(2,2,2);

            List<ItemStack> items = new ArrayList<>();

            for (Entity entity : exploded_entities) {
                if (!(entity.getType() == EntityType.ITEM)) {
                    continue;
                }
                Item curr_item = (Item) entity;
                items.add(curr_item.getItemStack());
            }

            String reward_key = isRecipeValid(items);

            items.forEach((item) -> item.setAmount(0));
            if (!reward_key.isEmpty()) {
                ItemStack reward = RecipesManager.getReward(reward_key);
                exploder.getWorld().dropItem(exploder.getLocation(), reward);
            }
        }
    }
}
