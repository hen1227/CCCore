package com.henhen1227.cccore.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ItemGeneration {

    public static ItemStack createUnbreakableItem(Material material) {
        ItemStack item = new ItemStack(material);
        item.getItemMeta().addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.getItemMeta().setUnbreakable(true);

        return item;
    }
}
