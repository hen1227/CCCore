package com.henhen1227.cccore.events.shop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SwordShop extends ItemShop {
    public SwordShop() {
        populateItems();
    }

    @Override
    public void populateItems() {
        items.add(new ShopItem(new ItemStack(Material.WOODEN_SWORD), "Wooden Sword", 10));
        items.add(new ShopItem(new ItemStack(Material.STONE_SWORD), "Stone Sword", 20));
        items.add(new ShopItem(new ItemStack(Material.IRON_SWORD), "Iron Sword", 40));
        items.add(new ShopItem(new ItemStack(Material.DIAMOND_SWORD), "Diamond Sword", 80));
    }

    @Override
    protected int getCurrentTier(Player player) {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (player.getInventory().contains(items.get(i).getItem().getType())) {
                return i;
            }
        }
        return 0;
    }
}