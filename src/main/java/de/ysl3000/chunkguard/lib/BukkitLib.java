package de.ysl3000.chunkguard.lib;

import de.ysl3000.chunkguard.ChunkGuardPlugin;
import de.ysl3000.chunkguard.lib.visualisation.ChunkEdges;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

public final class BukkitLib {

    private BukkitLib() {
    }

    public static boolean removeBlock(Chunk chunk, Collection<Material> materials) {
        List<BlockState> blocks = Arrays.asList(chunk.getTileEntities());
        blocks.stream().filter(b -> materials.contains(b.getType())).forEach(b -> {
            if (b instanceof Chest) {
                ((Chest) b).getBlockInventory().clear();
            }
            b.getBlock().setType(Material.AIR);
            b.update();
        });
        return Arrays.stream(chunk.getTileEntities()).noneMatch(b -> materials.contains(b.getType()));
    }

    public static void displayRegion(final ChunkGuardPlugin plugin, final Player p, final Location location) {
        final boolean isOccupied = plugin.getWorldGuardAdapter().getRegion(location).isPresent();
        new ChunkEdges(plugin).playAtChunk(Optional.of(p), location.getChunk(), (p1, location1) -> {
            if (isOccupied) {
                if (plugin.getWorldGuardAdapter().hasOwner(location1)) {
                    location1.getWorld().spawnParticle(Particle.FLAME, location1, 1);
                } else {
                    location1.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, location1, 1);
                }
            } else {
                location1.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location1, 1);
            }
        });
    }
}
