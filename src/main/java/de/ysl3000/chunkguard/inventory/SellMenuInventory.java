package de.ysl3000.chunkguard.inventory;

import de.ysl3000.chunkguard.ChunkGuardPlugin;
import de.ysl3000.chunkguard.events.ChunkSellToServerEvent;
import de.ysl3000.chunkguard.lib.interfaces.IBuyingAdapter;
import de.ysl3000.chunkguard.lib.interfaces.IMessageAdapter;
import de.ysl3000.chunkguard.lib.inventory.MenuInventoryConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SellMenuInventory implements Listener {

    private IBuyingAdapter buyingAdapter;
    private IMessageAdapter messageAdapter;
    private MenuInventoryConfig config;

    public SellMenuInventory(final ChunkGuardPlugin plugin) {
        this.messageAdapter = plugin.getMessageAdapter();
        this.buyingAdapter = plugin.getBuyingAdapter();
        this.config = plugin.getMenuConfig();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onSellInteract(final InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getView().getTitle().equals(this.config.SellMenuName())) {
            event.setCancelled(true);
            final ItemStack clickedStack = event.getCurrentItem();
            if (clickedStack == null) {
                return;
            }
            if (clickedStack.getType() == Material.AIR) {
                return;
            }
            final String displayName = clickedStack.getItemMeta().getDisplayName();
            if ("Akzeptieren".equals(displayName)) {
                this.buyingAdapter.sellToBank((OfflinePlayer) event.getWhoClicked(), event.getWhoClicked().getLocation());
                event.getWhoClicked().sendMessage(this.messageAdapter.getChunkSoldSuccessfully());
                Bukkit.getServer().getPluginManager().callEvent(new ChunkSellToServerEvent((OfflinePlayer) event.getWhoClicked(), event.getWhoClicked().getLocation().getChunk()));
            } else if ("Abbrechen".equals(displayName)) {
                event.getWhoClicked().sendMessage(this.messageAdapter.getCanceled());
            }
            event.getWhoClicked().closeInventory();
        }
    }
}
