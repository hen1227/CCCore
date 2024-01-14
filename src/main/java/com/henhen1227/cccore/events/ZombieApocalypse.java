package com.henhen1227.cccore.events;

import com.henhen1227.cccore.CCCore;
import com.henhen1227.cccore.ChatManager;
import com.henhen1227.cccore.events.shop.ItemShop;
import com.henhen1227.cccore.events.shop.SwordShop;
import org.bukkit.*;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static com.henhen1227.cccore.utils.ItemGeneration.createUnbreakableItem;

public class ZombieApocalypse extends Event implements Listener {

    public int wave = 0;

    private final int[][] spawnerLocations = {
            {-2, -1, -8},
            {-41, 0, -10},
            {31, -1, -19},
            {27, -1, -3},
            {46, -1, -19}, // <--
            {49, 2, 30},
            {31, -1, 17},
            {40, -1, 47},
            {24, 0, 50},
            {-15, 1, 36},
            {-30, 0, 17},
            {1, 0, 17}
    };

    private final Map<Location, ItemShop> shops = new HashMap<>();


    public ZombieApocalypse() {
        super("zombie_apocalypse");
    }

    public void registerShops(){
//        registerShop(
//                new Location(CCCore.instance.getServer().getWorld(getUniqueId()), 0, 0, 0),
//                new SwordShop()
//        );
    }

    @Override
    public void start() {
        startWave();
    }

    @Override
    public void stop() {

    }

    private void startWave(){
        wave++;
        EventManager.announce("Wave " + wave + " has started!");

        generateSpawners();
    }

    public void registerShop(Location location, ItemShop shop) {
        shop.showShop(location);
        shops.put(location, shop);
    }

    public Optional<ItemShop> getShopAt(Location location) {
        return Optional.ofNullable(shops.get(location));
    }

    @Override
    public void setStartingGear(Player player) {
        PlayerInventory inventory = player.getInventory();

        // Sword
        inventory.setItem(0, createUnbreakableItem(Material.WOODEN_SWORD));

        // Pickaxe
        ItemStack pickaxe = createUnbreakableItem(Material.WOODEN_PICKAXE);

        ItemMeta meta = pickaxe.getItemMeta();
        meta.setDestroyableKeys(List.of(Material.SPAWNER.getKey()));
        inventory.setItem(1, pickaxe);


        // Chestplate
        ItemStack chestplate = createUnbreakableItem(Material.LEATHER_CHESTPLATE);
        chestplate.getItemMeta().addEnchant(Enchantment.BINDING_CURSE, 1, true);
        chestplate.getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
        inventory.setChestplate(chestplate);

        // Leggings
        ItemStack leggings = createUnbreakableItem(Material.LEATHER_LEGGINGS);
        leggings.getItemMeta().addEnchant(Enchantment.BINDING_CURSE, 1, true);
        leggings.getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
        inventory.setLeggings(leggings);

        ItemStack boots = createUnbreakableItem(Material.LEATHER_BOOTS);
        boots.getItemMeta().addEnchant(Enchantment.DEPTH_STRIDER, 3, true);
        boots.getItemMeta().addEnchant(Enchantment.PROTECTION_FALL, 10, true);
        boots.getItemMeta().addEnchant(Enchantment.BINDING_CURSE, 1, true);
        boots.getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
        inventory.setBoots(boots);

        // Food
        inventory.setItem(7, new ItemStack(Material.COOKED_BEEF, 16));
        inventory.setItem(8, new ItemStack(Material.GOLDEN_APPLE, 3));
    }

    public void generateSpawnerStructure(int x, int y, int z){
        World world = Bukkit.getWorld(unique_id);
        assert world != null;
        Location location = new Location(world, x, y, z);
        world.strikeLightning(location.add(0, 3, 0));
        location.subtract(0, 3, 0);

        // Spawner
        location.getBlock().setType(Material.SPAWNER);
        CreatureSpawner spawner = (CreatureSpawner) location.getBlock().getState();

        spawner.setSpawnCount(50);
        spawner.setRequiredPlayerRange(30);
        spawner.setSpawnRange(4);
        spawner.setMinSpawnDelay(40);
        spawner.setMaxSpawnDelay(80);
        // Give zombie a wooden sword

        for(int i = 0; i < wave+3; i++) {
            Location spawnLocation = location.clone();
            spawnLocation.add(new Random().nextInt(5), 1, new Random().nextInt(5));
            Zombie zombie = (Zombie) Bukkit.getWorld(unique_id).spawnEntity(spawnLocation, EntityType.ZOMBIE);
            zombie.getEquipment().setItemInMainHand(new ItemStack(Material.WOODEN_SWORD));
            zombie.getEquipment().setItemInMainHandDropChance(0.0f);
            zombie.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
            zombie.getEquipment().setHelmetDropChance(0.0f);
            zombie.setAdult();
        }

        for(int i = 0; i < wave/3; i++) {
            Location spawnLocation = location.clone();
            spawnLocation.add(new Random().nextInt(5), -2, new Random().nextInt(5));
            Skeleton skeleton = (Skeleton) Bukkit.getWorld(unique_id).spawnEntity(spawnLocation, EntityType.SKELETON);
            skeleton.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
            skeleton.getEquipment().setHelmetDropChance(0.0f);
            skeleton.getEquipment().setItemInMainHand(new ItemStack(Material.BOW));
            skeleton.getEquipment().setItemInMainHandDropChance(0.0f);
        }

        spawner.setSpawnedType(EntityType.ZOMBIE);
//        spawner.setSpawnedItem(new ItemStack(Material.WOODEN_SWORD));
        spawner.update(true);

        // Rest of structure
//        location.add(0, 1, 0);
//        location.getBlock().setType(Material.RED_STAINED_GLASS_PANE);
        location.add(0, -1, 0);
        location.getBlock().setType(Material.MOSSY_COBBLESTONE_WALL);
        location.add(0, -1, 0);
        location.getBlock().setType(Material.RED_STAINED_GLASS);
        location.add(0, -1, 0);
    }

