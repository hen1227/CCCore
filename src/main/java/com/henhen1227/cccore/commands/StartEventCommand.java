package com.henhen1227.cccore.commands;

import com.henhen1227.cccore.events.EventManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StartEventCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(command.getName().equalsIgnoreCase("startevent")) {
            if(sender.hasPermission("cccore.admin.use")) {
                if (args.length == 0) {
                    sender.sendMessage("Please specify an event to start!");
                    return true;
                }
                if(EventManager.startEvent(args[0])){
                    sender.sendMessage("Event started!");
                }else{
                    sender.sendMessage(String.format("Event %s not found!", args[0]));
                }
                return true;
            }
        }

        return false;
    }
}
