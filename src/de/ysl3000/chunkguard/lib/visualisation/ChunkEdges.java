package de.ysl3000.chunkguard.lib.visualisation;

import org.bukkit.plugin.java.*;
import java.util.*;
import org.bukkit.entity.*;
import de.ysl3000.chunkguard.lib.visualisation.locationmanagement.*;
import org.bukkit.*;
import org.bukkit.plugin.*;
import org.bukkit.scheduler.*;

public class ChunkEdges implements ChunkLookup
{
    private JavaPlugin plugin;
    
    public ChunkEdges(final JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void playAtChunk(final Optional<Player> p, final Chunk chunk) {
        this.playAtChunk(p, chunk, ParticleEffect.FLAME);
    }
    
    @Override
    public void playAtChunk(final Optional<Player> p, final Chunk chunk, final ParticleEffect particleEffect) {
        this.playAtChunk(p, chunk, (p1, location) -> particleEffect.display(0.0f, 0.0f, 0.0f, 0.0f, 1, location, p1));
    }
    
    @Override
    public void playAtChunk(final Optional<Player> p, final Chunk chunk, final Play play) {
        if (p.isPresent() && p.get().getVelocity().length() > 7.0) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)this.plugin, (Runnable)new Runnable() {
            @Override
            public void run() {
                if (p.isPresent()) {
                    IManager manager = new Manager();
                    Set<Location> lc = manager.getChunkLocations(chunk, new EdgeValidator());

                    lc.stream().sorted((l1, l2) -> Integer.compare(l1.getBlockY(), l2.getBlockY())).forEach(loc -> {

                        Player player = p.get();

                        BukkitTask br = new BukkitRunnable() {

                            @Override
                            public void run() {
                                play.play(player, loc);
                            }

                        }.runTaskAsynchronously(plugin);
                    });
                }
            }
        });
    }
}
