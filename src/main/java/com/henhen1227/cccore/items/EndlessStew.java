package com.henhen1227.cccore.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EndlessStew extends MagicItem implements Listener {

    private final PotionEffectType[] effects = {
            PotionEffectType.BLINDNESS,
            PotionEffectType.CONFUSION,
            PotionEffectType.SLOW,
            PotionEffectType.DARKNESS,
            PotionEffectType.DAMAGE_RESISTANCE,
            PotionEffectType.DAMAGE_RESISTANCE,
            PotionEffectType.DAMAGE_RESISTANCE,
            PotionEffectType.SLOW_FALLING,
            PotionEffectType.SLOW_FALLING,
            PotionEffectType.SLOW_FALLING,
            PotionEffectType.INVISIBILITY,
            PotionEffectType.INVISIBILITY,
            PotionEffectType.INVISIBILITY,
            PotionEffectType.NIGHT_VISION,
            PotionEffectType.NIGHT_VISION,
            PotionEffectType.NIGHT_VISION,
            PotionEffectType.SPEED,
            PotionEffectType.SPEED,
            PotionEffectType.SPEED,
            PotionEffectType.LEVITATION,
            PotionEffectType.LEVITATION,
            PotionEffectType.LEVITATION,
            PotionEffectType.LEVITATION,
            PotionEffectType.LEVITATION,
            PotionEffectType.LEVITATION,
    };
    private final Random random = new Random();

    public EndlessStew() {
        super("endless_stew", 1500);
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            if (unique_id.equals(dataContainer.get(key, PersistentDataType.STRING))) {
                // Replace the stew with a bowl
                event.setCancelled(true);
                item.setAmount(item.getAmount() - 1);

                // Create a bowl with the unique ID "endless_stew" and lore
                ItemStack bowl = new ItemStack(Material.BOWL);
                ItemMeta bowlMeta = bowl.getItemMeta();
                PersistentDataContainer bowlDataContainer = bowlMeta.getPersistentDataContainer();
                bowlDataContainer.set(key, PersistentDataType.STRING, "endless_stew");
                bowlMeta.setLore(Arrays.asList("This bowl seems to be empty.", "Maybe it will refill in the morning?"));
                bowl.setItemMeta(bowlMeta);

                event.getPlayer().getInventory().addItem(bowl);
                event.getPlayer().setFoodLevel(Math.min(event.getPlayer().getFoodLevel() + 6, 20));  // The food level increase of a mushroom stew
                event.getPlayer().setSaturation(Math.min(event.getPlayer().getSaturation() + 7.2f, 20));  // The saturation increase of a mushroom stew
            }
        }
    }


    @Override
    public void morningUpdate(Player player, ItemStack item) {
        // Check if the item is a bowl
        if (item.getType() == Material.BOWL) {
            ItemStack stew = item();
            item.setAmount(0);
            player.getInventory().addItem(stew);
        }
    }

    @Override
    public ItemStack item(){
        List<String> lore = Arrays.asList("This stew seems to be full.","It has a strange smell...");
        // Replace the bowl with a full stew
        ItemStack item = MagicItemManager.createItem(unique_id, Material.SUSPICIOUS_STEW, "Timeless Suspicious Stew", lore, price);

        // Custom Settings
        SuspiciousStewMeta meta = (SuspiciousStewMeta) item.getItemMeta();
        meta.addCustomEffect(new PotionEffect(effects[random.nextInt(effects.length)], 20 * 60, 1), true);

        item.setItemMeta(meta);
        return item;
    }
}
