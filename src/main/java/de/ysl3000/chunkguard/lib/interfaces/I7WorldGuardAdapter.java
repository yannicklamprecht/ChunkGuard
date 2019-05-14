package de.ysl3000.chunkguard.lib.interfaces;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Created by ysl3000
 */
public interface I7WorldGuardAdapter {

    boolean isOwner(OfflinePlayer player, Location location);

    boolean isOwner(OfflinePlayer player, ProtectedRegion region);

    boolean addOwner(OfflinePlayer player, Location location);

    boolean removeOwner(OfflinePlayer player, Location location);

    boolean addOwner(OfflinePlayer player, ProtectedRegion region);

    boolean removeOwner(OfflinePlayer player, ProtectedRegion region);

    boolean cleanOwner(Location location);

    boolean hasOwner(Location location);

    boolean addMember(OfflinePlayer player, Location location);

    boolean removeMember(OfflinePlayer player, Location location);

    void removeMemberFromAllOwnedRegions(OfflinePlayer player,
    OfflinePlayer member, World world);

    void addMemberToAllOwnedRegions(OfflinePlayer player, OfflinePlayer member,
    World world);

    boolean isMember(OfflinePlayer player, ProtectedRegion protectedRegion);

    boolean cleanMembers(Location location);

    Set<OfflinePlayer> getMembers(Location location);

    Optional<OfflinePlayer> getOwner(Location location);

    Optional<ProtectedRegion> getRegion(Location location);

    Optional<ProtectedRegion> getRegion(World world, String protectedRegionId);

    Optional<ProtectedRegion> getRegion(String protectedRegionId);

    Optional<ProtectedRegion> getRegion(List<World> worlds, String protectedRegionId);

    List<ProtectedRegion> getLoadedProtectedRegions();

    List<ProtectedRegion> getLoadedProtectedRegionsByPredicate(Predicate<ProtectedRegion> protectedRegionPredicate);

    List<String> getLoadedProtectedRegionsStringByPredicate(Predicate<ProtectedRegion> protectedRegionPredicate);

    List<String> getLoadedProtectedRegionsString();

    List<String> getLoadedProtectedRegionsStringWherePlayerIsMember(OfflinePlayer player);

    List<String> getLoadedProtectedRegionsStringWherePlayerIsOwner(OfflinePlayer player);

    List<ProtectedRegion> getLoadedProtectedRegionsStringWherePlayerIsOwnerByWorld(OfflinePlayer player, World world);

    boolean cleanFlags(Location location);

    boolean cleanFlags(Optional<ProtectedRegion> region);

    boolean addRegion(Optional<ProtectedRegion> region, World world);

    boolean saveChanges(World world);

    Flag fuzzyMatchFlag(String flag);

    void addRegion(ProtectedCuboidRegion region, World world);

    void removeRegion(String region, World world);

    ProtectedCuboidRegion starter(Location l, int radius, String prefix);

    Set<ProtectedRegion> getRegionsInRadius(Location l, int radius);

    boolean isOverlapping(ProtectedCuboidRegion region, World world);

    boolean isOverlapping(ProtectedCuboidRegion region, World world, String... excluded);

    boolean isOwner(ProtectedRegion region, Player player);

    ProtectedRegion getCurrentRegion(Player player);

    void regen(ProtectedRegion region, Player player) throws IncompleteRegionException;

    boolean generateChunk(Chunk chunk);

    boolean isBuyable(Chunk chunk);

    boolean isBuyable(Location location);

    boolean isOwner(OfflinePlayer player, Chunk chunk);

    boolean setOwner(OfflinePlayer player, Chunk chunk);

    boolean setOwner(OfflinePlayer player, Location location);

    boolean cleanOwner(Chunk chunk);

    boolean hasOwner(Chunk chunk);

    boolean addMember(OfflinePlayer player, Chunk chunk);

    boolean removeMember(OfflinePlayer player, Chunk chunk);

    boolean cleanMembers(Chunk chunk);

    Optional<OfflinePlayer> getOwner(Chunk chunk);

    Set<OfflinePlayer> getMembers(Chunk chunk);

    Optional<ProtectedRegion> getRegion(Chunk chunk);

    boolean cleanFlags(Chunk chunk);
}
