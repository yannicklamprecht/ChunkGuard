package de.ysl3000.chunkguard.events;

import org.bukkit.event.*;
import org.bukkit.*;

public class ChunkBuyFromServerEvent extends Event
{
    private static HandlerList handlerList;
    private OfflinePlayer newOwner;
    private Chunk chunk;
    
    public ChunkBuyFromServerEvent(final OfflinePlayer newOwner, final Chunk chunk) {
        this.newOwner = newOwner;
        this.chunk = chunk;
    }
    
    public HandlerList getHandlers() {
        return ChunkBuyFromServerEvent.handlerList;
    }
    
    public static HandlerList getHandlerList() {
        return ChunkBuyFromServerEvent.handlerList;
    }
    
    public OfflinePlayer getNewOwner() {
        return this.newOwner;
    }
    
    public Chunk getChunk() {
        return this.chunk;
    }
    
    static {
        ChunkBuyFromServerEvent.handlerList = new HandlerList();
    }
}
