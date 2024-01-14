package com.henhen1227.cccore.commands;

import com.henhen1227.cccore.ChatManager;
import com.henhen1227.cccore.competitions.Competition;
import com.henhen1227.cccore.competitions.CompetitionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CompetitionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(command.getName().equalsIgnoreCase("competition")) {
            Competition currentCompetition = CompetitionManager.getActiveCompetition();
            if (currentCompetition == null) {
                ChatManager.message("There is no active competition.", sender);
                return true;
            }

            StringBuilder message = new StringBuilder(currentCompetition.getDescription() + "\n");
            Map<String, Integer> scoreMap = CompetitionManager.competitionScoreManager.getAllScores();
//            message.append("Competition Scores:\n");
            for (Map.Entry<String, Integer> entry : scoreMap.entrySet()) {
                message.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }

            ChatManager.message(message.toString(), sender);

            return true;
        }else if (command.getName().equalsIgnoreCase("startcompetition") && sender.hasPermission("cccore.admin.use")) {
            if (args.length == 0) {
                ChatManager.message("Please specify a competition to start.", sender);
                return true;
            }

            if (CompetitionManager.getActiveCompetition() != null) {
                ChatManager.message("There is already an active competition.", sender);
                return true;
            }

            if (CompetitionManager.startCompetition(args[0])) {
                ChatManager.message("Started competition: " + args[0], sender);
                return true;
            } else {
                ChatManager.message("Could not find competition: " + args[0], sender);
                return true;
            }
        }else if (command.getName().equalsIgnoreCase("endcompetition") && sender.hasPermission("cccore.admin.use")) {
            if (CompetitionManager.getActiveCompetition() == null) {
                ChatManager.message("There is no active competition.", sender);
                return true;
            }

            CompetitionManager.endCompetition();
            ChatManager.message("Ended competition.", sender);
            return true;
        }

        return false;
    }
}
