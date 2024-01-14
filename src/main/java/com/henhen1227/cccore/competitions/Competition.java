package com.henhen1227.cccore.competitions;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;

public abstract class Competition implements Listener {
    protected static String uniqueId;
    protected static String description;

    public boolean active = true;

    public Competition(String gameName, String desc) {
        uniqueId = gameName;
        description = desc;
    }

    public void incrementPoints(Player player, int amount) {
        CompetitionManager.competitionScoreManager.addPoints(player.getName(), amount);
    }

    public void onCompetitionStart() {
        // Optional Override this method
        active = true;
    }

    public void onCompetitionResume() {
        // Optional Override this method
    }

    public void onCompetitionEnd(List<Player> winners) {
        // Optional Override this method
    }

    public String getUniqueId() {
        return uniqueId;
    }
    public String getDescription() {
        return description;
    }
}
