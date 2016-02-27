package de.ysl3000.chunkguard.listener;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.ysl3000.chunkguard.ChunkGuardPlugin;
import de.ysl3000.chunkguard.events.ChunkChangeOwnerEvent;
import de.ysl3000.chunkguard.lib.interfaces.IMessageAdapter;
import de.ysl3000.chunkguard.lib.interfaces.IWorldGuardAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.UUID;

public class UserSellListener implements Listener
{
    private final String prizeKey = "ChunkGuard:cgsell.prize";
    private final String ownerKey = "ChunkGuard:cgsell.owner";
    private ChunkGuardPlugin plugin;
    private IMessageAdapter messageAdapter;
    private IWorldGuardAdapter worldGuardAdapter;
    
    public UserSellListener(final ChunkGuardPlugin plugin) {
        this.plugin = plugin;
        this.worldGuardAdapter = plugin.getWorldGuardAdapter();
        this.messageAdapter = plugin.getMessageAdapter();
        plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }
    
    @EventHandler
    public void onSignInteract(final PlayerInteractEvent e) {
        if (e.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK)) {
            final Block block = e.getClickedBlock();
            if ((block.getType().equals((Object)Material.SIGN_POST) || block.getType().equals((Object)Material.WALL_SIGN)) && block.hasMetadata("ChunkGuard:cgsell.prize") && block.hasMetadata("ChunkGuard:cgsell.owner")) {
                final double prize = block.getMetadata("ChunkGuard:cgsell.prize").get(0).asDouble();
                final Optional<UUID> owner = Optional.of(UUID.fromString(block.getMetadata("ChunkGuard:cgsell.owner").get(0).asString()));
                if (owner.isPresent()) {
                    if (owner.get().equals(e.getPlayer().getUniqueId())) {
                        e.getPlayer().sendMessage(this.plugin.getMessageAdapter().youCouldNotBuyYourOwnChunk());
                        e.setCancelled(true);
                        return;
                    }
                    if (this.plugin.getBuyingAdapter().buyFromUser((OfflinePlayer)e.getPlayer(), block.getLocation(), prize)) {
                        Bukkit.getServer().getPluginManager().callEvent((Event)new ChunkChangeOwnerEvent(Bukkit.getOfflinePlayer((UUID)owner.get()), (OfflinePlayer)e.getPlayer(), e.getPlayer().getLocation().getChunk(), prize));
                        e.getPlayer().sendMessage(this.plugin.getMessageAdapter().youNowOwnThisChunk());
                        e.getClickedBlock().setType(Material.AIR);
                    }
                    else {
                        e.getPlayer().sendMessage(this.plugin.getMessageAdapter().notEnoughMoney());
                    }
                    e.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onSignCreate(final SignChangeEvent e) {
        double prize = 200.0;
        if (e.getLine(0).equals("[cgsell]")) {
            try {
                prize = Double.valueOf(e.getLine(1));
            }
            catch (NumberFormatException ex) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(this.messageAdapter.musBeANumber().replace("{input}", e.getLine(1)));
            }
            final Optional<ProtectedRegion> region = this.worldGuardAdapter.getRegion(e.getBlock().getLocation());
            if (region.isPresent()) {
                if (this.worldGuardAdapter.isOwner((OfflinePlayer)e.getPlayer(), e.getBlock().getLocation())) {
                    e.getBlock().setMetadata("ChunkGuard:cgsell.prize", (MetadataValue)new FixedMetadataValue((Plugin)this.plugin, (Object)prize));
                    e.getBlock().setMetadata("ChunkGuard:cgsell.owner", (MetadataValue)new FixedMetadataValue((Plugin)this.plugin, (Object)this.worldGuardAdapter.getOwner(e.getBlock().getLocation()).get().getUniqueId()));
                    e.setLine(0, "[ChunkGuard]");
                    e.setLine(1, "Prize: " + prize);
                    e.setLine(2, "Rightclick to buy");
                }
            }
            else {
                e.getPlayer().sendMessage(this.messageAdapter.noChunkAvailable());
                e.setCancelled(true);
            }
        }
    }
}
