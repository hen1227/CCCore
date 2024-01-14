package com.henhen1227.cccore.competitions;

import com.henhen1227.cccore.CCCore;
import com.henhen1227.cccore.ChatManager;
import com.henhen1227.cccore.commands.PointsCommand;
import com.henhen1227.cccore.networking.NetworkManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

public class CompetitionManager {
    private static final ArrayList<Competition> competitions = new ArrayList<>();
    private static Competition activeCompetition;
    public static CompetitionScoreManager competitionScoreManager;

    public static void registerCompetitions() {
        competitionScoreManager = new CompetitionScoreManager();

        competitions.add(new PhantomKillsCompetition());
//        competitions.add(new BreakIronAxesCompetition());
        competitions.add(new PumpkinsBrokenCompetition());

        for (Competition competition : competitions) {
            Bukkit.getPluginManager().registerEvents(competition, CCCore.instance);
        }

        loadCurrentCompetition();
//        registerCompetitionScheduler();
    }

    public static void startRandomCompetition() {
        Random rand = new Random();
        int randomIndex = rand.nextInt(competitions.size());
        startCompetition(competitions.get(randomIndex).getUniqueId());
    }

    public static boolean startCompetition(String gameName){
        activeCompetition = getCompetitionById(gameName);

        if(activeCompetition != null) {
            ChatManager.titleMessage("Competition Started!", activeCompetition.getDescription());
            activeCompetition.active = true;
            activeCompetition.onCompetitionStart();
            competitionScoreManager.resetScores();
            return true;
        }

        return false;
    }

    public static Competition getActiveCompetition(){
        return activeCompetition;
    }

    public static void endCompetition(){
        if(activeCompetition == null) {
            ChatManager.message("Attempted to end competition when none was active!", "cccore.admin.errors");
            return;
        }

        List<Player> winners = competitionScoreManager.getTopThreePlayer();
        activeCompetition.onCompetitionEnd(winners);


        if (!winners.isEmpty()) {
            PointsCommand.awardPoints(150, winners.get(0).getName());
            NetworkManager.recordWin(winners.get(0), activeCompetition.getUniqueId());
            ChatManager.titleMessage("Competition Ended!", winners.get(0).getName() + " won the competition!");
            ChatManager.message(String.format("%s won the competition and has been awarded 150 points!", winners.get(0).getName()));
        }
        if (winners.size() > 1) {
            PointsCommand.awardPoints(100, winners.get(1).getName());
            ChatManager.message(String.format("%s came in second place and won 100 points!", winners.get(1).getName()));
        }
        if (winners.size() > 2) {
            PointsCommand.awardPoints(50, winners.get(2).getName());
            ChatManager.message(String.format("%s came in second place and won 50 points!", winners.get(2).getName()));
        }

        Map<String, Integer> allScores = competitionScoreManager.getAllScores();

        StringBuilder finalStandings = new StringBuilder("Final Standings:\n");
        for (int i = 0; i < allScores.size(); i++) {
            finalStandings.append(String.format("%d. %s - %d\n", i + 1, allScores.keySet().toArray()[i], (Integer) allScores.values().toArray()[i]));
        }
        ChatManager.message(finalStandings.toString());

        activeCompetition.active = false;
        activeCompetition = null;

        clearCompetitionConfig();
    }


    public static Competition getCompetitionById(String competitionId) {
        for (Competition competition : competitions) {
            if (competition.getUniqueId().equals(competitionId)) {
                return competition;
            }
        }
        return null;
    }

    public static void setCurrentCompetition(Competition competition) {
        activeCompetition = competition;
        activeCompetition.active = true;
    }

    public static void registerCompetitionScheduler() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CCCore.instance, new Runnable() {
            @Override
            public void run() {
                // Check the current time
                LocalDateTime now = LocalDateTime.now();
                if (now.getDayOfWeek() == DayOfWeek.SUNDAY && now.getHour() == 13) {
                    // It's 1:00 PM on a Sunday, end the competition
                    endCompetition();
                }
            }
        }, 0L, 1200L); // Check every 5 minutes
    }

    public static void saveCurrentCompetition() {
        if (activeCompetition != null) {
            // Save the unique ID of the competition
            CCCore.instance.getConfig().set("currentCompetition", activeCompetition.getUniqueId());

            // Save the scores
            Map<String, Integer> scores = competitionScoreManager.getAllScores();
            for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                CCCore.instance.getConfig().set("competitionScores." + getOfflinePlayerUUID(entry.getKey()), entry.getValue());
            }

            // Save the configuration to disk
            CCCore.instance.saveConfig();
        }
    }

    private static void loadCurrentCompetition() {
        String competitionName = CCCore.instance.getConfig().getString("currentCompetition");
        if (competitionName != null) {
            Competition competition = getCompetitionById(competitionName);
            if (competition != null) {
                setCurrentCompetition(competition);

                // Load the scores
                ConfigurationSection scoresSection = CCCore.instance.getConfig().getConfigurationSection("competitionScores");
                if (scoresSection != null) {
                    for (String key : scoresSection.getKeys(false)) {
                        UUID playerId = UUID.fromString(key);
                        int score = scoresSection.getInt(key);
                        String player = Bukkit.getOfflinePlayer(playerId).getName();
                        Bukkit.getLogger().info("Player: " + player);
                        Bukkit.getLogger().info("Score: " + score);
                        if (player != null) {
                            competitionScoreManager.setScore(player, score);
                        }
                    }
                }

                competition.onCompetitionResume();
            }
        }
    }

    public static UUID getOfflinePlayerUUID(String playerName) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        return offlinePlayer.getUniqueId();
    }

    private static void clearCompetitionConfig() {
        CCCore.instance.getConfig().set("currentCompetition", null);
        CCCore.instance.getConfig().set("competitionScores", null);
        CCCore.instance.saveConfig();
    }
}
