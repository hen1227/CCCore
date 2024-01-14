package com.henhen1227.cccore.events.shop;

import com.henhen1227.cccore.ChatManager;
import com.henhen1227.cccore.events.EventCoinManager;
import com.henhen1227.cccore.events.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public abstract class ItemShop {
    protected List<ShopItem> items = new ArrayList<>();
    protected ArmorStand armorStand;

    public ItemShop() {
    }

    public abstract void populateItems();

    public void showShop(Location location) {
        if (armorStand != null) {
            armorStand.remove();
        }

        armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setGravity(false);
        armorStand.setVisible(false);
        armorStand.setCustomNameVisible(true);
        // Assuming default is the first item
        setArmorStandItem(0);
    }

    public void handleInteraction(Player player) {
        Bukkit.getLogger().info("interaction detected");
        int currentTier = getCurrentTier(player);
        if (currentTier < items.size() - 1) {
            ShopItem currentItem = items.get(currentTier);
            if (EventManager.coinManager.getCoins(player) >= currentItem.getCost()) {
                EventManager.coinManager.removeCoins(player, currentItem.getCost());
                player.getInventory().remove(currentItem.getItem());
                player.getInventory().addItem(items.get(currentTier + 1).getItem());
                ChatManager.message("Purchase successful!", player);
                setArmorStandItem(currentTier + 1);
            } else {
                ChatManager.message("You do not have enough coins to purchase!", player);
            }
        } else {
            ChatManager.message("You already maxed out these upgrades!", player);
        }
    }

    protected void setArmorStandItem(int tier) {
        ShopItem item = items.get(tier);
        armorStand.setHelmet(item.getItem());
        armorStand.setCustomName(item.getName() + " - " + item.getCost() + " Coins");
    }

    protected abstract int getCurrentTier(Player player);
}
