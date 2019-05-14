package de.ysl3000.chunkguard.inventory;

import de.ysl3000.chunkguard.ChunkGuardPlugin;
import de.ysl3000.chunkguard.lib.interfaces.I7WorldGuardAdapter;
import de.ysl3000.chunkguard.lib.interfaces.IMessageAdapter;
import de.ysl3000.chunkguard.lib.inventory.ItemStackmodifier;
import de.ysl3000.chunkguard.lib.inventory.MenuInventoryConfig;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class MemberInventory implements Listener {

    private Map<Player, OfflinePlayer> targets;
    private ChunkGuardPlugin plugin;
    private I7WorldGuardAdapter worldGuardAdapter;
    private MenuInventoryConfig config;
    private IMessageAdapter messageAdapter;
    private ItemStackmodifier itemStackmodifier;

    public MemberInventory(final ChunkGuardPlugin plugin) {
        this.targets = new HashMap<>();
        this.plugin = plugin;
        this.worldGuardAdapter = plugin.getWorldGuardAdapter();
        this.config = plugin.getMenuConfig();
        this.messageAdapter = plugin.getMessageAdapter();
        this.itemStackmodifier = plugin.getItemStackmodifier();
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onFlagInventoryClick(final InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getView().getTitle().equals(this.config.getMemberAddName())) {
            final ItemStack clickedStack = event.getCurrentItem();
            event.setCancelled(true);
            if (clickedStack == null) {
                return;
            }
            if (clickedStack.getType() == Material.AIR) {
                return;
            }
            final SkullMeta sk = (SkullMeta) clickedStack.getItemMeta();
            this.worldGuardAdapter.addMember(sk.getOwningPlayer(), event.getWhoClicked().getLocation());
            event.getWhoClicked().closeInventory();
            event.getWhoClicked().sendMessage(this.messageAdapter.getMemberAddedSuccessfully());
        } else if (event.getView().getTitle().equals(this.config.getMemberRemoveName())) {
            final ItemStack clickedStack = event.getCurrentItem();
            event.setCancelled(true);
            if (clickedStack == null) {
                return;
            }
            if (clickedStack.getType() == Material.AIR) {
                return;
            }
            final SkullMeta sk = (SkullMeta) clickedStack.getItemMeta();
            this.targets.put((Player) event.getWhoClicked(), sk.getOwningPlayer());
            event.getWhoClicked().openInventory(this.itemStackmodifier.createAcceptDecline(this.config.getAccept()));
        } else if (event.getView().getTitle().equals(this.config.getAccept())) {
            final ItemStack clickedStack = event.getCurrentItem();
            event.setCancelled(true);
            if (clickedStack == null) {
                return;
            }
            if (clickedStack.getType() == Material.AIR) {
                return;
            }
            final String displayName = clickedStack.getItemMeta().getDisplayName();
            if ("Akzeptieren".equals(displayName)) {
                this.worldGuardAdapter.removeMember(this.targets.get(event.getWhoClicked()), event.getWhoClicked().getLocation());
                event.getWhoClicked().sendMessage(this.messageAdapter.getMemberRemovedSuccessfully());
            } else if ("Abbrechen".equals(displayName)) {
                event.getWhoClicked().sendMessage(this.messageAdapter.getCanceled());
            }
            event.getWhoClicked().closeInventory();
            this.targets.remove(event.getWhoClicked());
        }
    }
}
