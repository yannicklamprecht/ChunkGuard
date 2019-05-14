package de.ysl3000.chunkguard.lib.visualisation;

import de.ysl3000.chunkguard.lib.visualisation.locationmanagement.BorderValidator;
import de.ysl3000.chunkguard.lib.visualisation.locationmanagement.IManager;
import de.ysl3000.chunkguard.lib.visualisation.locationmanagement.Manager;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ChunkLookupListener implements ChunkLookup {

    private JavaPlugin plugin;

    public ChunkLookupListener(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void playAtChunk(final Optional<Player> p, final Chunk chunk) {
        this.playAtChunk(p, chunk, Particle.FLAME);
    }

    @Override
    public void playAtChunk(final Optional<Player> p, final Chunk chunk, final Particle particleEffect) {
        this.playAtChunk(p, chunk, (p1, location) -> location.getWorld().spawnParticle(particleEffect, location, 1));
    }

    @Override
    public void playAtChunk(final Optional<Player> p, final Chunk chunk, final Play play) {
        if (p.isPresent() && p.get().getVelocity().length() > 7.0) {
            return;
        }
        IManager iManager = new Manager();

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> iManager.getChunkLocations(chunk,new BorderValidator()).forEach(location -> play.play(p.get(),location)));
    }
}
