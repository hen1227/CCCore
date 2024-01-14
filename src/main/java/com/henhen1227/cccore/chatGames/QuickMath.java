package com.henhen1227.cccore.chatGames;

import com.henhen1227.cccore.CCCore;
import com.henhen1227.cccore.ChatManager;
import com.henhen1227.cccore.commands.PointsCommand;
import com.henhen1227.cccore.networking.NetworkManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class QuickMath extends ChatGame {
    private String solution;
    private FileConfiguration wordConfig;

    public QuickMath() {
        super("QuickMath");
        victoryPoints = 10;
    }

    @Override
    public void handleResponse(Player player, String message) {
        if (message.equalsIgnoreCase(solution)) {  // word is the current scrambled word
            // Player guessed the word correctly, reward them and end the game
            endGame(player);
        }
    }

    @Override
    public void startGame() {
        active = true;

        int rand = new Random().nextInt(4);
        // Random math problem
        String question;
        if(rand == 0) {
            int y = new Random().nextInt(20);
            int solutionValue = new Random().nextInt(20);
            int x = solutionValue * y;
            solution = String.valueOf(solutionValue);
            question = String.valueOf(x) + " / " + String.valueOf(y);
        }
        else if(rand  == 1) {
            int x = new Random().nextInt(250) + 250;
            int y = new Random().nextInt(250);
            solution = String.valueOf(x - y);
            question = String.valueOf(x) + " - " + String.valueOf(y);
        }
        else if(rand == 2) {
            int x = new Random().nextInt(20);
            int y = new Random().nextInt(20);
            solution = String.valueOf(x * y);
            question = String.valueOf(x) + " * " + String.valueOf(y);
        }
        else {
            int x = new Random().nextInt(1000);
            int y = new Random().nextInt(1000);
            solution = String.valueOf(x + y);
            question = String.valueOf(x) + " + " + String.valueOf(y);
        }

        // Send the scrambled word to chat and start listening for player responses
        // This logic will depend on your specific server setup
        Bukkit.getLogger().info("Question: " + question);
        Bukkit.getLogger().info("Answer" + solution);

        TextComponent message = Component.text("[CCCore] ").color(NamedTextColor.DARK_GREEN)
                .append(Component.text("First person so solve: ").color(NamedTextColor.WHITE))
                .append(Component.text(question).color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(" wins!").color(NamedTextColor.WHITE));

        ChatManager.message(message);

        // Start a timer to end the game after 300 seconds
        Bukkit.getScheduler().runTaskLater(CCCore.instance, () -> {
            if (active) {
                ChatManager.message("Time's up!");
                endGame(null);
            }
        }, 20 * 300);
    }

    public void endGame(Player winner) {
        if (winner == null) {
            ChatManager.message("No one guessed \"" + solution + "\" in time!");
            ChatGameManager.endGame();
            return;
        }

        active = false;

        // Reward the player and clear the active game
        PointsCommand.awardPoints(5, winner.getName());
        NetworkManager.recordWin(winner, getUniqueId());
        ChatGameManager.endGame();
    }
}
