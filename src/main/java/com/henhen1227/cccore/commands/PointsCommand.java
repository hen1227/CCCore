package com.henhen1227.cccore.commands;

import com.henhen1227.cccore.ChatManager;
import com.henhen1227.cccore.networking.NetworkManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class PointsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("points")) {
            if(args.length == 0) {

                NetworkManager.getPoints((Player) sender, (points, error) -> {
                    if(error != null){
                        Bukkit.getLogger().info("Failed to get points");
                        ChatManager.error("Failed to get points", sender);
                    }
                    try {
                        ChatManager.message("You have " + points + "points.", sender);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

                return true;
            }else
            if (args.length < 3 || !(args[0].equalsIgnoreCase("award") || args[0].equalsIgnoreCase("revoke"))) {
                ChatManager.message("Invalid usage. Usage: /points { award|revoke } <player> <amount>", sender);
                return false;
            }

            String playerName = args[1];
            int points = Integer.parseInt(args[2]);

            if(args[0].equalsIgnoreCase("revoke")){
                points = -points;
            }

            if (awardPoints(points, playerName)) {
                ChatManager.message(String.format("Awarded %d points to %s", points, playerName), sender);
                return true;
            }else{
                ChatManager.error("Failed to award points", sender);
                return false;
            }
        }
        return false;
    }

    public static boolean awardPoints(int points, String playerName){
        try {
            String url = "/points";
            JSONObject json = new JSONObject();
            json.put("username", playerName);
            json.put("points", points);

            NetworkManager.sendPOSTRequest(url, json, (response, error) -> {
                Bukkit.getLogger().info(String.format("Awarded %d points to %s", points, playerName));
                try {
                    if (response.getStatusLine().getStatusCode() != 200) {
                        Bukkit.getLogger().warning(String.format("Failed to award %d points to %s", points, playerName));
                    }
                } catch (Exception e) {
                    Bukkit.getLogger().warning(String.format("Failed to award %d points to %s", points, playerName));
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}