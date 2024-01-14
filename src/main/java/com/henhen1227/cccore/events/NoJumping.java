package com.henhen1227.cccore.events;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.henhen1227.cccore.CCCore;
import com.henhen1227.cccore.ChatManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import static com.henhen1227.cccore.events.EventManager.teleportAllPlayersToLobby;

public class NoJumping extends PeriodicEvent implements Listener {

    public NoJumping() {
        super("no_jumping");
    }


    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setStartingGear(Player player) {
//        ItemStack ladders = new ItemStack(Material.LADDER);
//        ladders.setAmount(16);
//        player.getInventory().setItemInMainHand(ladders);
    }

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent event){
        if (isInEventWorld(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDragonKill(EntityDeathEvent event){
        if (event.getEntity().getWorld().getName().equals("no_jumping_the_end")) {
            if (event.getEntity() instanceof EnderDragon) {
                ChatManager.titleMessage("The Dragon has been defeated!", "WITHOUT JUMPING!");
                ChatManager.message("The Dragon has been defeated WITHOUT JUMPING!");
                ChatManager.message("Well done!");
                ChatManager.message("You will be teleported back to the lobby in 60 seconds.");
                Bukkit.getScheduler().scheduleSyncDelayedTask(CCCore.instance, () -> {
                    teleportAllPlayersToLobby("no_jumping");
                }, 20 * 60);
            }
        }
    }
}
