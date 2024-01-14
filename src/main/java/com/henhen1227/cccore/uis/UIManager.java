package com.henhen1227.cccore.uis;

import com.henhen1227.cccore.CCCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class UIManager {
    private static final Map<String, UIView> uis = new HashMap<>();

    public static void registerUIs() {
        registerUI("shop", new ShopUI());

        Bukkit.getServer().getPluginManager().registerEvents(new InventoryClickListener(), CCCore.instance);
        Bukkit.getServer().getPluginManager().registerEvents(new TraderClickListener(), CCCore.instance);
    }

    public static void registerUI(String name, UIView ui) {
        uis.put(name, ui);
    }

    public static void openUI(Player player, String uiName) {
        if (uis.containsKey(uiName)) {
            uis.get(uiName).open(player);
        }
    }
}