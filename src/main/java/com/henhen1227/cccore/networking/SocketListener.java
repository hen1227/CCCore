package com.henhen1227.cccore.networking;

import com.henhen1227.cccore.CCCore;
import com.henhen1227.cccore.ChatManager;
import org.bukkit.Bukkit;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SocketListener extends WebSocketClient {

    public SocketListenerHandler handler;
    private final URI serverURI;

    public SocketListener(URI serverURI, SocketListenerHandler socketListenerHandler) {
        super(serverURI, new Draft_6455(), getHeaders(), 0);
        this.handler = socketListenerHandler;
        this.serverURI = serverURI;
    }

    private static Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-API-KEY", CCCore.apiKey);
        return headers;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        this.handler.onOpen();
    }

    @Override
    public void onMessage(String message) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(message);

            Object type = json.get("type");
            if (type.equals("chatMessage")) {
                ChatManager.message((String) json.get("message"), true);
            } else if (type.equals("purchase")) {
                Bukkit.getLogger().info("Purchase request has been made!");
//                Bukkit.getLogger().info(json.toJSONString());
                this.handler.deliverPurchasedItems((JSONObject) json.get("purchase"), (JSONObject) json.get("item"));
            }else{
                Bukkit.getLogger().info("Unknown message type: " + type);
            }
        } catch (ParseException e) {
            Bukkit.getLogger().info("Error parsing message from Websocket" + e);
        } catch (ClassCastException e) {
            Bukkit.getLogger().info("Error casting message data from Websocket" + e);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // Handle disconnection.
        Bukkit.getLogger().info("Disconnected from the server. Code: " + code + ". Reason: " + reason + ". Remote: " + remote);
        ChatManager.warning("Disconnected from Websocket!", "cccore.admin.errors");

        // Try to reconnect to the server.
//        SocketListener socketListener = new SocketListener(this.serverURI, this.handler);
//        socketListener.connect();
         this.handler.onClose();
    }

    @Override
    public void onError(Exception ex) {
        Bukkit.getLogger().warning("An error occurred: " + ex.getMessage());
        Bukkit.getLogger().warning("An stacktrace: " + Arrays.toString(ex.getStackTrace()));
//         Handle error.
//        ChatManager.error("Websocket had an issue!");
        ChatManager.error(ex.getMessage(), "cccore.admin.errors");

        this.handler.onClose();
    }

    public boolean isConnected() {
        return this.isOpen();
    }
}