package com.henhen1227.cccore.commands;

import com.henhen1227.cccore.CCCore;

public class CommandManager {
    public static void registerCommands(){
        CCCore cccore = CCCore.instance;

        cccore.getCommand("points").setExecutor(new PointsCommand());
        cccore.getCommand("reloadws").setExecutor(new NetworkCommand());
        cccore.getCommand("cccore").setExecutor(new CCCoreCommand());
        cccore.getCommand("startchatgame").setExecutor(new StartChatGameCommand());
        cccore.getCommand("startevent").setExecutor(new StartEventCommand());
        cccore.getCommand("leave").setExecutor(new LeaveCommand());
    }
}
