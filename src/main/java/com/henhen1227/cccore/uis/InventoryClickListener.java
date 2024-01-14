package com.henhen1227.cccore.uis;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

    public class InventoryClickListener implements Listener {
        public InventoryClickListener() {

        }

        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if (event.getInventory().getHolder() instanceof UIView) {
                ((UIView) event.getInventory().getHolder()).handleClickEvent(event);
            }
        }
    }