package com.henhen1227.cccore;

import org.bukkit.Bukkit;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.net.URISyntaxException;

public class ChatListener extends WebSocketClient {

    public ChatListener(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("Connected to chat server");
    }

    @Override
    public void onMessage(String message) {
        // Broadcast the incoming message to the server
        Bukkit.getServer().broadcastMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from chat server");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}