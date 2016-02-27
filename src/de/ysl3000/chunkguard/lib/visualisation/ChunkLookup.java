package de.ysl3000.chunkguard.lib.visualisation;

import java.util.*;
import org.bukkit.entity.*;
import de.ysl3000.chunkguard.lib.visualisation.locationmanagement.*;
import org.bukkit.*;

public interface ChunkLookup
{
    void playAtChunk(final Optional<Player> p0, final Chunk p1);
    
    void playAtChunk(final Optional<Player> p0, final Chunk p1, final ParticleEffect p2);
    
    void playAtChunk(final Optional<Player> p0, final Chunk p1, final Play p2);
    
    public interface Play
    {
        void play(final Player p0, final Location p1);
    }
}
