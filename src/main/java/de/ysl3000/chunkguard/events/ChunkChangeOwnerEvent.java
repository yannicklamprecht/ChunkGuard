package de.ysl3000.chunkguard.events;

import org.bukkit.event.*;
import org.bukkit.*;

public class ChunkChangeOwnerEvent extends Event
{
    private static HandlerList handlerList;
    private OfflinePlayer lastOwner;
    private OfflinePlayer newOwner;
    private Chunk chunk;
    private double prize;
    
    public ChunkChangeOwnerEvent(final OfflinePlayer lastOwner, final OfflinePlayer newOwner, final Chunk chunk, final double prize) {
        this.lastOwner = lastOwner;
        this.newOwner = newOwner;
        this.chunk = chunk;
        this.prize = prize;
    }
    
    public HandlerList getHandlers() {
        return ChunkChangeOwnerEvent.handlerList;
    }
    
    public static HandlerList getHandlerList() {
        return ChunkChangeOwnerEvent.handlerList;
    }
    
    public OfflinePlayer getLastOwner() {
        return this.lastOwner;
    }
    
    public OfflinePlayer getNewOwner() {
        return this.newOwner;
    }
    
    public Chunk getChunk() {
        return this.chunk;
    }
    
    public double getPrize() {
        return this.prize;
    }
    
    static {
        ChunkChangeOwnerEvent.handlerList = new HandlerList();
    }
}
