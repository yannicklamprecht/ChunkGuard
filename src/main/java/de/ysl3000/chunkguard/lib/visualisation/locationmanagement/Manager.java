package de.ysl3000.chunkguard.lib.visualisation.locationmanagement;

import java.util.*;
import org.bukkit.*;

public class Manager implements IManager
{
    private ILocationValidator logic;
    
    public Manager() {
        this.logic = new VisibleLocationValidator();
    }
    
    @Override
    public void playAtLocation(final Location loc) {
        loc.getWorld().spawnParticle(Particle.FLAME,loc,0);
    }
    
    @Override
    public Location getHighestBlockAtLocation(final Location loc) {
        return loc.getWorld().getHighestBlockAt(loc).getLocation();
    }
    
    @Override
    public Set<Location> getLocationsAboveLocation(final Location loc) {
        final Set<Location> locations = new HashSet<Location>();
        for (int y = loc.getBlockY(); y < loc.getWorld().getMaxHeight(); ++y) {
            locations.add(loc.add((double)loc.getBlockX(), (double)y, (double)loc.getBlockZ()));
        }
        return locations;
    }
    
    @Override
    public Set<Location> getChunkLocations(final Chunk chunk) {
        return this.getChunkLocations(chunk, this.logic);
    }
    
    @Override
    public Set<Location> getChunkLocations(final Chunk chunk, final ILocationValidator... validator) {
        final Set<Location> locations = new HashSet<Location>();
        for (int y = 0; y < chunk.getWorld().getMaxHeight(); ++y) {
            for (int z = 0; z <= 15; ++z) {
                for (int x = 0; x <= 15; ++x) {
                    boolean valid = false;
                    final Location location = chunk.getBlock(x, y, z).getLocation();
                    for (final ILocationValidator val : validator) {
                        if (val.isValidLocation(location) && val.isValid(x, y, z)) {
                            valid = true;
                        }
                    }
                    if (valid && this.logic.isValidLocation(location) && this.logic.isValid(x, y, z)) {
                        locations.add(location);
                    }
                }
            }
        }
        return locations;
    }
    
    @Override
    public Set<Location> getCuboidLocation(final Location min, final Location max) {
        return null;
    }
    
    @Override
    public Set<Location> getCuboidLocation(final Location min, final Location max, final ILocationValidator... validator) {
        return null;
    }
}
