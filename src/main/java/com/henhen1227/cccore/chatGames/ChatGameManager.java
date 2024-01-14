package com.henhen1227.cccore.chatGames;

import com.henhen1227.cccore.CCCore;
import com.henhen1227.cccore.ChatManager;
import com.henhen1227.cccore.networking.NetworkManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class ChatGameManager implements Listener {
    private static final ArrayList<ChatGame> games = new ArrayList<>();
    private static ChatGame activeGame;

    public static void registerGames() {
        games.add(new WordUnscramble());
        games.add(new QuickMath());
        // Add other games here

        startRandomGameTimer();
    }

    public static void startRandomGame() {
        Random rand = new Random();
        int randomIndex = rand.nextInt(games.size());

        activeGame = games.get(randomIndex);
        activeGame.startGame();
    }

    public static boolean startGame(String gameName){
        for (ChatGame game : games) {
            if (game.getUniqueId().equals(gameName)) {
                activeGame = game;
                activeGame.startGame();
                return true;
            }
        }
        return false;
    }

    //Every 20-40 minutes start a random chat game
    public static void startRandomGameTimer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CCCore.instance, () -> {
            // At least 3 players online
            if(activeGame == null && Bukkit.getOnlinePlayers().size() >= 3)
                startRandomGame();

        }, 30*60*20, 12000);
    }

    public static ChatGame getActiveGame(){
        return activeGame;
    }

    public static void endGame(){
//        ChatManager.message("Game ended!");
        activeGame.active = false;
        activeGame = null;
    }

    // Register this class as a Listener to listen for AsyncPlayerChatEvent
    public static void registerListener() {
        Bukkit.getPluginManager().registerEvents(new ChatGameManager(), CCCore.instance);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        if(activeGame == null) return;
        String guess = ((TextComponent) event.message()).content();
        // If a game is active, pass the chat message to the game
        activeGame.handleResponse(event.getPlayer(), guess);
    }
}