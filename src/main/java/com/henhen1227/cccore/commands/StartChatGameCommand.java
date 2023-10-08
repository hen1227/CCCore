package com.henhen1227.cccore.commands;

import com.henhen1227.cccore.ChatManager;
import com.henhen1227.cccore.chatGames.ChatGame;
import com.henhen1227.cccore.chatGames.ChatGameManager;
import com.henhen1227.cccore.networking.NetworkManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.json.simple.JSONObject;

public class StartChatGameCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("startchatgame") && sender.hasPermission("cccore.admin.use")) {
            Bukkit.getLogger().info("StartChatGameCommand");

            if (args.length == 1) {
                String gameName = args[0];
                if(ChatGameManager.startGame(gameName)){
                    ChatManager.message(String.format("Starting game %s", gameName), sender);
                }else{
                    ChatManager.error(String.format("Game %s not found", gameName), sender);
                }
                return true;
            }

            ChatManager.message("Starting random game", sender);
            ChatGameManager.startRandomGame();
            return true;
        }
        Bukkit.getLogger().info("StartChatGameCommand failed");

        return false;
    }
}