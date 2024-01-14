package com.henhen1227.cccore.commands;

import com.henhen1227.cccore.uis.UIManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("shop") && sender.hasPermission("cccore.shop.use")) {
            if(sender instanceof Player player) {
                UIManager.openUI(player, "shop");
            }
            return true;
        }

        return false;
    }
}