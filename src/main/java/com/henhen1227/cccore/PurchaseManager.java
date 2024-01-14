package com.henhen1227.cccore;

import com.henhen1227.cccore.networking.NetworkManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.ChestBoat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PurchaseManager {

    public static void attemptPurchase(Player player, ItemStack item) {

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();

        try {
            // Get price
            int price = dataContainer.get(new NamespacedKey(CCCore.instance, "price"), PersistentDataType.INTEGER);
            String itemUniqueId = dataContainer.get(new NamespacedKey(CCCore.instance, "unique_id"), PersistentDataType.STRING);

            // Check if player has enough money
            NetworkManager.buyItem(player, itemUniqueId, price, (error) -> {
                if (error != null) {
                    Bukkit.getLogger().info("Failed to buy item");
                    ChatManager.error("Failed to buy item", player);
                    ChatManager.error(error, player);
                } else {
                    Bukkit.getLogger().info("Bought item");
                    ChatManager.message("Bought item", player);

                    spawnChestWithItem(player.getWorld(), item, (x, y, z, isBoat) -> {
                        // Send message to player
                        ChatManager.message("You have purchased " + item.getItemMeta().getDisplayName() + " for " + price + " points!", player);
                        ChatManager.message("§k Secret name§r: Your package has been secretly hidden. ", player);

                        ItemStack locationDetails = new ItemStack(Material.COMPASS);
                        CompassMeta locationDetailsMeta = (CompassMeta) locationDetails.getItemMeta();
                        locationDetailsMeta.setDisplayName("§r Secret location");
                        locationDetailsMeta.setLore(Arrays.asList(
                                "§r X: " + x,
                                "§r Z: " + z));
                        locationDetailsMeta.setLodestone(new Location(Bukkit.getWorld("world"), x, y, z));
                        locationDetails.setItemMeta(locationDetailsMeta);

                        player.getInventory().addItem(locationDetails);
                    });
                }
            });

        } catch (NullPointerException e) {
            Bukkit.getLogger().info(item.toString() + "has no price");
            ChatManager.message("There was an error: No price was found for this item.", player);
        }

    }

//    public static void spawnChestWithItem(World world, String itemUniqueId, ChestLocationCallback callback) {
    public static void spawnChestWithItem(World world, ItemStack item, ChestLocationCallback callback) {
        // Takes most of the load off of the main thread.
        new Thread(() -> {
            Random random = new Random();

            // List of replaceable materials (unused)
            List<Material> replaceableMaterials = Arrays.asList(Material.AIR, Material.GRASS, Material.TALL_GRASS, Material.SNOW);

            // Generate random x and z coordinates between -4000 and 4000
            int x = random.nextInt(8000) - 4000;
            int z = random.nextInt(8000) - 4000;

//            int x = ((Number) location.get("x")).intValue();
//            int z = ((Number) location.get("z")).intValue();

            Bukkit.getLogger().info("Trying position: (" + x + " " + z + ")");


            // Get the world's highest block at x, z
            // This has to load the chunks to get the highest block
            // So it causes the whole server to freeze
            // That's why I'm loading it once
            // Instead of looping until I find a valid position
            int y = world.getHighestBlockYAt(x, z);

            // Get the block at x, y, z
            Block block = world.getBlockAt(x, y + 1, z);
            Block blockBelow = world.getBlockAt(x, y, z);

            // Check if it's replaceable
            Bukkit.getLogger().info("Block below is: " + blockBelow.getType());
            Bukkit.getLogger().info("Block is: " + block.getType());
            if (blockBelow.getType() == Material.WATER || blockBelow.getType() == Material.KELP_PLANT || blockBelow.getType() == Material.SEAGRASS) {
                Bukkit.getLogger().info("Spawning boat chest");

                // Spawn Boat with Chest
                Bukkit.getScheduler().runTask(CCCore.instance, () -> {
                    // Get chest and add items to it
                    ChestBoat chestBoat = (ChestBoat) world.spawnEntity(block.getLocation(), EntityType.CHEST_BOAT);
//                    ItemStack item = MagicItemManager.getItem(itemUniqueId);

                    chestBoat.getInventory().setItem(13, item);


                    callback.onChestPlaced(x,y,z, true);
                });
            }else{
                // If it is, schedule a task on the main thread to place the chest
                Bukkit.getScheduler().runTask(CCCore.instance, () -> {
                    // Change block type to chest
                    block.setType(Material.CHEST);

                    // Get chest and add items to it
                    Chest chest = (Chest) block.getState();

//                    ItemStack item = MagicItemManager.getItem(itemUniqueId);

                    // Add the item to the chest
                    chest.getBlockInventory().setItem(13, item);

                    callback.onChestPlaced(x,y,z, false);
                });

                // Wait for the task to finish
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
