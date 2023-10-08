package com.henhen1227.cccore.commands;

import com.henhen1227.cccore.CCCore;
import com.henhen1227.cccore.ChatManager;
import com.henhen1227.cccore.networking.NetworkManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CCCoreCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reloadconfig") && sender.hasPermission("cccore.admin.use")) {
                CCCore.instance.reloadConfig();
                sender.sendMessage(Component.text("Configuration reloaded successfully!"));
                return true;
            } else if (args[0].equalsIgnoreCase("reloadws") && sender.hasPermission("cccore.admin.use")) {
                // Reload the websocket connection
                sender.sendMessage(Component.text("Requested WebSocket connection reset!"));
                NetworkManager.resetWebsocketConnection();
                return true;
            } else if (args[0].equalsIgnoreCase("test") && sender.hasPermission("cccore.admin.use")) {
                // Test POST request
                // Test purchasing an item
                // Test sending a message
                // Test chat event
                // Test major event

            } else if (args[0].equalsIgnoreCase("help") && sender.hasPermission("cccore.default.use") || sender.hasPermission("cccore.admin.use")) {
                ChatManager.message("CCCore Help Menu", sender);
                ChatManager.message("/cccore help – Shows this list", sender);
                ChatManager.message("/cccore status – Shows connection to webserver", sender);
                return true;
            } else if (args[0].equalsIgnoreCase("help") && (sender.hasPermission("cccore.default.use") || sender.hasPermission("cccore.admin.use"))) {
                ChatManager.message(NetworkManager.status(), sender);

                return true;
            } else if (args[0].equalsIgnoreCase("version") && (sender.hasPermission("cccore.default.use") || sender.hasPermission("cccore.admin.use"))) {
                ChatManager.message("Currently running version: <" + CCCore.instance.getPluginMeta().getVersion() + ">", sender);
                ChatManager.message("Made by Henry Abrahamsen <henry@henhen1227.com>", sender);
                ChatManager.message("Learn more at https://mc.henhen1227.com", sender);

                return true;
            }
        }
        ChatManager.message("––––––===––––––", sender);
        ChatManager.message("Currently running version: <"+ CCCore.instance.getPluginMeta().getVersion()+">", sender);
        ChatManager.message("Made by Henry Abrahamsen <henry@henhen1227.com>", sender);
        ChatManager.message("Learn more at https://mc.henhen1227.com", sender);
        ChatManager.message("––––––===––––––", sender);
        return true;
    }
}