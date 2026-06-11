package org.hexnation.hexenchants.tools;

import com.destroystokyo.paper.MaterialTags;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
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
import java.util.List;

public class Hammer {

    static Tag<Material> getToolType(Block current_block) {
        Tag<Material> tool_to_use = null;

        if (Tag.MINEABLE_PICKAXE.isTagged(current_block.getType())) {
            tool_to_use = Tag.ITEMS_PICKAXES;
        }

        if (Tag.MINEABLE_SHOVEL.isTagged(current_block.getType())) {
            tool_to_use = Tag.ITEMS_SHOVELS;
        }

        if (Tag.MINEABLE_AXE.isTagged(current_block.getType())) {
            tool_to_use = Tag.ITEMS_AXES;
        }

        if (Tag.MINEABLE_HOE.isTagged(current_block.getType())) {
            tool_to_use = Tag.ITEMS_HOES;
        }

        return tool_to_use;
    }
    static LinkedList<Block> addBlockToList(LinkedList<Block> currentList, ItemStack tool_used, Block current_block) {
        Tag<Material> tool_type_preferred = getToolType(current_block);

        if (tool_type_preferred == null) {
            return null;
        }

        if (tool_type_preferred.isTagged(tool_used.getType())) {
            currentList.add(current_block);
        }

        return null;
    }

    static LinkedList<Block> calculateBlocks(int hammer_size, Block broken_block, Player player) {
        LinkedList<Block> detected_blocks = new LinkedList<>();
        int number_of_detections = (hammer_size - 1) / 2;

        RayTraceResult raytrace = player.rayTraceBlocks(6);

        ItemStack tool_used = player.getInventory().getItemInMainHand();

        // Check For Up / Down
        if (raytrace.getHitBlockFace() == BlockFace.DOWN || raytrace.getHitBlockFace() == BlockFace.UP) {
            LinkedList<Block> temp_list = new LinkedList<>();

            for (int z = -number_of_detections; z < hammer_size - number_of_detections; z++) {
                for (int x = -number_of_detections; x < hammer_size - number_of_detections; x++) {
                    Block current_block = broken_block.getRelative(x, 0, z);

//                    Tag<Material> tool_type_preferred = getToolType(current_block);
//
//                    if (tool_type_preferred.isTagged(tool_used.getType())) {
//                        temp_list.add(current_block);
//                    }
                    addBlockToList(temp_list, tool_used, current_block);
                }
            }

            detected_blocks.addAll(temp_list);
            return detected_blocks;
        }


        for (int y = -1; y < hammer_size - 1; y++) {
            LinkedList<Block> temp_list = new LinkedList<>();

            // Check for east and west facing
            if (raytrace.getHitBlockFace() == BlockFace.EAST || raytrace.getHitBlockFace() == BlockFace.WEST) {
                for (int z = -number_of_detections; z < hammer_size - number_of_detections; z++) {
                    Block current_block = broken_block.getRelative(0, y, z);

                    addBlockToList(temp_list, tool_used, current_block);
                }
            }

            // Check for north and south facing
            if (raytrace.getHitBlockFace() == BlockFace.NORTH || raytrace.getHitBlockFace() == BlockFace.SOUTH) {
                for (int x = -number_of_detections; x < hammer_size - number_of_detections; x++) {
                    Block current_block = broken_block.getRelative(x, y, 0);

                    addBlockToList(temp_list, tool_used, current_block);
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
                block.breakNaturally(tool);
                player.damageItemStack(tool, 1);
            }
        }

    }
}
