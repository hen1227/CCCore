package com.henhen1227.cccore.enchantments;

import com.henhen1227.cccore.CCCore;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnchantmentManager {


    public static List<Enchantment> enchantments;

    public static void registerEvents(PluginManager pluginManager) {
        enchantments = new ArrayList<>();

        EnchantmentListener enchantmentListener = new EnchantmentListener();
        pluginManager.registerEvents(enchantmentListener, CCCore.instance);

    }


    public static ItemStack getBook(String uniqueId) {
        return getBook(uniqueId, false);
    }

    public static ItemStack getBook(String uniqueId, boolean includePricePrefix) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();

        if (meta != null) {
            GlowingEnchantment glowingEnchantment = new GlowingEnchantment();
            meta.addStoredEnchant(glowingEnchantment, 1, false);

            // TODO: Have a way to change the price of individual books
            if (includePricePrefix) {
                meta.setDisplayName("§6§l✦" + "900" + "§e§l " + glowingEnchantment.getName());
            } else {
                meta.setDisplayName("§e§l " + glowingEnchantment.getName());
            }
            // Set unique ID
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            dataContainer.set(new NamespacedKey(CCCore.instance, "unique_id"), PersistentDataType.STRING, uniqueId);

            // Set price
            dataContainer.set(new NamespacedKey(CCCore.instance, "price"), PersistentDataType.INTEGER, 900);
            book.setItemMeta(meta);

            return book;
        }

        return defaultItem();
    }

    public static ItemStack defaultItem(){
        ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);

        ItemMeta meta = itemStack.getItemMeta();

        // Set name and lore
        meta.setDisplayName("Cursed Curse");
        meta.setLore(Arrays.asList("This item should not exist!", "Negative energy flows through it"));

        itemStack.setItemMeta(meta);

        return itemStack;
    }
}
