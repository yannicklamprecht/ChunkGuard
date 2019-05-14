package de.ysl3000.chunkguard.lib.visualisation;

import java.util.Optional;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public interface ChunkLookup {

    void playAtChunk(final Optional<Player> p0, final Chunk p1);

    void playAtChunk(final Optional<Player> p0, final Chunk p1, final Particle p2);

    void playAtChunk(final Optional<Player> p0, final Chunk p1, final Play p2);

    interface Play {

        void play(final Player p0, final Location p1);
    }
}
