package com.henhen1227.cccore.uis;

import com.henhen1227.cccore.CCCore;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;

public class TraderClickListener implements Listener {

    @EventHandler
    public void onVillagerClick(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Villager) {
            // This code will run when a player interacts with a villager
            Villager villager = (Villager) event.getRightClicked();
            Player player = event.getPlayer();

            NamespacedKey key = new NamespacedKey(CCCore.instance, "customVillager");
            NamespacedKey villagerType = new NamespacedKey(CCCore.instance, "villagerType");
            if(villager.getPersistentDataContainer().has(key, PersistentDataType.BOOLEAN)) {
                if (Boolean.TRUE.equals(villager.getPersistentDataContainer().get(key, PersistentDataType.BOOLEAN))) {
                    event.setCancelled(true);

                    if (villager.getPersistentDataContainer().has(villagerType, PersistentDataType.STRING)) {
                        String type = villager.getPersistentDataContainer().get(villagerType, PersistentDataType.STRING);
                        assert type != null;
                        if (type.equals("shop")) {
                            UIManager.openUI(player, "shop");
                        }
                    }
                }
            }
        }
    }
}
