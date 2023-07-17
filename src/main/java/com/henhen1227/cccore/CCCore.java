package com.henhen1227.cccore;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;



public final class CCCore extends JavaPlugin implements Listener {

    private ChatListener chatClient;

    @Override
    public void onEnable() {
        Logger log = this.getLogger();

        // Print startup message
        log.info("Chicken Craft Core has been enabled!");

        // Plugin startup logic
        this.getCommand("points").setExecutor(new PointsCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);


        // Initialize the WebSocket client
        try {
            chatClient = new ChatListener(new URI("ws://192.168.40.50:2555"));
            chatClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // Initialize the chat event handler
        ChatListenerHandler chatEventHandler = new ChatListenerHandler(chatClient);

        // Register events
        Bukkit.getPluginManager().registerEvents(chatEventHandler, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        // Forward the chat message to the WebSocket server
        chatClient.send(event.getMessage());
    }
}
