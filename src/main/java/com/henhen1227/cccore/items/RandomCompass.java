package com.henhen1227.cccore.items;

import com.henhen1227.cccore.CCCore;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomCompass extends MagicItem implements Listener {

    public static String unique_id = "random_compass";
    private final String[] messages = {
            "Who knows, maybe there is treasure over there!",
            "Actually! Lets go this way",
            "No this way!",
            "Adventure awaits!",
            "The compass is pulling us in this direction!",
            "Let's see what's over here!",
            "The winds of fate are blowing this way!",
            "Something interesting might be waiting for us!",
            "The compass never lies, let's go!",
            "Trust in the compass, it knows the way!",
            "The journey is the destination, let's move!",
            "The compass points to uncharted lands!",
            "Let's follow the compass, it knows the way!",
            "The compass is calling, we must go!",
            "The compass spins, a new direction it pins!",
            "The compass stirs, to adventure it refers!",
            "The compass twitches, let's find what it pitches!",
            "The compass sways, showing us the ways!",
            "The compass beckons, to a land unreckoned!",
            "The compass whirls, to where the adventure unfurls!"
    };

    public RandomCompass() {
        super(RandomCompass.unique_id, 400);
    }


    // Listen for the PlayerInteractEvent
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = event.getItem();
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
                if (RandomCompass.unique_id.equals(dataContainer.get(key, PersistentDataType.STRING))) {
                    CompassMeta compassMeta = (CompassMeta) meta;

                    // Generate a random location
                    World world = event.getPlayer().getWorld();
                    setRandomLocation(compassMeta, world);

                    item.setItemMeta(compassMeta);

                    // Select a random message
                    String message = messages[new Random().nextInt(messages.length)];

                    // Display the message to the player
                    event.getPlayer().sendMessage(message);
                }
            }
        }
    }


    @Override
    public ItemStack item(){
        ItemStack item = MagicItemManager.createItem(
                unique_id,
                Material.COMPASS,
                "Wanderer's Compass",
                Arrays.asList("Help you get where ever the compass desires"),
                price);

        // Custom Settings
        CompassMeta meta =  (CompassMeta) item.getItemMeta();

        World world = CCCore.instance.getServer().getWorlds().get(0);
        setRandomLocation(meta, world);

        item.setItemMeta(meta);
        return item;
    }

    private void setRandomLocation(CompassMeta meta, World world) {
        double x = ThreadLocalRandom.current().nextDouble(-5000, 5000);
        double z = ThreadLocalRandom.current().nextDouble(-5000, 5000);
        double y = 1227;
        Location location = new Location(world, x, y, z);

//        Bukkit.getLogger().info("Location: " + location);
        meta.setLodestone(location);

        meta.setLodestoneTracked(false);  // Make the compass point to the lodestone location
    }
}
