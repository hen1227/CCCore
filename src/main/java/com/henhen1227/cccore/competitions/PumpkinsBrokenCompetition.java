package com.henhen1227.cccore.competitions;

import io.papermc.paper.event.block.BlockBreakBlockEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;

public class PumpkinsBrokenCompetition extends Competition {

    public static String uniqueId = "pumpkin_break_competition";
    public static String description = "Break the most pumpkins to win!";

    public PumpkinsBrokenCompetition() {
        super(uniqueId, description);
    }

    @EventHandler
    public void onPumpkinBroken(BlockBreakEvent event) {
        if (!active) return;
        if (event.getBlock().getType() != Material.PUMPKIN) return;
        if (event.getPlayer() == null) return;

        incrementPoints(event.getPlayer(), 1);
    }
}
