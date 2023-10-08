package com.henhen1227.cccore.items;

import com.henhen1227.cccore.CCCore;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MagicItemManager {

    public static List<MagicItem> magicItems;

    public static void registerEvents(PluginManager pluginManager) {
        magicItems = new ArrayList<>();

        RandomCompass randomCompass = new RandomCompass();
        pluginManager.registerEvents(randomCompass, CCCore.instance);
        magicItems.add(randomCompass);

        EndlessStew endlessStew = new EndlessStew();
        pluginManager.registerEvents(endlessStew, CCCore.instance);
        magicItems.add(endlessStew);

        InvisibleSaddle invisibleSaddle = new InvisibleSaddle();
        pluginManager.registerEvents(invisibleSaddle, CCCore.instance);
        magicItems.add(invisibleSaddle);

        NightWings nightWings = new NightWings();
        pluginManager.registerEvents(nightWings, CCCore.instance);
        magicItems.add(nightWings);
    }

    public static void initializeListeners(){
        // Task that runs every morning
        Bukkit.getScheduler().runTaskTimer(CCCore.instance, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null && item.hasItemMeta()) {
                        for (MagicItem magicItem : magicItems) {
                            PersistentDataContainer dataContainer = item.getItemMeta().getPersistentDataContainer();
                            if (magicItem.unique_id.equals(dataContainer.get(magicItem.key, PersistentDataType.STRING))) {
                                magicItem.morningUpdate(player, item);
                            }
                        }
                    }
                }
            }
        }, 0L, 20L * 60L * 60L * 24L);  // 20 ticks/second * 60 seconds/minute * 60 minutes/hour * 24 hours/day
    }

    public static ItemStack getItem(String uniqueId) {
        Bukkit.getLogger().info("Looking for item " + uniqueId);
        Bukkit.getLogger().info("In list " + magicItems.toString());
        for(MagicItem magicItem : magicItems){
            if(Objects.equals(magicItem.unique_id, uniqueId)){
                Bukkit.getLogger().info("Found item " + magicItem.unique_id);
                return magicItem.item();
            }
        }
        return defaultItem();
    }

    public static ItemStack defaultItem(){
        ItemStack itemStack = new ItemStack(Material.APPLE);

        ItemMeta meta = itemStack.getItemMeta();

        // Set name and lore
        meta.setDisplayName("Glitched CCCore");
        meta.setLore(Arrays.asList("This item should not exist!", "Negative energy flows through it"));

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    // TODO: Add enchantments
    // TODO: Add Characteristics such as Unbreakable
    public static ItemStack createItem(String uniqueId, Material material, String name, List<String> lore){
        ItemStack itemStack = new ItemStack(material);

        ItemMeta meta = itemStack.getItemMeta();

        // Set name and lore
        meta.displayName(Component.text(name));

        // List<String> into List<TextComponent>
        meta.lore(lore.stream().map(Component::text).collect(Collectors.toList()));

        // Set unique ID
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        dataContainer.set(new NamespacedKey(CCCore.instance, "unique_id"), PersistentDataType.STRING, uniqueId);

        itemStack.setItemMeta(meta);

        return itemStack;
    }
}
