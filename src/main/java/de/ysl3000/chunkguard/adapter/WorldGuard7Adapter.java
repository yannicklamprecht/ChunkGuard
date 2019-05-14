package de.ysl3000.chunkguard.adapter;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.ysl3000.chunkguard.customflags.FlagRegistry;
import de.ysl3000.chunkguard.customflags.OverFlowFlag;
import de.ysl3000.chunkguard.lib.interfaces.I7WorldGuardAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Created by ysl3000
 */
public class WorldGuard7Adapter implements I7WorldGuardAdapter {

    private WorldGuard worldGuard;
    private WorldGuardPlatform worldGuardPlatform;
    private WorldEdit worldEdit;

    public WorldGuard7Adapter() {
        this.worldGuard = WorldGuard.getInstance();
        this.worldGuardPlatform = worldGuard.getPlatform();
        this.worldEdit = WorldEdit.getInstance();
        new FlagRegistry(this.worldGuard).registerFlags();
    }

    @Override
    public boolean isOwner(final OfflinePlayer player, final Location location) {
        return this.getRegion(location).isPresent() && isOwner(player, this.getRegion(location).get());
    }

    @Override
    public boolean isOwner(OfflinePlayer player, ProtectedRegion region) {
        return region.getOwners().getUniqueIds().contains(player.getUniqueId());
    }

    @Override
    public boolean addOwner(OfflinePlayer player, Location location) {
        Optional<ProtectedRegion> regionOptional = getRegion(location);
        if (regionOptional.isPresent()) {
            return addOwner(player, regionOptional.get());
        }
        return false;
    }

    @Override
    public boolean removeOwner(OfflinePlayer player, Location location) {
        Optional<ProtectedRegion> regionOptional = getRegion(location);
        if (regionOptional.isPresent()) {
            return removeOwner(player, regionOptional.get());
        }
        return false;
    }

    @Override
    public boolean addOwner(OfflinePlayer player, ProtectedRegion region) {
        region.getOwners().addPlayer(player.getUniqueId());
        return isOwner(player, region);
    }

    @Override
    public boolean removeOwner(OfflinePlayer player, ProtectedRegion region) {
        region.getOwners().removePlayer(player.getUniqueId());
        return !isOwner(player, region);
    }

    @Override
    public boolean cleanOwner(final Location location) {
        Optional<ProtectedRegion> regionOptional = this.getRegion(location);
        if (regionOptional.isPresent()) {
            final ProtectedRegion region = regionOptional.get();
            region.setOwners(new DefaultDomain());
            return region.getOwners().size() == 0;
        }
        return false;
    }

    @Override
    public boolean hasOwner(final Location location) {
        return this.getOwner(location).isPresent();
    }

    @Override
    public boolean addMember(final OfflinePlayer player, final Location location) {
        Optional<ProtectedRegion> regionOptional = this.getRegion(location);
        if (regionOptional.isPresent()) {
            regionOptional.get().getMembers().addPlayer(player.getUniqueId());
            return regionOptional.get().getMembers().getUniqueIds().contains(player.getUniqueId());
        }
        return false;
    }

    @Override
    public boolean removeMember(final OfflinePlayer player, final Location location) {
        Optional<ProtectedRegion> regionOptional = this.getRegion(location);
        if (regionOptional.isPresent()) {
            regionOptional.get().getMembers().removePlayer(player.getUniqueId());
            return !regionOptional.get().getMembers().getUniqueIds().contains(player.getUniqueId());
        }
        return false;
    }

    @Override
    public void removeMemberFromAllOwnedRegions(final OfflinePlayer player,
    final OfflinePlayer member, final World world) {
        getRegionManager(world).getRegions().forEach((s, rg) -> {
            if (rg.getOwners().getUniqueIds().contains(player.getUniqueId()) && rg.getMembers().getUniqueIds().contains(member.getUniqueId())) {
                rg.getMembers().removePlayer(member.getUniqueId());
            }
        });
    }

    @Override
    public void addMemberToAllOwnedRegions(final OfflinePlayer player, final OfflinePlayer member,
    final World world) {
        getRegionManager(world).getRegions().forEach((s, rg) -> {
            if (rg.getOwners().getUniqueIds().contains(player.getUniqueId()) && !rg.getMembers().getUniqueIds().contains(member.getUniqueId())) {
                rg.getMembers().addPlayer(member.getUniqueId());
            }
        });
    }

