package de.ysl3000.chunkguard.lib.interfaces;

import org.bukkit.*;

public interface IBuyingAdapter
{
    boolean buyFromBank(final OfflinePlayer p0, final Chunk p1);
    
    boolean buyFromUser(final OfflinePlayer p0, final Chunk p1, final double p2);
    
    boolean sellToBank(final OfflinePlayer p0, final Chunk p1);
    
    boolean buyFromBank(final OfflinePlayer p0, final Location p1);
    
    boolean buyFromUser(final OfflinePlayer p0, final Location p1, final double p2);
    
    boolean sellToBank(final OfflinePlayer p0, final Location p1);
}
