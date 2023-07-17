package com.henhen1227.cccore;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class ChatListenerHandler implements Listener {

    private ChatListener chatClient;

    public ChatListenerHandler(ChatListener chatClient) {
        this.chatClient = chatClient;
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        // Forward the chat message to the WebSocket server
        chatClient.send(event.getMessage());
    }
}