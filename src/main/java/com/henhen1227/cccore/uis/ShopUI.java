package com.henhen1227.cccore.uis;

import com.henhen1227.cccore.ChatManager;
import com.henhen1227.cccore.PurchaseManager;
import com.henhen1227.cccore.enchantments.EnchantmentManager;
import com.henhen1227.cccore.items.MagicItemManager;
import com.henhen1227.cccore.networking.NetworkManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Objects;

public class ShopUI extends UIView {

    public ShopUI() {
        inventory = Bukkit.createInventory(this, 9, "Shop");
        addItem(0, Material.PLAYER_HEAD, "Your points loading...", this::handlePointsClick);

        ItemStack invisibleSaddle = MagicItemManager.getItem("invisible_saddle", true);
        addItem(2, invisibleSaddle, this::handleInvisibleSaddleClick);

        ItemStack nightWings = MagicItemManager.getItem("night_wings", true);
        addItem(3, nightWings, this::handleNightWingsClick);

//        ItemStack glowingBook = EnchantmentManager.getBook("glowing", true);
//        addItem(5, glowingBook, this::handleGlowingBookClick);

//        ItemStack invisibleSaddle = MagicItemManager.getItem("invisible_saddle", true);
//        addItem(2, invisibleSaddle, this::handleInvisibleSaddleClick);
    }

    private void handlePointsClick(InventoryClickEvent event) {
        ChatManager.message(Objects.requireNonNull(event.getCurrentItem()).getItemMeta().getDisplayName());
    }

    private void handleInvisibleSaddleClick(InventoryClickEvent event) {
        PurchaseManager.attemptPurchase((Player) event.getWhoClicked(), MagicItemManager.getItem("invisible_saddle", false));
    }
    private void handleNightWingsClick(InventoryClickEvent event) {
        PurchaseManager.attemptPurchase((Player) event.getWhoClicked(), MagicItemManager.getItem("night_wings", false));
    }
    private void handleGlowingBookClick(InventoryClickEvent event) {
        PurchaseManager.attemptPurchase((Player) event.getWhoClicked(), EnchantmentManager.getBook("glowing", false));
    }

    @Override
    public void open(Player player) {
        NetworkManager.getPoints(player, (points, error) -> {
            if(error != null){
                Bukkit.getLogger().info("Failed to get points");
                ChatManager.error("Failed to get points", player);
            }
            try {


                ItemStack pointsItem = new ItemStack(Material.PLAYER_HEAD, 1);

                SkullMeta meta = (SkullMeta) pointsItem.getItemMeta();
                meta.setDisplayName("§r You have §6§b✦" + points);
                meta.setOwningPlayer(player);
                pointsItem.setItemMeta(meta);

                inventory.setItem(0, pointsItem);

                player.openInventory(inventory);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}