    // Listen to broken spawners
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // Check if block is spawner
        if (event.getBlock().getType() == Material.SPAWNER) {
            // Check if spawner is in zombie apocalypse
            if (event.getBlock().getLocation().getWorld().getName().equals(unique_id)) {
                event.setCancelled(true);

                // Strike block with lightning
                event.getBlock().getWorld().strikeLightning(event.getBlock().getLocation().add(0, 3, 0));

                // Award player with 20 points to scoreboard
                EventManager.coinManager.addCoins(event.getPlayer(), 20);

                // Destroy Structure
                Location location = event.getBlock().getLocation();
                location.getBlock().setType(Material.AIR);
                location.add(0, -2, 0);
                location.getBlock().setType(Material.DEAD_BRAIN_CORAL_BLOCK);

                // Check if all spawners are broken
                boolean allSpawnersBroken = true;
                for (int[] spawnerLocation : spawnerLocations) {
                    Location spawner = new Location(event.getBlock().getWorld(), spawnerLocation[0], spawnerLocation[1], spawnerLocation[2]);
                    if (spawner.getBlock().getType() == Material.SPAWNER && !spawner.equals(event.getBlock().getLocation())) {
                        allSpawnersBroken = false;
//                        ChatManager.warning("Not all spawners are broken! at " + spawner.toString(), event.getPlayer());
                    }
                }

                // If all spawners are broken, start next wave
                if (allSpawnersBroken) {
                    startWave();
                }else{
                    // Display action message
                    ChatManager.actionMessage("Spawner broken!");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Check if player is in zombie apocalypse
        if (event.getEntity().getWorld().getName().equals(unique_id)) {
            Player player = (Player) event.getEntity();
            ChatManager.message(String.format("%s died and lost half their coins!", player.getName()));
            EventManager.coinManager.removeHalfCoins(player);
        }
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        // Check if player is in zombie apocalypse
        if (event.getEntity().getWorld().getName().equals(unique_id)) {
            // Check if entity is a zombie
            if (event.getEntityType() == EntityType.ZOMBIE) {
                // Check if zombie was killed by a player
                if (event.getEntity().getKiller() != null) {
                    if (event.getEntity().getType() == EntityType.ZOMBIE) {
                        // Award player with 2 points to scoreboard
                        EventManager.coinManager.addCoins(event.getEntity().getKiller(), 2);
                    }else if (event.getEntity().getType() == EntityType.SKELETON) {

                        EventManager.coinManager.addCoins(event.getEntity().getKiller(), 5);
                    }else if (event.getEntity().getType() == EntityType.PIGLIN_BRUTE) {

                        EventManager.coinManager.addCoins(event.getEntity().getKiller(), 10);
                    }else if (event.getEntity().getType() == EntityType.ZOGLIN) {

                        EventManager.coinManager.addCoins(event.getEntity().getKiller(), 10);
                    }else if (event.getEntity().getType() == EntityType.RAVAGER) {

                        // Award Every player with 15 point to scoreboard
                        List<Player> players = EventManager.allPlayersInEvent(unique_id);
                        for(Player player : players){
                            EventManager.coinManager.addCoins(player, 15);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if(!event.getPlayer().getWorld().getName().equals(getUniqueId())) return;
        if(event.getPlayer().hasPermission("cccore.dropitems.bypass") || event.getPlayer().hasPermission("cccore.admin.use")) return;

        if(event.getItemDrop().getItemStack().getType() != Material.DIAMOND) {
            event.setCancelled(true);
            ChatManager.message("You can not drop this item!", event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof ArmorStand)) return;

        Bukkit.getLogger().info("clicked armor stand");
        Location clickedLocation = event.getRightClicked().getLocation();
        Optional<ItemShop> shopOptional = getShopAt(clickedLocation);
        Bukkit.getLogger().info(String.valueOf(shopOptional));

        if (shopOptional.isPresent()) {
            shopOptional.get().handleInteraction(event.getPlayer());
            event.setCancelled(true);
        }
    }

    public void generateSpawners() {
        // Max of spawnerLocations.length spawners
        // Min of 3 spawners
        // Number increases by 1 every 5 waves up to max
        int numberOfSpawners = Math.min (3 + (wave / 5), spawnerLocations.length);

        // Choose numberOfSpawners unique locations from spawner Locations
        List<int[]> chosenSpawnerLocations = selectNRandom(spawnerLocations, numberOfSpawners);

        for (int[] location : chosenSpawnerLocations) {
            generateSpawnerStructure(location[0], location[1], location[2]);
        }
    }

    public static List<int[]> selectNRandom(int[][] array, int n) {
        if (n > array.length) {
            throw new IllegalArgumentException("n cannot be greater than the array length.");
        }

        List<int[]> list = new ArrayList<>(Arrays.asList(array));

        List<int[]> result = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            int randomIndex = rand.nextInt(list.size());
            result.add(list.get(randomIndex));
            list.remove(randomIndex);
        }

        return result;
    }
}
