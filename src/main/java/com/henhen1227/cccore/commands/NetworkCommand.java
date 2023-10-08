package com.henhen1227.cccore.commands;

import com.henhen1227.cccore.networking.NetworkManager;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class NetworkCommand implements CommandExecutor {

    public NetworkCommand() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("reloadws")) {
            if (sender.hasPermission("cccore.reloadws.use") || sender.hasPermission("cccore.admin.use")) {
                NetworkManager.resetWebsocketConnection();

                sender.sendPlainMessage("WebSocket connection reloaded!");

                return true;
            } else {
                sender.sendMessage(Component.text("<red>You don't have permission to use this command."));
                return true;
            }
        }

        return false;
    }
}