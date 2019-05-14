package de.ysl3000.chunkguard.events;

import org.bukkit.event.*;
import org.bukkit.*;

public class MiniMapUpdateEvent extends Event
{
    private static HandlerList handlerList;
    private Chunk chunk;
    
    public MiniMapUpdateEvent(final Chunk chunk) {
        this.chunk = chunk;
    }
    
    public HandlerList getHandlers() {
        return MiniMapUpdateEvent.handlerList;
    }
    
    public HandlerList getHandlerList() {
        return MiniMapUpdateEvent.handlerList;
    }
    
    static {
        MiniMapUpdateEvent.handlerList = new HandlerList();
    }
}
