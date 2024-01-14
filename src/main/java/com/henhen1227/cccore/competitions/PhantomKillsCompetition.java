package com.henhen1227.cccore.competitions;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

public class PhantomKillsCompetition extends Competition {

    public static String uniqueId = "phantom_kills_competition";
    public static String description = "Kill the most Phantoms to win!";

    public PhantomKillsCompetition() {
        super(uniqueId, description);
    }

    @EventHandler
    public void onPhantomKill(EntityDeathEvent event) {
        if (!active) return;
        if (event.getEntity().getType() != EntityType.PHANTOM) return;
        if (event.getEntity().getKiller() == null) return;

        incrementPoints(event.getEntity().getKiller(), 1);
    }
}
