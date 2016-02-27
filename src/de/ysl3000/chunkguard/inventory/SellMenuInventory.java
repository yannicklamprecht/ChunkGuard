package de.ysl3000.chunkguard.inventory;

import de.ysl3000.chunkguard.*;
import de.ysl3000.chunkguard.lib.interfaces.*;
import de.ysl3000.chunkguard.lib.inventory.*;
import org.bukkit.plugin.*;
import org.bukkit.event.inventory.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import de.ysl3000.chunkguard.events.*;
import org.bukkit.inventory.*;
import org.bukkit.event.*;

public class SellMenuInventory implements Listener
{
    private ChunkGuardPlugin plugin;
    private IBuyingAdapter buyingAdapter;
    private IMessageAdapter messageAdapter;
    private MenuInventoryConfig config;
    private ItemStackmodifier itemStackmodifier;
    
    public SellMenuInventory(final ChunkGuardPlugin plugin) {
        this.plugin = plugin;
        this.messageAdapter = plugin.getMessageAdapter();
        this.buyingAdapter = plugin.getBuyingAdapter();
        this.config = plugin.getMenuConfig();
        this.itemStackmodifier = plugin.getItemStackmodifier();
        this.plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }
    
    @EventHandler
    public void onSellInteract(final InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getClickedInventory().getName().equals(this.config.SellMenuName())) {
            event.setCancelled(true);
            final ItemStack clickedStack = event.getCurrentItem();
            if (clickedStack == null) {
                return;
            }
            if (clickedStack.getType() == Material.AIR) {
                return;
            }
            final String displayName = clickedStack.getItemMeta().getDisplayName();
            switch (displayName) {
                case "Akzeptieren": {
                    this.buyingAdapter.sellToBank((OfflinePlayer)event.getWhoClicked(), event.getWhoClicked().getLocation());
                    event.getWhoClicked().sendMessage(this.messageAdapter.getChunkSoldSuccessfully());
                    Bukkit.getServer().getPluginManager().callEvent((Event)new ChunkSellToServerEvent((OfflinePlayer)event.getWhoClicked(), event.getWhoClicked().getLocation().getChunk()));
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
