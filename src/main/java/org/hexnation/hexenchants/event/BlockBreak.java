package org.hexnation.hexenchants.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.hexnation.hexenchants.enums.Enchantments;

import static org.hexnation.hexenchants.tools.Hammer.triggerHammer;
import static org.hexnation.hexenchants.tools.VeinMine.triggerVeinMine;

public class BlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block broken_block = event.getBlock();
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (!tool.getEnchantments().isEmpty())
        {
            if (tool.getEnchantments().containsKey(Enchantments.VEINMINE.get())) {
                triggerVeinMine(player, broken_block, tool);
            }

            if (tool.getEnchantments().containsKey(Enchantments.HAMMER.get())) {
                triggerHammer(player, broken_block, tool);
            }
        }
    }
}
