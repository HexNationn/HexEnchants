package org.hexnation.hexenchants.event;

import io.papermc.paper.entity.Shearable;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.hexnation.hexenchants.blocks.BlockDetection;
import org.hexnation.hexenchants.enums.Enchantments;
import org.hexnation.hexenchants.lib.ConfigurationManager;
import org.hexnation.hexenchants.tools.veinmine.VeinMine;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;

import static org.hexnation.hexenchants.tools.Hammer.triggerHammer;
import static org.hexnation.hexenchants.tools.veinmine.VeinMine.triggerVeinMine;

public class VeinHammerInteractions implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block broken_block = event.getBlock();
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (!tool.getEnchantments().isEmpty())
        {
            if (tool.getEnchantments().containsKey(Enchantments.VEINMINE.get())) {
                Boolean vein_mining_status = player.getPersistentDataContainer().getOrDefault(NamespacedKey.fromString("hexenchants:vein_toggle"), PersistentDataType.BOOLEAN, null);

                if (vein_mining_status) {
                    triggerVeinMine(player, broken_block, tool);
                }
            }

            if (tool.getEnchantments().containsKey(Enchantments.HAMMER.get())) {
                triggerHammer(player, broken_block, tool);
            }
        }
    }

    @EventHandler
    public void onStripEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (item == null || !Tag.ITEMS_AXES.isTagged(item.getType())) return;
        if (!Tag.LOGS.isTagged(event.getClickedBlock().getType())) return;
        if (event.getClickedBlock().getType().toString().startsWith("STRIPPED_")) return;

        VeinMine.veinAxeInteraction(player, item, event.getClickedBlock());
    }

    @EventHandler
    public void onHarvest(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItem(EquipmentSlot.HAND);


        if (event.getHand() != EquipmentSlot.OFF_HAND) return;
        if (!Tag.ITEMS_HOES.isTagged(tool.getType())) return;
        if (tool.getEnchantments().isEmpty()) return;
        if (!tool.getEnchantments().containsKey(Enchantments.VEINMINE.get())) return;
        Block block = event.getClickedBlock();

        int maxharvests = ConfigurationManager.getEnchantmentsConfig().getInt("vein_mine.max_harvests");

        if (Tag.CROPS.isTagged(block.getType())) {
            ArrayList<Block> all_crops = new ArrayList<>();
            all_crops.addAll(BlockDetection.startDetectingBlocks(block, block, maxharvests));

            event.setCancelled(true);

            for (Block crop : all_crops) {
                ItemStack offhand = player.getInventory().getItem(EquipmentSlot.OFF_HAND);
                Ageable crop_age = (Ageable) crop.getBlockData();
                Material crop_type = crop.getType();

                if (crop_age.getMaximumAge() == crop_age.getAge()) {
                    String crop_name = crop_type.name();
                    String seed_name = getSeedName(crop_type);

                    if (offhand.getType() == Material.getMaterial(seed_name)) {
                        crop.breakNaturally(tool);
                        crop.setType(Material.getMaterial(crop_name));

                        if (player.getGameMode() == GameMode.SURVIVAL) {
                            player.getInventory().getItem(EquipmentSlot.HAND).damage(1, player);
                            offhand.setAmount(offhand.getAmount() - 1);
                        }
                    }
                }
            }
        }
    }

    private static @Nullable String getSeedName(Material crop_type) {
        String seed_name = null;

        if (crop_type == Material.WHEAT) {
            seed_name = Material.WHEAT_SEEDS.name();
        }

        if (crop_type == Material.BEETROOTS) {
            seed_name = Material.BEETROOT_SEEDS.name();
        }

        if (crop_type == Material.CARROTS) {
            seed_name = Material.CARROT.name();
        }

        if (crop_type == Material.POTATOES) {
            seed_name = Material.POTATO.name();
        }
        return seed_name;
    }

    @EventHandler
    public void onShear(PlayerShearEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = event.getItem();
        World current_world = player.getWorld();
        Entity sheared = event.getEntity();

        PersistentDataContainer pdc = player.getPersistentDataContainer();
        boolean is_vein_enabled = pdc.getOrDefault(NamespacedKey.fromString("hexenchants:vein_toggle"), PersistentDataType.BOOLEAN, true);

        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (tool.getEnchantments().isEmpty()) return;
        if (!tool.getEnchantments().containsKey(Enchantments.VEINMINE.get())) return;
        if (!is_vein_enabled) return;

        ArrayList<Entity> entities_near = new ArrayList<>();
        entities_near.addAll(sheared.getNearbyEntities(5,1,5));

        for (Entity ent : entities_near) {
            if (ent.getType() == EntityType.SHEEP || ent.getType() == EntityType.MOOSHROOM) {
                Shearable shearable = (Shearable) ent;
                if (shearable.readyToBeSheared()) {
                    shearable.shear();

                    if (player.getGameMode() == GameMode.CREATIVE) continue;

                    player.getInventory().getItem(EquipmentSlot.HAND).damage(1, player);
                }
            }
        }
    }
}
