package de.ysl3000.chunkguard.commands;

import de.ysl3000.chunkguard.ChunkGuardPlugin;
import de.ysl3000.chunkguard.events.ChunkBuyFromServerEvent;
import de.ysl3000.chunkguard.lib.interfaces.IBuyingAdapter;
import de.ysl3000.chunkguard.lib.interfaces.IMessageAdapter;
import de.ysl3000.chunkguard.lib.inventory.MenuInventoryConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class UserBuy implements Listener
{

    private IBuyingAdapter buyingAdapter;
    private IMessageAdapter messageAdapter;
    private MenuInventoryConfig menuInventoryConfig;
    
    public UserBuy(final ChunkGuardPlugin plugin) {
        this.menuInventoryConfig = plugin.getMenuConfig();
        this.buyingAdapter = plugin.getBuyingAdapter();
        this.messageAdapter = plugin.getMessageAdapter();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onBuyInventoryClick(final InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getView().getTitle().equals(this.menuInventoryConfig.BuyMenuName())) {
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
                    case "Akzeptieren":
                        final Player p = (Player)event.getWhoClicked();
                        if (this.buyingAdapter.buyFromBank(p, p.getLocation())) {
                            Bukkit.getServer().getPluginManager().callEvent(new ChunkBuyFromServerEvent(p, p.getLocation().getChunk()));
                            p.sendMessage(this.messageAdapter.youNowOwnThisChunk());
                            Bukkit.getServer().getPluginManager().callEvent(new ChunkBuyFromServerEvent((OfflinePlayer)event.getWhoClicked(), event.getWhoClicked().getLocation().getChunk()));
                            break;
                        }
                        p.sendMessage(this.messageAdapter.buyingTransactionFailed());
                        break;
                    case "Abbrechen":
                        event.getWhoClicked().sendMessage(this.messageAdapter.getCanceled());
                        break;
                }
                event.getWhoClicked().closeInventory();
            }
        }
    }
}