    @Override
    public boolean isMember(OfflinePlayer player, ProtectedRegion protectedRegion) {
        return protectedRegion.getMembers().contains(player.getUniqueId()) || protectedRegion.getOwners().contains(player.getUniqueId());
    }

    @Override
    public boolean cleanMembers(final Location location) {
        Optional<ProtectedRegion> regionOptional = this.getRegion(location);
        if (regionOptional.isPresent()) {
            regionOptional.get().getMembers().removeAll();
            return regionOptional.get().getMembers().size() == 0;
        }
        return false;
    }

    @Override
    public Set<OfflinePlayer> getMembers(final Location location) {
        final Set<OfflinePlayer> members = new HashSet<>();
        Optional<ProtectedRegion> regionOptional = this.getRegion(location);
        regionOptional.ifPresent(protectedRegion -> protectedRegion.getMembers().getUniqueIds()
        .forEach(p -> members.add(Bukkit.getOfflinePlayer(p))));
        return members;
    }


    @Override
    public Optional<OfflinePlayer> getOwner(final Location location) {
        Optional<OfflinePlayer> owner = Optional.empty();
        Optional<ProtectedRegion> regionOptional = this.getRegion(location);
        if (regionOptional.isPresent()) {
            final Optional<UUID> ownerA = regionOptional.get().getOwners().getUniqueIds()
            .stream().findFirst();
            if (ownerA.isPresent()) {
                owner = Optional.of(Bukkit.getOfflinePlayer(ownerA.get()));
            }
        }
        return owner;
    }


    @Override
    public Optional<ProtectedRegion> getRegion(final Location location) {
        final ApplicableRegionSet regions = getRegionManager(location.getWorld())
        .getApplicableRegions(BukkitAdapter.asBlockVector(location));
        return regions.getRegions().stream().findFirst();
    }

    @Override
    public Optional<ProtectedRegion> getRegion(World world, String protectedRegionId) {
        return Optional.ofNullable(getRegionManager(world).getRegion(protectedRegionId));
    }

    @Override
    public Optional<ProtectedRegion> getRegion(String protectedRegionId) {
        return getLoadedProtectedRegions().stream().filter(rg -> rg.getId().equalsIgnoreCase(protectedRegionId)).findFirst();
    }

    @Override
    public Optional<ProtectedRegion> getRegion(List<World> worlds, String protectedRegionId) {
        for (World world : worlds) {
            Optional<ProtectedRegion> optionalProtectedRegion = getRegion(world, protectedRegionId);
            if (optionalProtectedRegion.isPresent()) {
                return optionalProtectedRegion;
            }
        }
        return Optional.empty();
    }

    @Override
    public List<ProtectedRegion> getLoadedProtectedRegions() {
        List<ProtectedRegion> loadedRegions = new ArrayList<>();
        worldGuardPlatform.getRegionContainer().getLoaded().forEach(regionManager -> loadedRegions.addAll(regionManager.getRegions().values()));
        return loadedRegions;
    }

    @Override
    public List<ProtectedRegion> getLoadedProtectedRegionsByPredicate(Predicate<ProtectedRegion> protectedRegionPredicate) {
        List<ProtectedRegion> protectedRegions = new ArrayList<>();
        worldGuardPlatform.getRegionContainer().getLoaded().forEach(regionManager -> protectedRegions.addAll(regionManager.getRegions().values().stream().filter(protectedRegionPredicate).collect(Collectors.toList())));
        return protectedRegions;
    }

    @Override
    public List<String> getLoadedProtectedRegionsStringByPredicate(Predicate<ProtectedRegion> protectedRegionPredicate) {
        List<String> loadedRegions = new ArrayList<>();
        worldGuardPlatform.getRegionContainer().getLoaded().forEach(regionManager -> {
            loadedRegions.addAll(mapTo(regionManager.getRegions().values().stream().filter(protectedRegionPredicate)));
        });
        return loadedRegions;
    }

    @Override
    public List<String> getLoadedProtectedRegionsString() {
        return getLoadedProtectedRegionsStringByPredicate(protectedRegion -> true);
    }

    @Override
    public List<String> getLoadedProtectedRegionsStringWherePlayerIsMember(OfflinePlayer player) {
        return getLoadedProtectedRegionsStringByPredicate(protectedRegion -> isMember(player, protectedRegion) || isOwner(player, protectedRegion));
    }

