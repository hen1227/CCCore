package com.henhen1227.cccore;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        String playerName = event.getPlayer().getName();
        Gson gson = new Gson();

        try {
            URL url = new URL("http://192.168.40.50:3000/generateVerificationToken");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; utf-8");

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("username", playerName);

            try(OutputStream os = conn.getOutputStream(); OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8")) {
                gson.toJson(jsonObject, osw);
                osw.flush();
            }

            conn.connect();
            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            }

            // Parse the response to get the verification token
            JsonObject jsonResponse = gson.fromJson(new Scanner(conn.getInputStream(), "UTF-8").useDelimiter("\\A").next(), JsonObject.class);
            String token = jsonResponse.get("token").getAsString();

            // Construct the verification link
            String verificationLink = "http://192.168.40.50:3000/verify?username=" + playerName + "&token=" + token;

            // Send the verification link to the player
            event.getPlayer().sendMessage("Please confirm your account by clicking on the following link: " + verificationLink);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}