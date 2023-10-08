package com.henhen1227.cccore.chatGames;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class ChatGame {
    protected String uniqueId;

    public boolean active = true;

    protected int victoryPoints = 0;

    public ChatGame(String gameName) {
        this.uniqueId = gameName;
    }

    public abstract void startGame();

    public abstract void endGame(Player winner);

    public String getUniqueId() {
        return uniqueId;
    }

    public abstract void handleResponse(Player player, String message);
}
