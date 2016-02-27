package de.ysl3000.chunkguard.lib.visualisation.locationmanagement;

import org.bukkit.*;

public abstract class AbstractValidator implements ILocationValidator
{
    @Override
    public boolean isValidLocation(final Location loc) {
        return this.isLocationVisibleFromSky(loc);
    }
    
    @Override
    public boolean isValid(final int x, final int y, final int z) {
        return this.isLocationValid(x, y, z);
    }
    
    protected final boolean isLocationValid(final int x, final int y, final int z) {
        return this.isBetween(x, 0, 16) && this.isBetween(y, 0, 255) && this.isBetween(z, 0, 16);
    }
    
    protected final boolean isLocationVisibleFromSky(final Location loc) {
        return loc.getBlockY() >= loc.getWorld().getHighestBlockAt(loc).getY();
    }
}
