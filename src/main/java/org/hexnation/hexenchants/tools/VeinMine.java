package org.hexnation.hexenchants.tools;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.hexnation.hexenchants.blocks.NeighboringBlocks;
import org.hexnation.hexenchants.lib.ConfigurationManager;

import java.util.ArrayList;


public class VeinMine {
    public static void triggerVeinMine(Player player, Block broken_block, ItemStack tool) {
        int max_veinmine_blocks = ConfigurationManager.getEnchantmentsConfig().getInt("vein_mine.max_block_break");
        Tag<Material> mineable_ores_tag = Bukkit.getTag("items", new NamespacedKey("hexnationenchantments", "mineable_ores"), Material.class);

        // DETERMINE IF TOOL IS AXE
        if (Tag.ITEMS_AXES.isTagged(tool.getType())) {
            ArrayList<Block> detected_blocks = new ArrayList<>(NeighboringBlocks.startDetectingBlocks(broken_block, Tag.LOGS, max_veinmine_blocks));
            ItemStack player_tool = player.getInventory().getItemInMainHand();

            for (Block detected_block : detected_blocks) {
                if (player.getGameMode() == GameMode.CREATIVE) {
                    detected_block.setType(Material.AIR);
                    continue;
                }

                if (player_tool.isEmpty()) {
                    break;
                }

                detected_block.breakNaturally();
                player_tool.damage(1, player);
            }
        }

        if (Tag.ITEMS_PICKAXES.isTagged(tool.getType())) {
            ArrayList<Block> detected_blocks = new ArrayList<>(NeighboringBlocks.startDetectingBlocks(broken_block, mineable_ores_tag, max_veinmine_blocks));
            ItemStack player_tool = player.getInventory().getItemInMainHand();

            for (Block detected_block : detected_blocks) {
                if (player.getGameMode() == GameMode.CREATIVE) {
                    detected_block.setType(Material.AIR);
                    continue;
                }

                if (player_tool.isEmpty()) {
                    break;
                }

                detected_block.breakNaturally();
                player_tool.damage(1, player);
            }
        }

    }
}

