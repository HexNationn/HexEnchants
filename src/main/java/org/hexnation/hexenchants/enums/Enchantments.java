package org.hexnation.hexenchants.enums;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

public enum Enchantments {
    VEINMINE(RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(NamespacedKey.fromString("hexenchants:vein_mine"))),
    HAMMER(RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(NamespacedKey.fromString("hexenchants:hammer")));

    private Enchantment enchantment;

    Enchantments(Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    public Enchantment get() {
        return enchantment;
    }
}
