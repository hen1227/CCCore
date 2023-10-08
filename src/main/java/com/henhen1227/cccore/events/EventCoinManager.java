package com.henhen1227.cccore.events;

import com.henhen1227.cccore.CCCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class EventCoinManager {

    private final Scoreboard scoreboard;
    private final Objective objective;

    public EventCoinManager(){
        Bukkit.getLogger().info("Loading Coin Manager...");
        Bukkit.getLogger().info("Scoreboard Manager: " + CCCore.scoreboardManager);
        scoreboard = CCCore.scoreboardManager.getNewScoreboard();
        objective = scoreboard.registerNewObjective("coins", "dummy", "Coins");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Add title to scoreboard
        objective.setDisplayName("Zombie Apocalypse");
    }

    public void addCoins(Player player, int amount) {
        Score score = objective.getScore(player.getName()); // Retrieve the score for the player
        int newAmount = score.getScore() + amount;
        score.setScore(newAmount); // Increment the score by the given amount

        player.setLevel(newAmount);
    }

    public void removeCoins(Player player, int amount) {
        Score score = objective.getScore(player.getName());
        int newAmount = score.getScore() - amount;
        score.setScore(newAmount); // Decrement the score by the given amount

        player.setLevel(newAmount);
    }

    public void removeHalfCoins(Player player) {
        Score score = objective.getScore(player.getName());
        int newAmount = score.getScore() / 2;
        score.setScore(newAmount); // Decrement the score by the given amount

        player.setLevel(newAmount);
    }

    public int getCoins(Player player) {
        return objective.getScore(player.getName()).getScore(); // Retrieve the current coin count
    }

    public void setPlayerXPToCoins(Player player) {
        player.setLevel(getCoins(player));
    }

    public void showScoreboardToPlayer(Player player) {
        player.setScoreboard(scoreboard);
    }
}
