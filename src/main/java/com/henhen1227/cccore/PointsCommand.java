package com.henhen1227.cccore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PointsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("points")) {

            if (args.length < 3 || !args[0].equalsIgnoreCase("award")) {
                sender.sendMessage("Invalid usage. Usage: /points award <player> <amount>");
                return false;
            }

            String playerName = args[1];
            int points = Integer.parseInt(args[2]);

            try {
                URL url = new URL("http://localhost:3000/points");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                String jsonInputString = String.format("{\"username\": \"%s\", \"points\": \"%d\"}", playerName, points);

                try(OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                conn.connect();
                int responseCode = conn.getResponseCode();

                if (responseCode != 200) {
                    throw new RuntimeException("HttpResponseCode: " + responseCode);
                }

                sender.sendMessage(String.format("Awarded %d points to %s", points, playerName));
            } catch (Exception e) {
                e.printStackTrace();
                sender.sendMessage("Failed to award points");
            }
        }

        return true;
    }
}