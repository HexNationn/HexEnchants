package org.hexnation.hexenchants;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.hexnation.hexenchants.event.VeinHammerInteractions;
import org.hexnation.hexenchants.event.PlayerInteraction;
import org.hexnation.hexenchants.event.TntUpgradeProcess;
import org.hexnation.hexenchants.lib.ConfigurationManager;

public final class HexEnchants extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();

            saveResource("enchantments/enchantments.yml", false);
            saveResource("recipes/recipes.yml", false);
            saveResource("enchantments/veinmine_settings/allowed_blocks.yml", false);
        }

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new VeinHammerInteractions(), this);
        getServer().getPluginManager().registerEvents(new TntUpgradeProcess(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteraction(), this);


        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            LiteralCommandNode<CommandSourceStack> command = Commands.literal("reload")
                    .then(Commands.literal("reload"))
                    .executes(ctx -> {
                        ConfigurationManager.reloadConfigs();
                        ctx.getSource().getSender().sendMessage("Config has been reloaded!");
                        return Command.SINGLE_SUCCESS;
                    })
                    .build();

            commands.registrar().register(command);
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
