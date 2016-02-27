package de.ysl3000.chunkguard.adapter;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.ysl3000.chunkguard.ChunkGuardPlugin;
import de.ysl3000.chunkguard.customflags.OverFlowFlag;
import de.ysl3000.chunkguard.events.ChunkRegionCreateEvent;
import de.ysl3000.chunkguard.events.MiniMapUpdateEvent;
import de.ysl3000.chunkguard.lib.interfaces.IWorldGuardAdapter;
import org.bukkit.*;
import org.bukkit.event.Event;
import com.sk89q.worldedit.BlockVector;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class WorldGuardAdapter implements IWorldGuardAdapter
{
    private WorldGuardPlugin worldGuardPlugin;
    
    public WorldGuardAdapter(final ChunkGuardPlugin plugin) {
        this.worldGuardPlugin = plugin.getWG();
    }
    
    @Override
    public boolean isBuyable(final Location loc) {
        return this.isBuyable(loc.getChunk());
    }
    
    @Override
    public boolean isBuyable(final Chunk chunk) {
        if (this.getRegion(chunk).isPresent()) {
            final ProtectedRegion region = this.getRegion(chunk).get();
            if (region.getFlag((Flag)DefaultFlag.BUYABLE) != null) {
                return (boolean)region.getFlag((Flag)DefaultFlag.BUYABLE);
            }
        }
        return false;
    }
    
    @Override
    public boolean isOwner(final OfflinePlayer player, final Location location) {
        return this.getRegion(location).isPresent() && this.getRegion(location).get().isOwner(this.worldGuardPlugin.wrapOfflinePlayer(player));
    }
    
    @Override
    public boolean isOwner(final OfflinePlayer player, final Chunk chunk) {
        return this.isOwner(player, chunk.getBlock(0, 0, 0).getLocation());
    }
    
    @Override
    public boolean setOwner(final OfflinePlayer player, final Location location) {
        if (this.getRegion(location).isPresent()) {
            final DefaultDomain domain = new DefaultDomain();
            LocalPlayer localPlayer = this.worldGuardPlugin.wrapOfflinePlayer(player);
            if (player.isOnline()) {
                localPlayer = this.worldGuardPlugin.wrapPlayer(player.getPlayer());
            }
            domain.addPlayer(localPlayer);
            this.getRegion(location).get().setOwners(domain);
            return this.getRegion(location).get().isOwner(localPlayer);
        }
        return false;
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
    public boolean cleanOwner(final Location location) {
        if (this.getRegion(location).isPresent()) {
            final ProtectedRegion region = this.getRegion(location).get();
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
    public boolean hasOwner(final Chunk chunk) {
        return this.hasOwner(chunk.getBlock(0, 0, 0).getLocation());
    }
    
    @Override
    public boolean addMember(final OfflinePlayer player, final Location location) {
        if (this.getRegion(location).isPresent()) {
            final LocalPlayer localPlayer = this.worldGuardPlugin.wrapOfflinePlayer(player);
            this.getRegion(location).get().getMembers().addPlayer(localPlayer);
            return this.getRegion(location).get().isMember(localPlayer);
        }
        return false;
    }
    
    @Override
    public boolean addMember(final OfflinePlayer player, final Chunk chunk) {
        return this.addMember(player, chunk.getBlock(0, 0, 0).getLocation());
    }
    
    @Override
    public boolean removeMember(final OfflinePlayer player, final Location location) {
        if (this.getRegion(location).isPresent()) {
            final LocalPlayer localPlayer = this.worldGuardPlugin.wrapOfflinePlayer(player);
            this.getRegion(location).get().getMembers().removePlayer(localPlayer);
            return !this.getRegion(location).get().isMember(localPlayer);
        }
        return false;
    }
    
    @Override
    public void removeMemberFromAllOwnedRegions(final OfflinePlayer player, final OfflinePlayer member, final World world) {
        this.worldGuardPlugin.getRegionManager(world).getRegions().forEach((s, rg) -> {
            if (rg.isOwner(this.worldGuardPlugin.wrapOfflinePlayer(player))) {
                LocalPlayer target = this.worldGuardPlugin.wrapOfflinePlayer(member);
                if (rg.isMember(target)) {
                    rg.getMembers().removePlayer(target);
                }
            }
        });
    }
    
    @Override
    public void addMemberToAllOwnedRegions(final OfflinePlayer player, final OfflinePlayer member, final World world) {
        this.worldGuardPlugin.getRegionManager(world).getRegions().forEach((s, rg) -> {
            if (rg.isOwner(this.worldGuardPlugin.wrapOfflinePlayer(player))) {
                LocalPlayer target = this.worldGuardPlugin.wrapOfflinePlayer(member);
                if (!rg.isMember(target)) {
                    rg.getMembers().addPlayer(target);
                }
            }
        });
    }
    
    @Override
    public boolean removeMember(final OfflinePlayer player, final Chunk chunk) {
        return this.removeMember(player, chunk.getBlock(0, 0, 0).getLocation());
    }
    
    @Override
    public boolean cleanMembers(final Location location) {
        if (this.getRegion(location).isPresent()) {
            this.getRegion(location).get().getMembers().removeAll();
            return this.getRegion(location).get().getMembers().size() == 0;
        }
        return false;
    }
    
    @Override
    public boolean cleanMembers(final Chunk chunk) {
        return this.cleanMembers(chunk.getBlock(0, 0, 0).getLocation());
    }
    
    @Override
    public Set<OfflinePlayer> getMembers(final Location location) {
        final Set<OfflinePlayer> members = new HashSet<OfflinePlayer>();
        if (this.getRegion(location).isPresent()) {
            this.getRegion(location).get().getMembers().getUniqueIds().forEach(p -> members.add(Bukkit.getOfflinePlayer(p)));
        }
        return members;
    }
    
    @Override
    public Set<OfflinePlayer> getMembers(final Chunk chunk) {
        return this.getMembers(chunk.getBlock(0, 0, 0).getLocation());
    }
    
    @Override
    public Optional<OfflinePlayer> getOwner(final Location location) {
        Optional<OfflinePlayer> owner = Optional.empty();
        if (this.getRegion(location).isPresent()) {
            final Optional<UUID> ownerA = (Optional<UUID>)this.getRegion(location).get().getOwners().getUniqueIds().stream().findFirst();
            if (ownerA.isPresent()) {
                owner = Optional.of(Bukkit.getOfflinePlayer((UUID)ownerA.get()));
            }
        }
        return owner;
    }
    
    @Override
    public Optional<OfflinePlayer> getOwner(final Chunk chunk) {
        return this.getOwner(chunk.getBlock(0, 0, 0).getLocation());
    }
    
    @Override
    public Optional<ProtectedRegion> getRegion(final Location location) {
        final Optional<ProtectedRegion> region = Optional.empty();
        final ApplicableRegionSet regions = this.worldGuardPlugin.getRegionManager(location.getWorld()).getApplicableRegions(location);
        return regions.getRegions().stream().findFirst();
    }
    
    @Override
    public Optional<ProtectedRegion> getRegion(final Chunk chunk) {
        return this.getRegion(chunk.getBlock(0, 0, 0).getLocation());
    }
    
    @Override
    public boolean cleanFlags(final Location location) {
        return this.cleanFlags(this.getRegion(location));
    }
    
    @Override
    public boolean cleanFlags(final Chunk chunk) {
        return this.cleanFlags(this.getRegion(chunk));
    }
    
    @Override
    public boolean cleanFlags(final Optional<ProtectedRegion> region) {
        if (region.isPresent()) {
            final ProtectedRegion rg = region.get();
            this.setFlags(rg, RegionGroup.NON_MEMBERS, StateFlag.State.DENY);
            rg.setFlag((Flag)DefaultFlag.LAVA_FIRE, (Object)StateFlag.State.DENY);
            rg.setFlag((Flag)DefaultFlag.FIRE_SPREAD, (Object)StateFlag.State.DENY);
            rg.setFlag((Flag)DefaultFlag.OTHER_EXPLOSION, (Object)StateFlag.State.DENY);
            rg.setFlag((Flag)DefaultFlag.GHAST_FIREBALL, (Object)StateFlag.State.DENY);
            rg.setFlag((Flag)DefaultFlag.ENDERDRAGON_BLOCK_DAMAGE, (Object)StateFlag.State.DENY);
            rg.setFlag((Flag)DefaultFlag.CREEPER_EXPLOSION, (Object)StateFlag.State.DENY);
            rg.setFlag((Flag)DefaultFlag.BUYABLE, (Object)true);
            rg.setFlag((Flag)DefaultFlag.PVP, (Object)StateFlag.State.DENY);
            rg.setFlag((Flag)OverFlowFlag.LAVA_OVERFLOW, (Object)StateFlag.State.DENY);
            rg.setFlag((Flag)OverFlowFlag.WATER_OVERFLOW, (Object)StateFlag.State.DENY);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean addRegion(final Optional<ProtectedRegion> region, final World world) {
        if (region.isPresent()) {
            this.worldGuardPlugin.getRegionManager(world).addRegion((ProtectedRegion)region.get());
            return true;
        }
        return false;
    }
    
    @Override
    public boolean generateChunk(final Chunk chunk) {
        if (this.getRegion(chunk).isPresent()) {
            Bukkit.getPluginManager().callEvent((Event)new MiniMapUpdateEvent(chunk));
            return false;
        }
        final Location min = chunk.getBlock(0, 0, 0).getLocation();
        final Location max = chunk.getBlock(15, 255, 15).getLocation();
        final String name = "c_" + chunk.getX() + "_" + chunk.getZ();
        final ProtectedRegion rg = new ProtectedCuboidRegion(name, new BlockVector(min.getBlockX(), min.getBlockY(), min.getBlockZ()), new BlockVector(max.getBlockX(), max.getBlockY(), max.getBlockZ()));
        this.cleanFlags(Optional.of(rg));
        this.addRegion(Optional.of(rg), chunk.getWorld());
        this.safeChanges(chunk.getWorld());
        Bukkit.getServer().getPluginManager().callEvent((Event)new ChunkRegionCreateEvent(chunk));
        return true;
    }
    
    @Override
    public boolean safeChanges(final World world) {
        try {
            this.worldGuardPlugin.getRegionManager(world).saveChanges();
            return true;
        }
        catch (StorageException e) {
            return false;
        }
    }
    
    private void setFlags(final ProtectedRegion rg, final RegionGroup g, final StateFlag.State state) {
        rg.setFlag((Flag)DefaultFlag.INTERACT, (Object)state);
        rg.setFlag((Flag)DefaultFlag.INTERACT.getRegionGroupFlag(), (Object)g);
        rg.setFlag((Flag)DefaultFlag.BLOCK_BREAK, (Object)state);
        rg.setFlag((Flag)DefaultFlag.BLOCK_BREAK.getRegionGroupFlag(), (Object)g);
        rg.setFlag((Flag)DefaultFlag.BUILD, (Object)state);
        rg.setFlag((Flag)DefaultFlag.BUILD.getRegionGroupFlag(), (Object)g);
        rg.setFlag((Flag)DefaultFlag.DESTROY_VEHICLE, (Object)state);
        rg.setFlag((Flag)DefaultFlag.DESTROY_VEHICLE.getRegionGroupFlag(), (Object)g);
        rg.setFlag((Flag)DefaultFlag.BLOCK_PLACE, (Object)state);
        rg.setFlag((Flag)DefaultFlag.BLOCK_PLACE.getRegionGroupFlag(), (Object)g);
        rg.setFlag((Flag)DefaultFlag.USE, (Object)state);
        rg.setFlag((Flag)DefaultFlag.USE.getRegionGroupFlag(), (Object)g);
        rg.setFlag((Flag)DefaultFlag.ENTITY_ITEM_FRAME_DESTROY, (Object)state);
        rg.setFlag((Flag)DefaultFlag.ENTITY_ITEM_FRAME_DESTROY.getRegionGroupFlag(), (Object)g);
        rg.setFlag((Flag)DefaultFlag.ENTITY_PAINTING_DESTROY, (Object)state);
        rg.setFlag((Flag)DefaultFlag.ENTITY_PAINTING_DESTROY.getRegionGroupFlag(), (Object)g);
        rg.setFlag((Flag)DefaultFlag.CHEST_ACCESS, (Object)state);
        rg.setFlag((Flag)DefaultFlag.CHEST_ACCESS.getRegionGroupFlag(), (Object)g);
    }
}
