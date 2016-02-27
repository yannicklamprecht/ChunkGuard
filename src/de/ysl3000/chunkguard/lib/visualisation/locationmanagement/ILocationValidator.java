package de.ysl3000.chunkguard.lib.visualisation.locationmanagement;

import org.bukkit.*;

public interface ILocationValidator
{
    boolean isValidLocation(final Location p0);
    
    default boolean isBetween(final int value, final int min, final int max) {
        return value >= min && value <= max;
    }
    
    boolean isValid(final int p0, final int p1, final int p2);
}
