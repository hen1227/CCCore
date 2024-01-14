package com.henhen1227.cccore.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Event {
    protected String unique_id;

    public Event(String id){
        unique_id = id;
    }
    public abstract void start();

    public abstract void stop();

    public abstract void setStartingGear(Player player);

    public @NotNull TextComponent onJoinMessage(){
        return Component.text("You have joined the event: " + unique_id);
    }


    public void clearStats(){

    }

    public String getUniqueId() {
        return unique_id;
    }

    public boolean isInEventWorld(Player player){
        return player.getWorld().getName().equals(getUniqueId()) || player.getWorld().getName().equals(getUniqueId() + "_nether") || player.getWorld().getName().equals(getUniqueId() + "_the_end");
    }

    public boolean isEventWorld(World world){
        return world.getName().equals(getUniqueId()) || world.getName().equals(getUniqueId() + "_nether") || world.getName().equals(getUniqueId() + "_the_end");
    }
}
