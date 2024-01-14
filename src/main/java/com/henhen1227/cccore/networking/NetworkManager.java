package com.henhen1227.cccore.networking;

import com.henhen1227.cccore.CCCore;
import com.henhen1227.cccore.ChatManager;
import com.henhen1227.cccore.PlayerJoin;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class NetworkManager {

//    public final static String socketServerIP = "ws://api.henhen1227.com/minecraftServer";
//    public final static String socketServerIP = "ws://10.31.64.43:4001/minecraftServer";
    public final static String socketServerIP = "ws://192.168.86.85:4001/minecraftServer";

//    public final static String baseURL = "https://api.henhen1227.com/minecraft";
//    public final static String baseURL = "http://10.31.64.43:4001/minecraft";
    public final static String baseURL = "http://192.168.86.85:4001/minecraft";

    private static SocketListenerHandler socketListenerHandler;

    // Number of website sockets listening to chat messages
    private static int numberOfSocketConnections = 0;

    public static void register(){
        socketListenerHandler = new SocketListenerHandler();

        Bukkit.getPluginManager().registerEvents(socketListenerHandler, CCCore.instance);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), CCCore.instance);
    }

    public static void resetWebsocketConnection(){
        socketListenerHandler.reattemptConnection();
    }

    public static void increaseSocketConnections(){
        numberOfSocketConnections++;
    }

    public static void decreaseSocketConnections(){
        numberOfSocketConnections--;
    }

    public static int getSocketConnections(){
        return numberOfSocketConnections;
    }

    public static void recordWin(Player player, String gameType){
        JSONObject data = new JSONObject();
        data.put("username", player.getName());
        data.put("gameType", gameType);

        NetworkManager.sendPOSTRequest("/gameWin", data, (responseCode, response) -> {
            Bukkit.getLogger().info("Response code: " + responseCode.getStatusLine().getStatusCode());
            if(responseCode.getStatusLine().getStatusCode() == 200){
                Bukkit.getLogger().info("Win recorded!");
                ChatManager.message(String.format("Win recorded of game '%s' for player %s", gameType, player.getName()), "cccore.admin.errors");
            } else {
                ChatManager.error("Error recording win!", "cccore.admin.errors");
            }
        });
    }

    public static void sendPOSTRequest(String url, JSONObject json, NetworkResponseCallback callback){
        try {
            String fullURL = baseURL + url;
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(fullURL);

            StringEntity entity = new StringEntity(json.toJSONString());
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("X-API-KEY", CCCore.apiKey);

            HttpResponse response = client.execute(httpPost);

            // Log status code:
            Bukkit.getLogger().info("Accessing URL: `" + fullURL+"`");
            Bukkit.getLogger().info(response.getStatusLine().toString());

            callback.networkResponse(response, null);

            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendGETRequest(String url, NetworkResponseCallback callback){
        try {
            String fullURL = baseURL + url;
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(fullURL);

            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            httpGet.setHeader("X-API-KEY", CCCore.apiKey);

            HttpResponse response = client.execute(httpGet);

            callback.networkResponse(response, null);

            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String status(){

        return socketListenerHandler.status();
    }

    public static JSONObject parseJSON(HttpResponse response) {
        try {
            JSONParser parser = new JSONParser();
            return (JSONObject) parser.parse(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void buyItem(Player player, String itemUniqueId, int price, BuyItemCallback callback){
        JSONObject data = new JSONObject();
        data.put("username", player.getName());
        data.put("itemUniqueId", itemUniqueId);
        data.put("price", price);

        NetworkManager.sendPOSTRequest("/buyItem", data, (responseCode, response) -> {
            if(responseCode.getStatusLine().getStatusCode() == 202){
                Bukkit.getLogger().info("Purchase made with insufficient points!");
                ChatManager.message(String.format("Item purchased for player %s", player.getName()), "cccore.admin.errors");
                callback.buyItemResponse("Insufficient points");
            } else if(responseCode.getStatusLine().getStatusCode() == 200){
                Bukkit.getLogger().info("Item purchased!");
                ChatManager.message(String.format("Item purchased for player %s", player.getName()), "cccore.admin.errors");
                callback.buyItemResponse(null);
            } else {
                ChatManager.error("Error purchasing item!", "cccore.admin.errors");
                callback.buyItemResponse("Error purchasing item");
            }
        });
    }

    public static void getPoints(Player player, PointsCallback callback){
        String url = "/points/"+player.getName();

        NetworkManager.sendGETRequest(url, (response, error) -> {
            if(error != null){
                ChatManager.error("Failed to get points", player);
                return;
            }

            try {
                // EntityUtils.toString(response.getEntity()) to JSON Object
                JSONObject json = NetworkManager.parseJSON(response);
                callback.pointsResponse(Integer.parseInt(json.get("points").toString()), null);

            } catch (Exception e) {
                callback.pointsResponse(0, "Failed to get points");
            }
        });
    }
}
