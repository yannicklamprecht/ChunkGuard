package de.ysl3000.chunkguard.events;

import org.bukkit.event.*;
import org.bukkit.*;

public class ChunkRegionCreateEvent extends Event
{
    private static HandlerList handlerList;
    private Chunk chunk;
    
    public HandlerList getHandlers() {
        return ChunkRegionCreateEvent.handlerList;
    }
    
    public static HandlerList getHandlerList() {
        return ChunkRegionCreateEvent.handlerList;
    }
    
    public ChunkRegionCreateEvent(final Chunk chunk) {
        this.chunk = chunk;
    }
    
    public Chunk getChunk() {
        return this.chunk;
    }
    
    static {
        ChunkRegionCreateEvent.handlerList = new HandlerList();
    }
}
