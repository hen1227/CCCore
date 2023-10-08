package com.henhen1227.cccore;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ChatManager {

    // Standard Messages
    public static void message(TextComponent message){
        Bukkit.getServer().broadcast(message);
    }
    public static void message(String message){
        Bukkit.getServer().broadcast(textComponent(message));
    }
    public static void message(String message, @NotNull Player target){
        target.sendMessage(textComponent(message));
    }
    public static void message(String message, @NotNull CommandSender target){
        target.sendMessage(textComponent(message));
    }
    public static void message(String message, boolean webPrefix){
        Bukkit.getServer().broadcast(textComponent(message, webPrefix));
    }
    public static void message(String message, String permission){
        Bukkit.getServer().broadcast(textComponent(message), permission);
//        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
//            if (player.hasPermission(permission)) {
//                player.sendMessage(textComponent(message));
//            }
//        }
    }

    // Warnings
    public static void warning(String message){
        Bukkit.getServer().broadcast(warningComponent(message));
    }

    public static void warning(String message, @NotNull Player target){
        target.sendMessage(warningComponent(message));
    }

    public static void warning(String message, String permission) {
        Bukkit.getServer().broadcast(warningComponent(message), permission);
//        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
//            if (player.hasPermission(permission)) {
//                player.sendMessage(warningComponent(message));
//            }
//        }
    }

    // Errors
    public static void error(String message){
        Bukkit.getServer().broadcast(errorComponent(message));
    }
    public static void error(String message, @NotNull Player target){
        target.sendMessage(errorComponent(message));
    }
    public static void error(String message, @NotNull CommandSender target){
        target.sendMessage(errorComponent(message));
    }
    public static void error(String message, String permission){
        Bukkit.getServer().broadcast(errorComponent(message), permission);
//        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
//            if (player.hasPermission(permission)) {
//                player.sendMessage(errorComponent(message));
//            }
//        }
    }

    public static void actionMessage(String message){
        Bukkit.getServer().sendActionBar(actionComponent(message));
    }

    // Formatting Components
    private static @NotNull Component textComponent(String text){
        return textComponent(text, false);
    }
    private static @NotNull Component textComponent(String text, boolean webPrefix){
        if (webPrefix)
            return Component.text("").color(NamedTextColor.DARK_GREEN)
                    .append(Component.text("[Web] ").color(NamedTextColor.YELLOW))
                    .append(Component.text(text).color(NamedTextColor.WHITE));
        return Component.text("").color(NamedTextColor.DARK_GREEN)
                .append(Component.text(text).color(NamedTextColor.WHITE));
    }
    private static @NotNull Component warningComponent(String text){
        return Component.text("[CCCore]").color(NamedTextColor.DARK_GREEN)
                .append(Component.text("[Warning] ").color(NamedTextColor.YELLOW))
                .append(Component.text(text).color(NamedTextColor.WHITE));
    }
    private static @NotNull Component errorComponent(String text){
        return Component.text("[CCCore]").color(NamedTextColor.DARK_GREEN)
                .append(Component.text("[Error] ").color(NamedTextColor.RED))
                .append(Component.text(text).color(NamedTextColor.WHITE));
    }

    private static @NotNull Component actionComponent(String text){
        return Component.text(text).color(NamedTextColor.WHITE);
    }
}
