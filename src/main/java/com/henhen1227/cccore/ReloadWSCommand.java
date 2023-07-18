package com.henhen1227.cccore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static com.henhen1227.cccore.CCCore.chatEventHandler;

public class ReloadWSCommand implements CommandExecutor {

    public ReloadWSCommand() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("reloadws")) {
            if (sender.hasPermission("cccore.reloadws.use")) {
                // disconnect the socket
                chatEventHandler.getSocket().disconnect();

                // reconnect the socket
                chatEventHandler.getSocket().connect();

                sender.sendMessage("WebSocket connection reloaded!");

            } else {
                sender.sendMessage("You don't have permission to use this command.");
            }
            return true;
        }

        return false;
    }
}