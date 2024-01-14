package com.henhen1227.cccore.competitions;

import com.henhen1227.cccore.events.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public class CompetitionScoreManager {

    private final Scoreboard scoreboard;
    private final Objective objective;

    public CompetitionScoreManager() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        this.scoreboard = manager.getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("competition", "dummy", "Competition Points");
        this.objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
    }

    public void addPoints(String playerName, int points) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) return;
        if(EventManager.isInEventWorld(player)) return;
        Score score = objective.getScore(playerName);
        score.setScore(score.getScore() + points);
    }

    public void resetScores() {
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }
    }

    public void setScore(String playerName, int score) {
        objective.getScore(playerName).setScore(score);
    }

    public List<Player> getTopThreePlayer(){
        // Get the top three of the player's scores
        List<Player> topThree = new ArrayList<>();

        for(int i = 0; i < 3; i++){
            String topPlayer = null;
            int topScore = 0;

            for(String entry : scoreboard.getEntries()){
                Score score = objective.getScore(entry);

                if(score.getScore() > topScore && !topThree.contains(Bukkit.getPlayer(entry))){
                    topPlayer = entry;
                    topScore = score.getScore();
                }
            }

            if(topPlayer != null){
                topThree.add(Bukkit.getPlayer(topPlayer));
            }
        }

        return topThree;
    }

    public Map<String, Integer> getAllScores(){
        // Get all player's scores in descending order
        Map<String, Integer> allPlayers = new HashMap<>();

        for(String entry : scoreboard.getEntries()){
            Score score = objective.getScore(entry);
//            Player player = Bukkit.getPlayer(entry);

            allPlayers.put(entry, score.getScore());
        }

        return allPlayers;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}
