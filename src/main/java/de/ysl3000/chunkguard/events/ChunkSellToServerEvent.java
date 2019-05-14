package de.ysl3000.chunkguard.events;

import org.bukkit.event.*;
import org.bukkit.*;

public class ChunkSellToServerEvent extends Event
{
    private static HandlerList handlerList;
    private Chunk chunk;
    private OfflinePlayer lastOwner;
    
    public ChunkSellToServerEvent(final OfflinePlayer lastOwner, final Chunk chunk) {
        this.lastOwner = lastOwner;
        this.chunk = chunk;
    }
    
    public HandlerList getHandlers() {
        return ChunkSellToServerEvent.handlerList;
    }
    
    public static HandlerList getHandlerList() {
        return ChunkSellToServerEvent.handlerList;
    }
    
    public OfflinePlayer getLastOwner() {
        return this.lastOwner;
    }
    
    public Chunk getChunk() {
        return this.chunk;
    }
    
    static {
        ChunkSellToServerEvent.handlerList = new HandlerList();
    }
}
