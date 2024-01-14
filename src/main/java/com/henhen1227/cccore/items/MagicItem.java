package com.henhen1227.cccore.items;

import com.henhen1227.cccore.CCCore;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public abstract class MagicItem {
    protected final NamespacedKey key;
    protected final String unique_id;

    protected final int price;

    public MagicItem(String key, int price) {
        this.key = new NamespacedKey(CCCore.instance, "unique_id");
        this.unique_id = key;
        this.price = price;
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