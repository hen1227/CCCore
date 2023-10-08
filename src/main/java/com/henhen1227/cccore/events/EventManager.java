package com.henhen1227.cccore.events;

import com.henhen1227.cccore.CCCore;
import com.henhen1227.cccore.ChatManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public class EventManager {

    private static final List<Event> events = new ArrayList<>();
    public static EventCoinManager coinManager;

    public static void registerEvents(){
        ZombieApocalypse zombieApocalypse = new ZombieApocalypse();
        Bukkit.getPluginManager().registerEvents(zombieApocalypse, CCCore.instance);
        events.add(zombieApocalypse);
    }

    public static void registerCoinManager(){
        coinManager = new EventCoinManager();
    }

    public static boolean startEvent(String eventName){
        for (Event event : events) {
            if(event.getUniqueId().equals(eventName)){
                event.start();
                teleportAllPlayersInLobby(event.getUniqueId());
                giveStartingGear(event);

                return true;
            }
        }

        return false;
    }

    public static void teleportPlayer(Player player, String to){
        World world = Bukkit.getWorld(to);
        if(world != null) {
            teleportPlayer(player, world);
        }else{
            ChatManager.error("Cannot move you to " + to + ". It does not exist");
        }
    }
    public static void teleportPlayer(Player player, World to){
        // Assuming you want to teleport the player to the world's spawn location
        Location spawnLocation = to.getSpawnLocation();
        // Check if sender is a player (since this code only makes sense for players)
        player.teleport(spawnLocation);
    }

    public static void teleportAllPlayersInLobby(String uniqueId){
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(player.getWorld().getName().equals("lobby")){
                teleportPlayer(player, uniqueId);
            }
        }
    }

    public static void giveStartingGear(Event event){
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(player.getWorld().getName().equals(event.getUniqueId())){
                player.getInventory().clear();
                PlayerInventory startingGear = event.startingGear();
                player.getInventory().setContents(startingGear.getContents());
            }
        }
    }

    public static void announce(String message){
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(isInEventWorld(player)) {
                ChatManager.message(message, player);
            }
        }
    }


    // TODO LP: Create a better method of checking if a player is in an event world
    public static boolean isInEventWorld(Player player){
        String dimension = player.getWorld().getName();
        return !(dimension.equals("world") || dimension.equals("world_nether") || dimension.equals("world_the_end"));
    }

    public static List<Player> allPlayersInEvent(String uniqueId) {
        List<Player> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld().getName().equals(uniqueId)) {
                players.add(player);
            }
        }
        return players;
    }
}
