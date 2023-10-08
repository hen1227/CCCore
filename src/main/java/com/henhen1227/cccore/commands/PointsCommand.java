package com.henhen1227.cccore.commands;

import com.henhen1227.cccore.CCCore;
import com.henhen1227.cccore.ChatManager;
import com.henhen1227.cccore.networking.NetworkManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PointsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("points")) {
            if(args.length == 0) {
                String url = "/points/"+sender.getName();

                NetworkManager.sendGETRequest(url, (response, error) -> {
                    if(error != null){
                        ChatManager.error("Failed to get points", sender);
                        return;
                    }

                    try {
                        // EntityUtils.toString(response.getEntity()) to JSON Object
                        JSONObject json = NetworkManager.parseJSON(response);
                        int points = Integer.parseInt(json.get("points").toString());
                        ChatManager.message(String.format("You have %d points", points), sender);
                    } catch (Exception e) {
                        ChatManager.error("Failed to get points", sender);
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