package de.ysl3000.chunkguard.commands;

import de.ysl3000.chunkguard.*;
import de.ysl3000.chunkguard.lib.interfaces.*;
import de.ysl3000.chunkguard.config.*;
import de.ysl3000.chunkguard.lib.inventory.*;
import org.bukkit.plugin.*;
import org.bukkit.event.inventory.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import de.ysl3000.chunkguard.events.*;
import org.bukkit.inventory.*;
import org.bukkit.event.*;

public class UserBuy implements Listener
{
    private final String key = "ChunkGuard:buy.item";
    private ChunkGuardPlugin chunkGuardPlugin;
    private IBuyingAdapter buyingAdapter;
    private IMessageAdapter messageAdapter;
    private Config config;
    private MenuInventoryConfig menuInventoryConfig;
    
    public UserBuy(final ChunkGuardPlugin plugin) {
        this.menuInventoryConfig = plugin.getMenuConfig();
        this.chunkGuardPlugin = plugin;
        this.buyingAdapter = plugin.getBuyingAdapter();
        this.messageAdapter = plugin.getMessageAdapter();
        this.config = plugin.getConfiguration();
        plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }
    
    @EventHandler
    public void onBuyInventoryClick(final InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getClickedInventory().getName().equals(this.menuInventoryConfig.BuyMenuName())) {
            event.setCancelled(true);
            final ItemStack clickedStack = event.getCurrentItem();
            if (clickedStack == null) {
                return;
            }
            if (clickedStack.getType() == Material.AIR) {
                return;
            }
            if (event.getWhoClicked() instanceof Player) {
                final String displayName = clickedStack.getItemMeta().getDisplayName();
                switch (displayName) {
                    case "Akzeptieren": {
                        final Player p = (Player)event.getWhoClicked();
                        if (this.buyingAdapter.buyFromBank(p, p.getLocation())) {
                            Bukkit.getServer().getPluginManager().callEvent((Event)new ChunkBuyFromServerEvent(p, p.getLocation().getChunk()));
                            p.sendMessage(this.messageAdapter.youNowOwnThisChunk());
                            Bukkit.getServer().getPluginManager().callEvent((Event)new ChunkBuyFromServerEvent((OfflinePlayer)event.getWhoClicked(), event.getWhoClicked().getLocation().getChunk()));
                            break;
                        }
                        p.sendMessage(this.messageAdapter.buyingTransactionFailed());
                        break;
                    }
                    case "Abbrechen": {
                        event.getWhoClicked().sendMessage(this.messageAdapter.getCanceled());
                        break;
                    }
                }
                event.getWhoClicked().closeInventory();
            }
        }
    }
}
