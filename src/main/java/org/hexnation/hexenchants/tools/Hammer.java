package org.hexnation.hexenchants.tools;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.hexnation.hexenchants.enums.Enchantments;
import org.hexnation.hexenchants.lib.ConfigurationManager;

import java.util.ArrayList;
import java.util.LinkedList;

public class Hammer {
    static LinkedList<Block> calculateBlocks(int hammer_size, Block broken_block, Player player) {
        LinkedList<Block> detected_blocks = new LinkedList<>();
        int number_of_detections = (hammer_size - 1) / 2;

        RayTraceResult raytrace = player.rayTraceBlocks(6);

        // Special Check For Up / Down
        if (raytrace.getHitBlockFace() == BlockFace.DOWN || raytrace.getHitBlockFace() == BlockFace.UP) {
            LinkedList<Block> temp_list = new LinkedList<>();

            for (int z = -number_of_detections; z < hammer_size - number_of_detections; z++) {
                for (int x = -number_of_detections; x < hammer_size - number_of_detections; x++) {
                    temp_list.add(broken_block.getRelative(x, 0, z));
                }
            }

            detected_blocks.addAll(temp_list);
            return detected_blocks;
        }

        for (int y = -1; y < hammer_size - 1; y++) {
            LinkedList<Block> temp_list = new LinkedList<>();

            if (raytrace.getHitBlockFace() == BlockFace.EAST || raytrace.getHitBlockFace() == BlockFace.WEST) {
                for (int z = -number_of_detections; z < hammer_size - number_of_detections; z++) {
                    temp_list.add(broken_block.getRelative(0, y, z));
                }
            }

            if (raytrace.getHitBlockFace() == BlockFace.NORTH || raytrace.getHitBlockFace() == BlockFace.SOUTH) {
                for (int x = -number_of_detections; x < hammer_size - number_of_detections; x++) {
                    temp_list.add(broken_block.getRelative(x, y, 0));
                }
            }

            detected_blocks.addAll(temp_list);
        }

        return detected_blocks;
    }

    public static void triggerHammer(Player player, Block broken_block, ItemStack tool) {
        Enchantment hammer_enchantment  = Enchantments.HAMMER.get();
        ArrayList<Integer> hammer_sizes = new ArrayList<>(ConfigurationManager.getEnchantmentsConfig().getIntegerList("hammer.sizes"));

        int enchantment_level = tool.getEnchantmentLevel(hammer_enchantment);
        int hammer_size = hammer_sizes.get(enchantment_level - 1);

        LinkedList<Block> detected_blocks = calculateBlocks(hammer_size, broken_block, player);

        if (player.getGameMode() == GameMode.CREATIVE) {
            for (Block block : detected_blocks) {
                block.setType(Material.AIR);
            }
        } else {
            for (Block block: detected_blocks) {
                block.breakNaturally();
                player.damageItemStack(tool, 1);
            }
        }

    }
}
