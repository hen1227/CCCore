package com.henhen1227.cccore.networking;

import com.henhen1227.cccore.ChatManager;
import com.henhen1227.cccore.PurchaseManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

import static org.bukkit.Bukkit.getServer;

public class SocketListenerHandler implements Listener {

    private SocketListener socketListener;
    private final PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();

    private final URI serverURI;

    private int reconnectAttempts = 0;
    private boolean attemptingConnection = false;
    private boolean connected = false;


    public SocketListenerHandler() {
        try {
            serverURI = new URI(NetworkManager.socketServerIP);
            socketListener = new SocketListener(serverURI, this);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        socketListener.connect();
    }

    public void deliverPurchasedItems(JSONObject purchase, JSONObject item){
        World world = getServer().getWorlds().get(0);

        String itemUniqueId = (String) item.get("uniqueId");
        JSONObject location = (JSONObject) purchase.get("location");

        PurchaseManager.spawnChestWithItem(world, itemUniqueId, location, (x, y, z, isBoat) ->
            {
                JSONObject json = new JSONObject();
                json.put("origin", "minecraft");
                json.put("type", "purchaseCompleted");
                json.put("purchase", ((Number) purchase.get("id")).intValue());
//                JSONArray position = new JSONArray();
//                position.add(x);
//                position.add(y);
//                position.add(z);
//                json.put("position", position);
                json.put("isBoat", isBoat);


                Bukkit.getLogger().info("Completed A Purchase!");
                Bukkit.getLogger().info("Spawned Chest with items at ("+x+" "+y+" "+z+")");

                // Forward the chat message to the WebSocket server
                socketListener.send(json.toJSONString());
            }
        );
    }

    public void sendWebsocketMessage(String message, String type){
        JSONObject json = new JSONObject();
        json.put("origin", "minecraft");
        json.put("type", "chatMessage");
        json.put("message", message);

        Bukkit.getLogger().info("Attempting to send message: " + message);

        // Forward the chat message to the WebSocket server
        socketListener.send(json.toJSONString());
    }

    public void reattemptConnection(){
        reconnectAttempts = 0;
        attemptingConnection = false;
        connected = false;

        resetSocketListener();
    }

    // TODO: Fix this. It has concurrent connections.
    public void resetSocketListener(){
        socketListener.close();
        if (attemptingConnection) return;
        // Calculate the delay before the next reconnect attempt.
        long delay;
        switch (reconnectAttempts) {
            case 0:
                delay = 2000;
                break;
            case 1:
                delay = 5000;
                break;
            case 2:
                delay = 10000;
                break;
            case 3:
                delay = 30000;
                break;
            case 4:
                delay = 60000;
                break;
//            case 5:
//                delay = 300000;
//                break;
//            case 6:
//                delay = 600000;
//                break;
            default:
                return; // Stop trying to reconnect after 7 attempts.
        }

        // Increment the number of reconnect attempts.
        reconnectAttempts++;

        // Start a new thread that will try to reconnect to the server.
        new Thread(() -> {
            try {
                Thread.sleep(delay); // Wait before trying to reconnect.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            attemptingConnection = true;
            socketListener = new SocketListener(serverURI, this);
            socketListener.connect();
        }).start();
    }

    public void onOpen() {
        Bukkit.getLogger().info("Socket connection opened.");
        ChatManager.message("Minecraft Server is connected", "cccore.admin.errors");

        attemptingConnection = false;
        connected = true;
        reconnectAttempts = 0;
    }

    public void onClose(){
        Bukkit.getLogger().info("Socket connection closed.");
        ChatManager.message("Minecraft Server is disconnected", "cccore.admin.errors");

        attemptingConnection = false;
        connected = false;

        resetSocketListener();
    }

    // Bukkit Listeners
    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        if(socketListener.isConnected()) {

            String message = "<" + event.getPlayer().getName() + "> " + event.signedMessage().message();
            // Debug message to confirm receipt of chat event
            Bukkit.getLogger().info("Server chat event: " + message);

            sendWebsocketMessage(message, "chatMessage");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(socketListener.isConnected()) {
            String message = serializer.serialize(event.joinMessage());
            Bukkit.getLogger().info("Server event: " + event.getJoinMessage());
//            Bukkit.getLogger().info("Server event: " + message);
            sendWebsocketMessage(message, "playerJoined");
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if(socketListener.isConnected()) {
            String message = serializer.serialize(event.quitMessage());
            Bukkit.getLogger().info("Server event: " + message);
            sendWebsocketMessage(message, "playerJoin");// Same formatting as player join
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(socketListener.isConnected()) {
            String message = serializer.serialize(event.deathMessage());
            Bukkit.getLogger().info("Server chat event: " + message);
            sendWebsocketMessage(message, "playerDeath"); // Same formatting as player join
        }
    }

    @EventHandler
    public void onPlayerAchievement(PlayerAdvancementDoneEvent event) {
        if(socketListener.isConnected()) {
            String message = serializer.serialize(event.getAdvancement().displayName());
            Bukkit.getLogger().info("Server event: " + message);
            sendWebsocketMessage(message, "playerAchievement");
        }
    }

    public String status(){
        return socketListener.isConnected() ? "Connected" : "Disconnected";
    }
}