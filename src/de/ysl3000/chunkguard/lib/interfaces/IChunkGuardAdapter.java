package de.ysl3000.chunkguard.lib.interfaces;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.ysl3000.chunkguard.lib.interfaces.IWorldGuardAdapter;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;

import java.util.Optional;
import java.util.Set;

/**
 * Created by Yannick on 09.10.2016.
 */
public interface IChunkGuardAdapter extends IWorldGuardAdapter{

    boolean isBuyable(final Chunk p0);

    boolean setOwner(final OfflinePlayer p0, final Chunk p1);

    boolean isOwner(final OfflinePlayer p0, final Chunk p1);

    boolean cleanOwner(final Chunk p0);

    boolean hasOwner(final Chunk p0);

    boolean addMember(final OfflinePlayer p0, final Chunk p1);

    boolean removeMember(final OfflinePlayer p0, final Chunk p1);

    boolean cleanMembers(final Chunk p0);

    Set<OfflinePlayer> getMembers(final Chunk p0);

    Optional<OfflinePlayer> getOwner(final Chunk p0);

    Optional<ProtectedRegion> getRegion(final Chunk p0);
    boolean cleanFlags(final Chunk p0);


    boolean generateChunk(Chunk chunk);
}
