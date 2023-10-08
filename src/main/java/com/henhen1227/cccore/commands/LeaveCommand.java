package com.henhen1227.cccore.commands;

import com.henhen1227.cccore.ChatManager;
import com.henhen1227.cccore.events.EventManager;
import com.henhen1227.cccore.networking.NetworkManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.henhen1227.cccore.events.EventManager.teleportPlayer;

public class LeaveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("leave")) {
            if(sender instanceof Player) {

                if (EventManager.isInEventWorld(((Player) sender).getPlayer())) {
                    ChatManager.warning("You aren't in a game or lobby to leave!", (Player) sender);
                    return true;
                }

                // Multiverse-Core tp without permission
                teleportPlayer((Player) sender, "world");
                return true;
            }

            return true;
        }

        return false;
    }
}