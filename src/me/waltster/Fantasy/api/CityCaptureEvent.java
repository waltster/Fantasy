package me.waltster.Fantasy.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.waltster.Fantasy.Race;

public final class CityCaptureEvent extends Event implements Cancellable{
    private static final HandlerList handlers = new HandlerList();
    private String cityName;
    private Race oldOwner;
    private Player capturer;
    private boolean canceled;
    
    public CityCaptureEvent(String cityName, Race oldOwner, Player capturer) {
        this.cityName = cityName;
        this.oldOwner = oldOwner;
        this.capturer = capturer;
    }

    public String getCityName() {
        return cityName;
    }

    public Race getOldOwner(){
        return oldOwner;
    }
    
    public Player getCapturer(){
        return capturer;
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean arg0) {
        this.canceled = arg0;
    }
}