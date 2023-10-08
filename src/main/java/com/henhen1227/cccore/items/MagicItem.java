package com.henhen1227.cccore.items;

import com.henhen1227.cccore.CCCore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;

public abstract class MagicItem {
    protected final NamespacedKey key;
    protected final String unique_id;

    public MagicItem(String key) {
        this.key = new NamespacedKey(CCCore.instance, "unique_id");
        this.unique_id = key;
    }

    public boolean isMatchingItem(ItemStack item){
        if(item == null || item.getItemMeta() == null) return false;
        return unique_id.equals(item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING));
    }

    public void morningUpdate(Player player, ItemStack item) {
        // Default implementation (does nothing)
    }

    public ItemStack item() {
        return MagicItemManager.defaultItem();
    }
}