package com.henhen1227.cccore.commands;

import com.henhen1227.cccore.CCCore;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.persistence.PersistentDataType;

public class SpawnShopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawntrader") && sender.hasPermission("cccore.admin.use")) {

            if (args.length < 1) {
                sender.sendMessage("Do `/spawntrader list` to see options.");
                return false;
            }

            if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage("List of options:");
                sender.sendMessage("1. /spawntrader shop");
                return true;
            }

            if (args[0].equalsIgnoreCase("shop")) {
                if(sender instanceof Player player) {
                    spawnVillager(player.getLocation(), "Shopkeeper", "shop", Villager.Profession.NONE);
                }
                return true;
            }

            sender.sendMessage(args[0] + " is not a valid option.");

            return true;
        }

        return false;
    }

    public static void spawnVillager(Location location, String name, String metadata, Villager.Profession profession) {
        Villager villager = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        villager.setCustomName(name);
        villager.setProfession(profession);
//        villager.setMetadata(metadata, new FixedMetadataValue(CCCore.instance, true));
//        villager.setMetadata("customVillager", new FixedMetadataValue(CCCore.instance, true));
        villager.setCustomNameVisible(true);
        villager.setAI(false);
        villager.setAware(true);
        villager.setInvulnerable(true);

        NamespacedKey key = new NamespacedKey(CCCore.instance, "customVillager");
        NamespacedKey villagerType = new NamespacedKey(CCCore.instance, "villagerType");
        villager.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
        villager.getPersistentDataContainer().set(villagerType, PersistentDataType.STRING, metadata);


    }
}
