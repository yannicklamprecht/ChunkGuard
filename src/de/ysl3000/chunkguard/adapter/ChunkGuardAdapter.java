package de.ysl3000.chunkguard.adapter;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.ysl3000.chunkguard.lib.interfaces.IChunkGuardAdapter;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.Optional;
import java.util.Set;

/**
 * Created by Yannick on 09.10.2016.
 */
public class ChunkGuardAdapter extends WorldGuardAdapter implements IChunkGuardAdapter {


    public ChunkGuardAdapter(WorldGuardPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean isBuyable(final Chunk chunk) {
        return this.isBuyable(chunk.getBlock(0, 0, 0).getLocation());
    }

    @Override
    public boolean isOwner(final OfflinePlayer player, final Chunk chunk) {
        return this.isOwner(player, chunk.getBlock(0, 0, 0).getLocation());
    }

    @Override
    public boolean setOwner(final OfflinePlayer player, final Chunk chunk) {
        return this.setOwner(player, chunk.getBlock(0, 0, 0).getLocation());
    }

    @Override
    public boolean cleanOwner(final Chunk chunk) {
        return this.cleanOwner(chunk.getBlock(0, 0, 0).getLocation());
    }

    @Override
    public boolean hasOwner(final Chunk chunk) {
        return this.hasOwner(chunk.getBlock(0, 0, 0).getLocation());
    }


    @Override
    public boolean addMember(final OfflinePlayer player, final Chunk chunk) {
        return this.addMember(player, chunk.getBlock(0, 0, 0).getLocation());
    }

    @Override
    public boolean removeMember(final OfflinePlayer player, final Chunk chunk) {
        return this.removeMember(player, chunk.getBlock(0, 0, 0).getLocation());
    }


    @Override
    public boolean cleanMembers(final Chunk chunk) {
        return this.cleanMembers(chunk.getBlock(0, 0, 0).getLocation());
    }

    @Override
    public Optional<OfflinePlayer> getOwner(final Chunk chunk) {
        return this.getOwner(chunk.getBlock(0, 0, 0).getLocation());
    }


    @Override
    public Set<OfflinePlayer> getMembers(final Chunk chunk) {
        return this.getMembers(chunk.getBlock(0, 0, 0).getLocation());
    }

    @Override
    public Optional<ProtectedRegion> getRegion(final Chunk chunk) {
        return this.getRegion(chunk.getBlock(0, 0, 0).getLocation());
    }

    @Override
    public boolean cleanFlags(final Chunk chunk) {
        return this.cleanFlags(this.getRegion(chunk));
    }


    @Override
    public boolean generateChunk(final Chunk chunk) {
        if (this.getRegion(chunk).isPresent()) {
            //Bukkit.getPluginManager().callEvent((Event) new MiniMapUpdateEvent(chunk));
            return false;
        }
        final Location min = chunk.getBlock(0, 0, 0).getLocation();
        final Location max = chunk.getBlock(15, 255, 15).getLocation();
        final String name = "c_" + chunk.getX() + "_" + chunk.getZ();
        final ProtectedRegion rg = new ProtectedCuboidRegion(name, new BlockVector(min.getBlockX(), min.getBlockY(), min.getBlockZ()), new BlockVector(max.getBlockX(), max.getBlockY(), max.getBlockZ()));
        this.cleanFlags(Optional.of(rg));
        this.addRegion(Optional.of(rg), chunk.getWorld());
        this.safeChanges(chunk.getWorld());
        //Bukkit.getServer().getPluginManager().callEvent(new ChunkRegionCreateEvent(chunk));
        return true;
    }


}
