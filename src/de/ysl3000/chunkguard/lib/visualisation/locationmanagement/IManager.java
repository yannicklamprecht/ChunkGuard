package de.ysl3000.chunkguard.lib.visualisation.locationmanagement;

import java.util.*;
import org.bukkit.*;

public interface IManager
{
    void playAtLocation(final Location p0);
    
    Location getHighestBlockAtLocation(final Location p0);
    
    Set<Location> getLocationsAboveLocation(final Location p0);
    
    Set<Location> getChunkLocations(final Chunk p0);
    
    Set<Location> getChunkLocations(final Chunk p0, final ILocationValidator... p1);
    
    Set<Location> getCuboidLocation(final Location p0, final Location p1);
    
    Set<Location> getCuboidLocation(final Location p0, final Location p1, final ILocationValidator... p2);
}
