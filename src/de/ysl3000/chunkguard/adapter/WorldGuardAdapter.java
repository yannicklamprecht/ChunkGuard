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
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.ysl3000.chunkguard.customflags.OverFlowFlag;
import de.ysl3000.chunkguard.lib.interfaces.IWorldGuardAdapter;
import org.bukkit.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class WorldGuardAdapter implements IWorldGuardAdapter {
    private WorldGuardPlugin worldGuardPlugin;

    public WorldGuardAdapter(final WorldGuardPlugin plugin) {
        this.worldGuardPlugin = plugin;
    }

    @Override
    public boolean isBuyable(final Location loc) {

        if (this.getRegion(loc).isPresent()) {
            final ProtectedRegion region = this.getRegion(loc).get();
            if (region.getFlag(DefaultFlag.BUYABLE) != null) {
                return region.getFlag(DefaultFlag.BUYABLE).booleanValue();
            }
        }
        return false;
    }


    @Override
    public boolean isOwner(final OfflinePlayer player, final Location location) {
        return this.getRegion(location).isPresent() && this.getRegion(location).get().isOwner(this.worldGuardPlugin.wrapOfflinePlayer(player));
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
            Optional<ProtectedRegion> region = this.getRegion(location);
            if (region.isPresent()) {
                region.get().setOwners(domain);
                return region.get().isOwner(localPlayer);
            }
        }
        return false;
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
    public boolean addMember(final OfflinePlayer player, final Location location) {
        if (this.getRegion(location).isPresent()) {
            final LocalPlayer localPlayer = this.worldGuardPlugin.wrapOfflinePlayer(player);
            this.getRegion(location).get().getMembers().addPlayer(localPlayer);
            return this.getRegion(location).get().isMember(localPlayer);
        }
        return false;
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
    public boolean cleanMembers(final Location location) {
        if (this.getRegion(location).isPresent()) {
            this.getRegion(location).get().getMembers().removeAll();
            return this.getRegion(location).get().getMembers().size() == 0;
        }
        return false;
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
    public Optional<OfflinePlayer> getOwner(final Location location) {
        Optional<OfflinePlayer> owner = Optional.empty();
        if (this.getRegion(location).isPresent()) {
            final Optional<UUID> ownerA = this.getRegion(location).get().getOwners().getUniqueIds().stream().findFirst();
            if (ownerA.isPresent()) {
                owner = Optional.of(Bukkit.getOfflinePlayer((UUID) ownerA.get()));
            }
        }
        return owner;
    }


    @Override
    public Optional<ProtectedRegion> getRegion(final Location location) {
        final Optional<ProtectedRegion> region = Optional.empty();
        final ApplicableRegionSet regions = this.worldGuardPlugin.getRegionManager(location.getWorld()).getApplicableRegions(location);
        return regions.getRegions().stream().findFirst();
    }


    @Override
    public boolean cleanFlags(final Location location) {
        return this.cleanFlags(this.getRegion(location));
    }


    @Override
    public boolean cleanFlags(final Optional<ProtectedRegion> region) {
        if (region.isPresent()) {
            final ProtectedRegion rg = region.get();
            this.setFlags(rg, RegionGroup.NON_MEMBERS, StateFlag.State.DENY);
            rg.setFlag(DefaultFlag.LAVA_FIRE, StateFlag.State.DENY);
            rg.setFlag(DefaultFlag.FIRE_SPREAD, StateFlag.State.DENY);
            rg.setFlag(DefaultFlag.OTHER_EXPLOSION, StateFlag.State.DENY);
            rg.setFlag(DefaultFlag.GHAST_FIREBALL, StateFlag.State.DENY);
            rg.setFlag(DefaultFlag.ENDERDRAGON_BLOCK_DAMAGE, StateFlag.State.DENY);
            rg.setFlag(DefaultFlag.CREEPER_EXPLOSION, StateFlag.State.DENY);
            rg.setFlag(DefaultFlag.BUYABLE, true);
            rg.setFlag(DefaultFlag.PVP, StateFlag.State.DENY);
            rg.setFlag(OverFlowFlag.LAVA_OVERFLOW, StateFlag.State.DENY);
            rg.setFlag(OverFlowFlag.WATER_OVERFLOW, StateFlag.State.DENY);
            return true;
        }
        return false;
    }

    @Override
    public boolean addRegion(final Optional<ProtectedRegion> region, final World world) {
        if (region.isPresent()) {
            this.worldGuardPlugin.getRegionManager(world).addRegion(region.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean safeChanges(final World world) {
        try {
            this.worldGuardPlugin.getRegionManager(world).saveChanges();
            return true;
        } catch (StorageException e) {
            return false;
        }
    }

    @Override
    public Flag fuzzyMatchFlag(String flag) {
        return DefaultFlag.fuzzyMatchFlag(worldGuardPlugin.getFlagRegistry(), flag);
    }

    private void setFlags(final ProtectedRegion rg, final RegionGroup g, final StateFlag.State state) {
        rg.setFlag(DefaultFlag.INTERACT, state);
        rg.setFlag(DefaultFlag.INTERACT.getRegionGroupFlag(), g);
        rg.setFlag(DefaultFlag.BLOCK_BREAK, state);
        rg.setFlag(DefaultFlag.BLOCK_BREAK.getRegionGroupFlag(), g);
        rg.setFlag(DefaultFlag.BUILD, state);
        rg.setFlag(DefaultFlag.BUILD.getRegionGroupFlag(), g);
        rg.setFlag(DefaultFlag.DESTROY_VEHICLE, state);
        rg.setFlag(DefaultFlag.DESTROY_VEHICLE.getRegionGroupFlag(), g);
        rg.setFlag(DefaultFlag.BLOCK_PLACE, state);
        rg.setFlag(DefaultFlag.BLOCK_PLACE.getRegionGroupFlag(), g);
        rg.setFlag(DefaultFlag.USE, state);
        rg.setFlag(DefaultFlag.USE.getRegionGroupFlag(), g);
        rg.setFlag(DefaultFlag.ENTITY_ITEM_FRAME_DESTROY, state);
        rg.setFlag(DefaultFlag.ENTITY_ITEM_FRAME_DESTROY.getRegionGroupFlag(), g);
        rg.setFlag(DefaultFlag.ENTITY_PAINTING_DESTROY, state);
        rg.setFlag(DefaultFlag.ENTITY_PAINTING_DESTROY.getRegionGroupFlag(), g);
        rg.setFlag(DefaultFlag.CHEST_ACCESS, state);
        rg.setFlag(DefaultFlag.CHEST_ACCESS.getRegionGroupFlag(), g);
    }

}
