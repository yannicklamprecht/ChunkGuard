package de.ysl3000.chunkguard.lib.visualisation;

import de.ysl3000.chunkguard.lib.visualisation.locationmanagement.EdgeValidator;
import de.ysl3000.chunkguard.lib.visualisation.locationmanagement.IManager;
import de.ysl3000.chunkguard.lib.visualisation.locationmanagement.Manager;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ChunkEdges implements ChunkLookup {

    private JavaPlugin plugin;

    public ChunkEdges(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void playAtChunk(final Optional<Player> p, final Chunk chunk) {
        this.playAtChunk(p, chunk, Particle.FLAME);
    }

    @Override
    public void playAtChunk(final Optional<Player> p, final Chunk chunk, final Particle particleEffect) {
        this.playAtChunk(p, chunk, (p1, location) -> {
            location.getWorld().spawnParticle(particleEffect, location, 1);
        });
    }

    @Override
    public void playAtChunk(final Optional<Player> p, final Chunk chunk, final Play play) {
        if (p.isPresent() && p.get().getVelocity().length() > 7.0) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            if (p.isPresent()) {
                IManager manager = new Manager();
                Set<Location> lc = manager.getChunkLocations(chunk, new EdgeValidator());

                lc.stream().sorted(Comparator.comparingInt(Location::getBlockY)).forEach(loc -> {

                    Player player = p.get();

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            play.play(player, loc);
                        }

                    }.runTaskAsynchronously(plugin);
                });
            }
        });
    }
}
