package de.ysl3000.chunkguard.listener;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.ysl3000.chunkguard.ChunkGuardPlugin;
import de.ysl3000.chunkguard.events.ChunkChangeOwnerEvent;
import de.ysl3000.chunkguard.lib.interfaces.I7WorldGuardAdapter;
import de.ysl3000.chunkguard.lib.interfaces.IMessageAdapter;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class UserSellListener implements Listener {

    private ChunkGuardPlugin plugin;
    private IMessageAdapter messageAdapter;
    private I7WorldGuardAdapter worldGuardAdapter;

    public UserSellListener(final ChunkGuardPlugin plugin) {
        this.plugin = plugin;
        this.worldGuardAdapter = plugin.getWorldGuardAdapter();
        this.messageAdapter = plugin.getMessageAdapter();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onSignInteract(final PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            final Block block = e.getClickedBlock();
            if (Tag.SIGNS.isTagged(block.getType()) && block.hasMetadata("ChunkGuard:cgsell.prize") && block.hasMetadata("ChunkGuard:cgsell.owner")) {
                final double prize = block.getMetadata("ChunkGuard:cgsell.prize").get(0).asDouble();
                final Optional<UUID> owner = Optional.of(UUID.fromString(block.getMetadata("ChunkGuard:cgsell.owner").get(0).asString()));
                if (owner.get().equals(e.getPlayer().getUniqueId())) {
                    e.getPlayer().sendMessage(this.plugin.getMessageAdapter().youCouldNotBuyYourOwnChunk());
                    e.setCancelled(true);
                    return;
                }
                if (this.plugin.getBuyingAdapter().buyFromUser(e.getPlayer(), block.getLocation(), prize)) {
                    Bukkit.getServer().getPluginManager().callEvent(new ChunkChangeOwnerEvent(Bukkit.getOfflinePlayer(owner.get()), e.getPlayer(), e.getPlayer().getLocation().getChunk(), prize));
                    e.getPlayer().sendMessage(this.plugin.getMessageAdapter().youNowOwnThisChunk());
                    e.getClickedBlock().setType(Material.AIR);
                } else {
                    e.getPlayer().sendMessage(this.plugin.getMessageAdapter().notEnoughMoney());
                }
                e.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onSignCreate(final SignChangeEvent e) {
        double prize = 200.0;
        if (e.getLine(0).equals("[cgsell]")) {
            try {
                prize = Double.valueOf(e.getLine(1));
            } catch (NumberFormatException ex) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(this.messageAdapter.musBeANumber().replace("{input}", e.getLine(1)));
            }
            final Optional<ProtectedRegion> region = this.worldGuardAdapter.getRegion(e.getBlock().getLocation());
            if (region.isPresent()) {
                if (this.worldGuardAdapter.isOwner(e.getPlayer(), e.getBlock().getLocation())) {
                    e.getBlock().setMetadata("ChunkGuard:cgsell.prize", new FixedMetadataValue(this.plugin, prize));
                    e.getBlock().setMetadata("ChunkGuard:cgsell.owner", new FixedMetadataValue(this.plugin, this.worldGuardAdapter.getOwner(e.getBlock().getLocation()).get().getUniqueId()));
                    e.setLine(0, "[ChunkGuard]");
                    e.setLine(1, "Prize: " + prize);
                    e.setLine(2, "Rightclick to buy");
                }
            } else {
                e.getPlayer().sendMessage(this.messageAdapter.noChunkAvailable());
                e.setCancelled(true);
            }
        }
    }
}
