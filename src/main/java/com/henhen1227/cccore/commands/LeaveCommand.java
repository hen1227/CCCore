package com.henhen1227.cccore.commands;

import com.henhen1227.cccore.ChatManager;
import com.henhen1227.cccore.events.EventManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("leave")) {
            if(sender instanceof Player) {

                if (!EventManager.isInEventWorld(((Player) sender).getPlayer())) {
                    ChatManager.warning("You aren't in a game or lobby to leave!", (Player) sender);
                    return true;
                }

                if (!((Player) sender).getWorld().getName().equals("lobby")){
                    EventManager.teleportPlayer((Player) sender, "lobby");
                    return true;
                }


                EventManager.teleportPlayer((Player) sender, "world");
                return true;
            }

            return true;
        }

        return false;
    }
}