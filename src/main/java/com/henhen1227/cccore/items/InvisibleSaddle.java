package com.henhen1227.cccore.items;

import com.henhen1227.cccore.CCCore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class InvisibleSaddle extends MagicItem implements Listener {

    public InvisibleSaddle() {
        super("invisible_saddle", 2000);
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
//        Bukkit.getLogger().info(String.format("Inventory click event: %s", event.getInventory().getType().toString()));

        // Handle horse inventory
        if (event.getInventory().getHolder() instanceof AbstractHorse horse) {
            // Slot 0 is the saddle slot for horses
            if (event.getSlotType() == InventoryType.SlotType.CONTAINER && event.getSlot() == 0) {
                if (isMatchingItem(event.getCursor())) {
                    // The item being placed is the magic saddle. Schedule a brief delay to allow the slot update to finish.
                    Bukkit.getScheduler().runTaskLater(CCCore.instance, () -> {
//                        if (isMatchingItem(horse.getInventory().getSaddle())) {  // Double-check after the delay
                        applyInvisibilityEffect(horse);
//                        }
                    }, 1L); // One second delay
                } else if (isMatchingItem(event.getCurrentItem())) {
                    // The current item in the slot is the magic saddle and it's being removed or replaced
                    removeInvisibilityEffect(horse);
                }
            }
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return; // Ensures the interaction is from the main hand only
//        Bukkit.getLogger().info(String.format("Entity interact event: %s", event.getRightClicked().getName()));
        if (event.getRightClicked() instanceof AbstractHorse || event.getRightClicked() instanceof Pig) {
            Player player = event.getPlayer();
            Bukkit.getLogger().info(player.getInventory().getItemInMainHand().toString());
            if (isMatchingItem(player.getInventory().getItemInMainHand())) {
                applyInvisibilityEffect(event.getRightClicked());
            }else{
                Bukkit.getLogger().info("Item in main hand does not match");
            }
        }
    }

    private void applyInvisibilityEffect(Entity entity) {
        Bukkit.getLogger().info(String.format("Applying invisibility effect to %s", entity.getName()));
        if (entity instanceof AbstractHorse) {
            ((AbstractHorse) entity).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
        } else if (entity instanceof Pig) {
            ((Pig) entity).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
        }
    }

    private void removeInvisibilityEffect(Entity entity) {
        Bukkit.getLogger().info(String.format("Removing invisibility effect to %s", entity.getName()));
        if (entity instanceof AbstractHorse) {
            ((AbstractHorse) entity).removePotionEffect(PotionEffectType.INVISIBILITY);
        } else if (entity instanceof Pig) {
            ((Pig) entity).removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    }

    @Override
    public ItemStack item() {
        ItemStack item = MagicItemManager.createItem(
                unique_id,
                Material.SADDLE,
                "Invisible Saddle",
                Arrays.asList("Steeped in Shadows", "and mystery.", "Use on a horse to", "make it invisible."),
                price
        );

        return item;
    }

}
