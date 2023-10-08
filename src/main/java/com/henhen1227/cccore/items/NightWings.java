package com.henhen1227.cccore.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class NightWings extends MagicItem implements Listener {
    public NightWings() {
        super("night_wings");
    }


    @EventHandler
    public void onElytraGlide(EntityToggleGlideEvent event){
        if (!(event.getEntity() instanceof Player)) return;

        if(isMatchingItem(((Player) event.getEntity()).getEquipment().getChestplate())){
            // If past sun set or before sun rise
            if (event.getEntity().getWorld().getTime() > 0 && event.getEntity().getWorld().getTime() < 13000) {
                ((Player) event.getEntity()).setFireTicks(25);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if(!event.getPlayer().isGliding()) return;
        if(isMatchingItem(event.getPlayer().getEquipment().getChestplate())){
            // If past sun set or before sun rise
            if (event.getPlayer().getWorld().getTime() > 0 && event.getPlayer().getWorld().getTime() < 13000) {
                event.getPlayer().setGliding(false);
            }
        }
    }


    @Override
    public ItemStack item(){
        ItemStack item = MagicItemManager.createItem(
                unique_id,
                Material.ELYTRA,
                "Wings of the Lunar Wraith",
                Arrays.asList("A reward to the moon's champion", "these wings imbue the wearer with lunar flight.", "but only below the moon's watchful eye.")
            );

        return item;
    }
}
