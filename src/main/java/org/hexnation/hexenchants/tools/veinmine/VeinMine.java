package org.hexnation.hexenchants.tools.veinmine;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.hexnation.hexenchants.blocks.BlockDetection;
import org.hexnation.hexenchants.lib.ConfigurationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class VeinMine {
    private static List<Tag<Material>> tool_tags = new ArrayList<>(List.of(
            Tag.ITEMS_AXES,
            Tag.ITEMS_HOES,
            Tag.ITEMS_PICKAXES,
            Tag.ITEMS_SHOVELS
    ));


    static Tag<Material> detect_tool_type (ItemStack find_from) {
        Tag<Material> shears_tag = Bukkit.getTag("items", NamespacedKey.fromString("hexenchants:shears"), Material.class);

        if (shears_tag.isTagged(find_from.getType())) {
            return shears_tag;
        }

        for (Tag<Material> current_tag : tool_tags) {
            if (current_tag.isTagged(find_from.getType())) { return current_tag; }
        }

        return null;
    }

    public static void triggerVeinMine(Player player, Block broken_block, ItemStack tool) {
        int max_veinmine_blocks = ConfigurationManager.getEnchantmentsConfig().getInt("vein_mine.max_block_breaks");
//        Tag<Material> vein_mineable_tag = Bukkit.getTag("items", new NamespacedKey(HexEnchants.getPlugin(HexEnchants.class).namespace(), "vein_mineable"), Material.class);

        // get tool type
        Tag<Material> tool_type = detect_tool_type(tool);

        List<String> block_tags_allowed = ConfigurationManager.veinmine_tool_tags(tool_type.getKey().asString());
        List<String> block_materials_allowed = ConfigurationManager.veinmine_material_tags(tool_type.getKey().asString());


        Tag<Material> tag_to_detect = null;
        Material material_to_detect = null;

        Block block_to_break = null;

        for (String tag_name : block_tags_allowed) {
            Tag<Material> current_tag = Bukkit.getTag("items", NamespacedKey.fromString(tag_name), Material.class);

            if (current_tag == null) {
                current_tag = Bukkit.getTag("blocks", NamespacedKey.fromString(tag_name), Material.class);
            }

            if (current_tag.isTagged(broken_block.getType())) {
//                tag_to_detect = current_tag;
                block_to_break = broken_block;
                break;
            }
        }

        for (String material_name : block_materials_allowed) {
            if (broken_block.getType().key().asString().toString().equals(material_name)) {
                block_to_break = broken_block;
            }
        }

        if (block_to_break == null) {
            return;
        }

        ArrayList<Block> detected_blocks = new ArrayList<>();
        detected_blocks.addAll(BlockDetection.startDetectingBlocks(broken_block, block_to_break, max_veinmine_blocks));

        for (Block detected_block : detected_blocks) {
            if (player.getGameMode() == GameMode.CREATIVE) {
                detected_block.setType(Material.AIR);
                continue;
            }

            if (tool.isEmpty()) {
                break;
            }

            detected_block.breakNaturally(tool);
            tool.damage(1, player);
        }
    }

    static void set_stripped_variant(Block unstripped) {
        String stripped_name = "STRIPPED_" + unstripped.getType().name();
        Orientable old_orientation = (Orientable) unstripped.getBlockData();

        unstripped.setType(Objects.requireNonNull(Material.getMaterial(stripped_name)));

        Orientable new_orientation = (Orientable) unstripped.getBlockData();
        new_orientation.setAxis(old_orientation.getAxis());

        unstripped.setBlockData(new_orientation);
    }

    //TODO ADD CONFIG FOR MAX NUMBER OF STRIPPED LOGS
    public static void veinAxeInteraction(Player player, ItemStack tool, Block interacted_block) {
        int max_strips = ConfigurationManager.getEnchantmentsConfig().getInt("vein_mine.max_strips");

        ArrayList<Block> detected_blocks = new ArrayList<>();
        detected_blocks.addAll(BlockDetection.startDetectingBlocks(interacted_block, interacted_block, max_strips));

        for (Block current_block : detected_blocks) {
            if (player.getGameMode() == GameMode.CREATIVE) {
                set_stripped_variant(current_block);
                continue;
            }

            set_stripped_variant(current_block);
            tool.damage(1, player);
        }
    }

}