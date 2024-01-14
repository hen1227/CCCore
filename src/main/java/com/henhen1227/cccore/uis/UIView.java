package com.henhen1227.cccore.uis;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class UIView implements InventoryHolder {
    protected Inventory inventory;
    private final Map<Integer, Consumer<InventoryClickEvent>> slotActions = new HashMap<>();

    public abstract void open(Player player);

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void addItem(int slot, Material material, String name, Consumer<InventoryClickEvent> onClick) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        addItem(slot, item, onClick);
    }

    public void addItem(int slot, ItemStack item, Consumer<InventoryClickEvent> onClick) {
        inventory.setItem(slot, item);
        slotActions.put(slot, onClick);
    }

    public void handleClickEvent(InventoryClickEvent event) {
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slotActions.containsKey(slot)) {
            slotActions.get(slot).accept(event);
        }
    }
}