    @Override
    public List<String> getLoadedProtectedRegionsStringWherePlayerIsOwner(OfflinePlayer player) {
        return getLoadedProtectedRegionsStringByPredicate(protectedRegion -> isOwner(player, protectedRegion));
    }

    @Override
    public List<ProtectedRegion> getLoadedProtectedRegionsStringWherePlayerIsOwnerByWorld(OfflinePlayer player, World world) {
        return worldGuard.getPlatform()
        .getRegionContainer().get(BukkitAdapter.adapt(world))
        .getRegions().values().stream()
        .filter(region -> isOwner(player, region)).collect(Collectors.toList());
    }


    private List<String> mapTo(Stream<ProtectedRegion> regionStream) {
        return regionStream.map(ProtectedRegion::getId).collect(Collectors.toList());
    }


    @Override
    public boolean cleanFlags(final Location location) {
        return this.cleanFlags(this.getRegion(location));
    }


    //globals: firespread, lava_fire, enderdragon, ghast, creeper, pvp
    @Override
    public boolean cleanFlags(final Optional<ProtectedRegion> region) {
        if (region.isPresent()) {
            final ProtectedRegion rg = region.get();
            this.setFlags(rg, RegionGroup.NON_MEMBERS, StateFlag.State.DENY);
            rg.setFlag(Flags.OTHER_EXPLOSION, StateFlag.State.DENY);
            rg.setFlag(OverFlowFlag.LAVA_OVERFLOW, StateFlag.State.DENY);
            rg.setFlag(OverFlowFlag.WATER_OVERFLOW, StateFlag.State.DENY);
            return true;
        }
        return false;
    }

    @Override
    public boolean addRegion(final Optional<ProtectedRegion> region, final World world) {
        if (region.isPresent()) {
            getRegionManager(world).addRegion(region.get());
            return true;
        }
        return false;
    }


    @Override
    public boolean saveChanges(final World world) {
        try {
            getRegionManager(world).saveChanges();
            return true;
        } catch (StorageException e) {
            return false;
        }
    }

    @Override
    public Flag fuzzyMatchFlag(String flag) {
        return Flags.fuzzyMatchFlag(worldGuard.getFlagRegistry(), flag);
    }


    private void setFlags(final ProtectedRegion rg, final RegionGroup g,
    final StateFlag.State state) {
        rg.setFlag(Flags.INTERACT, state);
        rg.setFlag(Flags.INTERACT.getRegionGroupFlag(), g);
        rg.setFlag(Flags.BLOCK_BREAK, state);
        rg.setFlag(Flags.BLOCK_BREAK.getRegionGroupFlag(), g);
        rg.setFlag(Flags.BUILD, state);
        rg.setFlag(Flags.BUILD.getRegionGroupFlag(), g);
        rg.setFlag(Flags.DESTROY_VEHICLE, state);
        rg.setFlag(Flags.DESTROY_VEHICLE.getRegionGroupFlag(), g);
        rg.setFlag(Flags.BLOCK_PLACE, state);
        rg.setFlag(Flags.BLOCK_PLACE.getRegionGroupFlag(), g);
        rg.setFlag(Flags.USE, state);
        rg.setFlag(Flags.USE.getRegionGroupFlag(), g);
        rg.setFlag(Flags.ENTITY_ITEM_FRAME_DESTROY, state);
        rg.setFlag(Flags.ENTITY_ITEM_FRAME_DESTROY.getRegionGroupFlag(), g);
        rg.setFlag(Flags.ENTITY_PAINTING_DESTROY, state);
        rg.setFlag(Flags.ENTITY_PAINTING_DESTROY.getRegionGroupFlag(), g);
        rg.setFlag(Flags.CHEST_ACCESS, state);
        rg.setFlag(Flags.CHEST_ACCESS.getRegionGroupFlag(), g);
    }


    @Override
    public void addRegion(ProtectedCuboidRegion region, World world) {
        RegionManager regionManager = worldGuard.getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
        regionManager.addRegion(region);
    }

    @Override
    public void removeRegion(String region, World world) {
        RegionManager regionManager = worldGuard.getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
        regionManager.removeRegion(region);
    }

    @Override
    public ProtectedCuboidRegion starter(Location l, int radius, String prefix) {
        BlockVector3 b1 = BlockVector3.at(l.getBlockX() + radius, 0, l.getBlockZ() + radius);
        BlockVector3 b2 = BlockVector3.at(l.getBlockX() - radius, 256, l.getBlockZ() - radius);
        return new ProtectedCuboidRegion(prefix + l.getBlockX() + "_" + l.getBlockZ(), b1, b2);
    }


