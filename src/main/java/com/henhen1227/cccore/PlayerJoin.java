package com.henhen1227.cccore;


import com.henhen1227.cccore.competitions.CompetitionManager;
import com.henhen1227.cccore.networking.NetworkManager;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.simple.JSONObject;

import java.io.IOException;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        String playerName = event.getPlayer().getName();
        String playerUUID = event.getPlayer().getUniqueId().toString();
        boolean isOp = event.getPlayer().isOp();

        String url = "/playerJoined";
        JSONObject json = new JSONObject();
        json.put("username", playerName);
        json.put("uuid", playerUUID);
        json.put("isOp", isOp);

        NetworkManager.sendPOSTRequest(url, json, (response, error) -> {
            if(error != null){
                Bukkit.getLogger().info("Failed to verify your CCCore account");
                ChatManager.error("Failed to verify your CCCore account", event.getPlayer());
            }
            try {
                String message = EntityUtils.toString(response.getEntity(), "UTF-8");
                Bukkit.getLogger().info(message);
                ChatManager.message(message, event.getPlayer());
                if(CompetitionManager.getActiveCompetition() != null)
                    ChatManager.message("A competition is active! Do /competition for more details.", event.getPlayer());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}