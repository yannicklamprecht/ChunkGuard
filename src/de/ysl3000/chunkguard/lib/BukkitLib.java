package de.ysl3000.chunkguard.lib;

import org.bukkit.inventory.*;
import org.bukkit.block.*;
import de.ysl3000.chunkguard.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.plugin.java.*;
import java.util.*;
import de.ysl3000.chunkguard.lib.visualisation.*;
import de.ysl3000.chunkguard.lib.visualisation.locationmanagement.*;

public class BukkitLib
{
    public static boolean decreaseItemStack(final Inventory inv, final ItemStack itemStack, final int amount) {
        if (itemStack.getAmount() < amount) {
            return false;
        }
        if (itemStack.getAmount() == amount) {
            inv.remove(itemStack);
            return true;
        }
        if (itemStack.getAmount() > amount) {
            itemStack.setAmount(itemStack.getAmount() - amount);
            return true;
        }
        return false;
    }
    
    public static boolean removeBlock(final Chunk chunk, final Material... materials) {
        final List<Material> materialList = Arrays.asList(materials);
        final List<BlockState> blocks = Arrays.asList(chunk.getTileEntities());
        blocks.stream().filter(b -> materialList.contains(b.getType())).forEach(b -> {
            if (b instanceof Chest) {
                ((Chest)b).getBlockInventory().clear();
            }
            ((BlockState)b).getBlock().setType(Material.AIR);
            return;
        });
        return !Arrays.asList(chunk.getTileEntities()).stream().anyMatch(b -> materialList.contains(b.getType()));
    }
    
    public static void listBlocks(final Chunk chunk) {
        final List<BlockState> blocks = Arrays.asList(chunk.getTileEntities());
        blocks.stream().forEach(b -> System.out.print("" + b.getType()));
    }
    
    public static void displayRegion(final ChunkGuardPlugin plugin, final Player p, final Location location) {
        final boolean isOccupied = plugin.getWorldGuardAdapter().getRegion(location).isPresent();
        new ChunkEdges(plugin).playAtChunk(Optional.of(p), location.getChunk(), new ChunkLookup.Play() {
            @Override
            public void play(final Player p, final Location location) {
                if (isOccupied) {
                    if (plugin.getWorldGuardAdapter().hasOwner(location)) {
                        ParticleEffect.FLAME.display(0.0f, 0.0f, 0.0f, 0.0f, 1, location, p);
                    }
                    else {
                        ParticleEffect.FIREWORKS_SPARK.display(0.0f, 0.0f, 0.0f, 0.0f, 1, location, p);
                    }
                }
                else {
                    ParticleEffect.SMOKE_NORMAL.display(0.0f, 0.0f, 0.0f, 0.0f, 1, location, p);
                }
            }
        });
    }
}