    @Override
    public Set<ProtectedRegion> getRegionsInRadius(Location l, int radius) {
        ApplicableRegionSet regions = worldGuard.getPlatform()
        .getRegionContainer()
        .get(BukkitAdapter.adapt(l.getWorld()))
        .getApplicableRegions(starter(l, radius, "temp"));
        return regions.getRegions();

    }


    @Override
    public boolean isOverlapping(ProtectedCuboidRegion region, World world) {
        return worldGuard.getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world)).getApplicableRegions(region).size() > 0;
    }

    @Override
    public boolean isOverlapping(ProtectedCuboidRegion region, World world, String... excluded) {
        ApplicableRegionSet regions = worldGuard.getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world)).getApplicableRegions(region);
        Iterator iterator = regions.iterator();
        boolean overlapping = false;
        List<String> ex = Arrays.asList(excluded);
        while (iterator.hasNext()) {
            ProtectedRegion protectedRegion = (ProtectedRegion) iterator.next();
            if (!ex.contains(protectedRegion.getId()) && protectedRegion.getParent().getId().equals("__GLOBAL__")) {
                overlapping = true;
            }
        }

        return overlapping;
    }


    @Override
    public boolean isOwner(ProtectedRegion region, Player player) {
        return region.getOwners().contains(player.getUniqueId());
    }

    @Override
    public ProtectedRegion getCurrentRegion(Player player) {
        com.sk89q.worldedit.entity.Player skPlayer = BukkitAdapter.adapt(player);
        ApplicableRegionSet set = worldGuard.getPlatform()
        .getRegionContainer()
        .get(skPlayer.getWorld())
        .getApplicableRegions(skPlayer.getLocation().toVector().toBlockPoint());
        for (ProtectedRegion region : set) {
            if (region.getParent() == null) {
                return region;
            } else {
                return region.getParent();
            }
        }
        return null;
    }

    @Override
    public void regen(ProtectedRegion region, Player player) throws IncompleteRegionException {
        com.sk89q.worldedit.entity.Player skPlayer = BukkitAdapter.adapt(player);
        LocalSession session = worldEdit.getSessionManager().get(skPlayer);
        EditSession editSession = worldEdit.getSessionManager().get(skPlayer).createEditSession(skPlayer);
        session.setRegionSelector(skPlayer.getWorld(),
        new CuboidRegionSelector(
        skPlayer.getWorld(),
        region.getMinimumPoint(),
        region.getMaximumPoint()));
        skPlayer.getWorld().regenerate(session.getSelection(skPlayer.getWorld()), editSession);
    }


    private RegionManager getRegionManager(World world) {
        return worldGuardPlatform.getRegionContainer().get(BukkitAdapter.adapt(world));
    }


    @Override
    public boolean generateChunk(final Chunk chunk) {
        if (this.getRegion(chunk).isPresent()) {
            return false;
        }
        final Location min = chunk.getBlock(0, 0, 0).getLocation();
        final Location max = chunk.getBlock(15, 255, 15).getLocation();
        final String name = "c_" + chunk.getX() + "_" + chunk.getZ();
        final ProtectedRegion rg = new ProtectedCuboidRegion(name, BlockVector3.at(min.getBlockX(), min.getBlockY(), min.getBlockZ()), BlockVector3.at(max.getBlockX(), max.getBlockY(), max.getBlockZ()));

        this.cleanFlags(Optional.of(rg));
        this.addRegion(Optional.of(rg), chunk.getWorld());
        this.saveChanges(chunk.getWorld());
        return true;
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
    public boolean setOwner(OfflinePlayer player, Location location) {
        return getRegion(location).filter(rg -> {
            rg.getOwners().addPlayer(player.getUniqueId());
            return rg.getOwners().contains(player.getUniqueId());
        }).isPresent();
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
    public boolean isBuyable(final Chunk chunk) {
        if (this.getRegion(chunk).isPresent()) {
            final ProtectedRegion region = this.getRegion(chunk).get();
            if (region.getFlag(OverFlowFlag.BUYABLE) != null) {
                return region.getFlag(OverFlowFlag.BUYABLE);
            }
        }
        return false;
    }

    @Override
    public boolean isBuyable(Location location) {
        return isBuyable(location.getChunk());
    }

}
