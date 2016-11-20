package de.ysl3000.chunkguard.lib.interfaces;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.regions.*;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import java.util.Optional;
import java.util.Set;

public interface IWorldGuardAdapter {
    boolean isBuyable(final Location p0);

    boolean isOwner(final OfflinePlayer p0, final Location p1);

    boolean setOwner(final OfflinePlayer p0, final Location p1);


    boolean cleanOwner(final Location p0);

    boolean hasOwner(final Location p0);


    boolean addMember(final OfflinePlayer p0, final Location p1);


    boolean removeMember(final OfflinePlayer p0, final Location p1);

    void removeMemberFromAllOwnedRegions(final OfflinePlayer p0, final OfflinePlayer p1, final World p2);

    void addMemberToAllOwnedRegions(final OfflinePlayer p0, final OfflinePlayer p1, final World p2);


    boolean cleanMembers(final Location p0);


    Set<OfflinePlayer> getMembers(final Location p0);


    Optional<OfflinePlayer> getOwner(final Location p0);


    Optional<ProtectedRegion> getRegion(final Location p0);


    boolean cleanFlags(final Location p0);


    boolean cleanFlags(final Optional<ProtectedRegion> p0);

    boolean addRegion(final Optional<ProtectedRegion> p0, final World p1);

    boolean safeChanges(final World p0);

    Flag fuzzyMatchFlag(String flag);

}
