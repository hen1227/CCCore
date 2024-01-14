package com.henhen1227.cccore.events.shop;

import org.bukkit.inventory.ItemStack;

class ShopItem {
    private final ItemStack item;
    private final String name;
    private final int cost;

    public ShopItem(ItemStack item, String name, int cost) {
        this.item = item;
        this.name = name;
        this.cost = cost;
    }

    public ItemStack getItem() { return item; }
    public String getName() { return name; }
    public int getCost() { return cost; }
}