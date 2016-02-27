package de.ysl3000.chunkguard.lib.interfaces;

import org.bukkit.*;
import java.util.*;
import com.sk89q.worldguard.protection.regions.*;

public interface IWorldGuardAdapter
{
    boolean isBuyable(final Location p0);
    
    boolean isBuyable(final Chunk p0);
    
    boolean isOwner(final OfflinePlayer p0, final Location p1);
    
    boolean isOwner(final OfflinePlayer p0, final Chunk p1);
    
    boolean setOwner(final OfflinePlayer p0, final Location p1);
    
    boolean setOwner(final OfflinePlayer p0, final Chunk p1);
    
    boolean cleanOwner(final Chunk p0);
    
    boolean cleanOwner(final Location p0);
    
    boolean hasOwner(final Location p0);
    
    boolean hasOwner(final Chunk p0);
    
    boolean addMember(final OfflinePlayer p0, final Location p1);
    
    boolean addMember(final OfflinePlayer p0, final Chunk p1);
    
    boolean removeMember(final OfflinePlayer p0, final Location p1);
    
    void removeMemberFromAllOwnedRegions(final OfflinePlayer p0, final OfflinePlayer p1, final World p2);
    
    void addMemberToAllOwnedRegions(final OfflinePlayer p0, final OfflinePlayer p1, final World p2);
    
    boolean removeMember(final OfflinePlayer p0, final Chunk p1);
    
    boolean cleanMembers(final Location p0);
    
    boolean cleanMembers(final Chunk p0);
    
    Set<OfflinePlayer> getMembers(final Location p0);
    
    Set<OfflinePlayer> getMembers(final Chunk p0);
    
    Optional<OfflinePlayer> getOwner(final Location p0);
    
    Optional<OfflinePlayer> getOwner(final Chunk p0);
    
    Optional<ProtectedRegion> getRegion(final Location p0);
    
    Optional<ProtectedRegion> getRegion(final Chunk p0);
    
    boolean cleanFlags(final Location p0);
    
    boolean cleanFlags(final Chunk p0);
    
    boolean cleanFlags(final Optional<ProtectedRegion> p0);
    
    boolean addRegion(final Optional<ProtectedRegion> p0, final World p1);
    
    boolean generateChunk(final Chunk p0);
    
    boolean safeChanges(final World p0);
}
