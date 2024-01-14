package com.henhen1227.cccore;

import com.henhen1227.cccore.chatGames.ChatGameManager;
import com.henhen1227.cccore.commands.CommandManager;
import com.henhen1227.cccore.competitions.CompetitionManager;
import com.henhen1227.cccore.events.EventManager;
import com.henhen1227.cccore.items.MagicItemManager;
import com.henhen1227.cccore.networking.NetworkManager;
import com.henhen1227.cccore.uis.UIManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

public final class CCCore extends JavaPlugin implements Listener {

    public static CCCore instance;
    public static ScoreboardManager scoreboardManager;

    public static String apiKey;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        apiKey = this.getConfig().getString("api-key");
        this.getLogger().info("Hello from Chicken Craft Core! :D");

        // Load on first game tick
        Bukkit.getScheduler().runTask(this, () -> {
            scoreboardManager = Bukkit.getScoreboardManager();
            EventManager.registerCoinManager();

            // Competitions
            // TODO: ADD ERROR CATCHING TO LACK OF SCOREBOARD BEING SETUP
            CompetitionManager.registerCompetitions(); // <-- requires scoreboard to be loaded
        });

        //Networking
        NetworkManager.register();

        // Commands
        CommandManager.registerCommands();

        // Magic Items
        MagicItemManager.registerEvents(Bukkit.getPluginManager());
        MagicItemManager.initializeListeners();

        // UIs
        UIManager.registerUIs();

        // Events
//        EventManager.registerListener();
        EventManager.registerEvents();
        EventManager.registerEventScheduler();

        // Chat Games
        ChatGameManager.registerListener();
        ChatGameManager.registerGames();

    }

    @Override
    public void onDisable() {
        CompetitionManager.saveCurrentCompetition();
    }
}
