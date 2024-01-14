package com.henhen1227.cccore.events;

import com.henhen1227.cccore.CCCore;
import com.henhen1227.cccore.ChatManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public abstract class PeriodicEvent extends Event implements Listener {

    private final HashMap<UUID, Location> playerLastLocations = new HashMap<>();
    private final LocalTime startTime;
    private final LocalTime endTime;


    public PeriodicEvent(String id) {
        this(id, LocalTime.of(12, 0), LocalTime.of(13, 7));
    }

    public PeriodicEvent(String id, LocalTime start, LocalTime end) {
        super(id);
        startTime = start;
        endTime = end;
    }

    @Override
    public void stop() {

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (isInEventWorld(event.getPlayer())) {
            savePlayerLocation(player);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(isInEventWorld(event.getPlayer())) {
            if (!isEventTime()) {
                ChatManager.message("This world is no longer available at this time.", event.getPlayer());
                EventManager.teleportPlayer(event.getPlayer(), "lobby");
            }
        }
    }

    @EventHandler
    public void onPlayerJoinWorld(PlayerChangedWorldEvent event){
        if (isInEventWorld(event.getPlayer()) && !isEventWorld(event.getFrom())) {
            if (isEventTime()){
                teleportToSavedLocation(event.getPlayer());
            } else {
                ChatManager.message("You cannot join this world at this time!", event.getPlayer());
                EventManager.teleportPlayer(event.getPlayer(), "lobby", false);
            }
        }
    }

    private void teleportToSavedLocation(Player player) {
        UUID playerId = player.getUniqueId();
        if (CCCore.instance.getConfig().contains(getUniqueId() + ".playerLocations." + playerId.toString())) {
            String locationString = CCCore.instance.getConfig().getString(getUniqueId() + ".playerLocations." + playerId.toString());
            Location loc = stringToLocation(locationString);
            if (loc != null) {
                player.teleport(loc);
            }
        }
    }

    public void teleportPlayersToLobby(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isInEventWorld(player)) {
                EventManager.teleportPlayer(player, "lobby");
            }
        }
    }

    private Location stringToLocation(String locationString) {
        try {
            String[] parts = locationString.split(",");
            if(parts.length != 6) return null;
            World world = Bukkit.getServer().getWorld(parts[0]);
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            float yaw = Float.parseFloat(parts[4]);
            float pitch = Float.parseFloat(parts[5]);
            return new Location(world, x, y, z, yaw, pitch);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<UUID, Location> loadPlayerLocations() {
        HashMap<UUID, Location> playerLocations = new HashMap<>();
        ConfigurationSection locationsSection = CCCore.instance.getConfig().getConfigurationSection(getUniqueId() + ".playerLocations");
        if (locationsSection != null) {
            for (String key : locationsSection.getKeys(false)) {
                Location loc = Objects.requireNonNull(stringToLocation(locationsSection.getString(key)));
                playerLocations.put(UUID.fromString(key), loc);
            }
        }
        return playerLocations;
    }

    public void clearPlayerLocationFromConfig(UUID playerId) {
        CCCore.instance.getConfig().set(getUniqueId() + ".playerLocations." + playerId.toString(), null);
        CCCore.instance.saveConfig();
    }

    public void savePlayerLocation(Player player) {
        savePlayerLocation(player.getUniqueId(), player.getLocation());
    }

    public void savePlayerLocation(UUID uuid, Location loc) {
        String locationString = loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
        CCCore.instance.getConfig().set(getUniqueId() + ".playerLocations." + uuid.toString(), locationString);
        CCCore.instance.saveConfig();

        playerLastLocations.put(uuid, loc);
    }

    public void savePlayerLocations(HashMap<UUID, Location> playerLocations) {
        for (Map.Entry<UUID, Location> entry : playerLocations.entrySet()) {
            UUID uuid = entry.getKey();
            Location loc = entry.getValue();
            String locationString = loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
            CCCore.instance.getConfig().set(getUniqueId() + ".playerLocations." + uuid.toString(), locationString);
        }
        CCCore.instance.saveConfig();
    }

    public boolean isEventTime(){
        LocalTime now = LocalTime.now();

        if(startTime.isBefore(endTime)){
            return now.isAfter(startTime) && now.isBefore(endTime);
        }else{
            return now.isAfter(startTime) || now.isBefore(endTime);
        }
    }
}
