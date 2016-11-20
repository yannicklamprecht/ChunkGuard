package de.ysl3000.chunkguard.adapter;

import de.ysl3000.chunkguard.*;
import de.ysl3000.chunkguard.lib.interfaces.*;
import de.ysl3000.chunkguard.config.*;
import org.bukkit.*;
import de.ysl3000.chunkguard.lib.*;
import java.util.*;

public class BuyingAdapter implements IBuyingAdapter
{
    private ChunkGuardPlugin plugin;
    private IChunkGuardAdapter worldGuardAdapter;
    private IMoneyAdapter moneyAdapter;
    private Config config;
    
    public BuyingAdapter(final ChunkGuardPlugin plugin) {
        this.plugin = plugin;
        this.moneyAdapter = plugin.getMoneyAdapter();
        this.worldGuardAdapter = plugin.getWorldGuardAdapter();
        this.config = plugin.getConfiguration();
    }
    
    @Override
    public boolean buyFromBank(final OfflinePlayer p, final Chunk chunk) {
        return this.buyFromBank(p, chunk.getBlock(0, 0, 0).getLocation());
    }
    
    @Override
    public boolean buyFromUser(final OfflinePlayer p, final Chunk chunk, final double price) {
        return this.buyFromUser(p, chunk.getBlock(0, 0, 0).getLocation(), price);
    }
    
    @Override
    public boolean sellToBank(final OfflinePlayer p, final Chunk chunk) {
        return this.sellToBank(p, chunk.getBlock(0, 0, 0).getLocation());
    }
    
    @Override
    public boolean buyFromBank(final OfflinePlayer p, final Location location) {
        if (!this.worldGuardAdapter.getRegion(location).isPresent()) {
            this.worldGuardAdapter.generateChunk(location.getChunk());
            this.worldGuardAdapter.safeChanges(location.getWorld());
        }
        return !this.worldGuardAdapter.hasOwner(location) && this.worldGuardAdapter.isBuyable(location) && this.moneyAdapter.buyFromBank(p, this.config.getBuyPriceFromServer()) && this.worldGuardAdapter.cleanFlags(this.worldGuardAdapter.getRegion(location)) && this.worldGuardAdapter.setOwner(p, location);
    }
    
    @Override
    public boolean buyFromUser(final OfflinePlayer p, final Location location, final double price) {
        final Optional<OfflinePlayer> lastOwner = this.worldGuardAdapter.getOwner(location);
        return lastOwner.isPresent() && this.moneyAdapter.pay(p, lastOwner.get(), price) && this.worldGuardAdapter.cleanFlags(this.worldGuardAdapter.getRegion(location)) && this.worldGuardAdapter.setOwner(p, location) && BukkitLib.removeBlock(location.getChunk(), Material.SIGN_POST, Material.WALL_SIGN, Material.CHEST);
    }
    
    @Override
    public boolean sellToBank(final OfflinePlayer p, final Location location) {
        return this.worldGuardAdapter.getRegion(location).isPresent() && this.worldGuardAdapter.isOwner(p, location) && this.moneyAdapter.sellToBank(p, this.config.getSellPriceToServer()) && this.worldGuardAdapter.cleanFlags(location) && this.worldGuardAdapter.cleanOwner(location) && this.worldGuardAdapter.cleanMembers(location) && BukkitLib.removeBlock(location.getChunk(), Material.WALL_SIGN, Material.SIGN_POST, Material.CHEST);
    }
}
