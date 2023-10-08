package com.henhen1227.cccore.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public abstract class Event {
    protected String unique_id;

    public Event(String id){
        unique_id = id;
    }
    public abstract void start();

    public abstract void stop();

    public abstract PlayerInventory startingGear();

    public @NotNull TextComponent onJoinMessage(){
        return Component.text("You have joined the event: " + unique_id);
    }


    public void clearStats(){

    }

    public String getUniqueId() {
        return unique_id;
    }
}
