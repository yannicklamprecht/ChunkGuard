package de.ysl3000.chunkguard.inventory;

import java.util.*;
import org.bukkit.entity.*;
import de.ysl3000.chunkguard.*;
import de.ysl3000.chunkguard.lib.interfaces.*;
import de.ysl3000.chunkguard.lib.inventory.*;
import org.bukkit.plugin.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.event.*;

public class MemberInventory implements Listener
{
    private HashMap<Player, OfflinePlayer> targets;
    private ChunkGuardPlugin plugin;
    private IWorldGuardAdapter worldGuardAdapter;
    private MenuInventoryConfig config;
    private IMessageAdapter messageAdapter;
    private ItemStackmodifier itemStackmodifier;
    
    public MemberInventory(final ChunkGuardPlugin plugin) {
        this.targets = new HashMap<Player, OfflinePlayer>();
        this.plugin = plugin;
        this.worldGuardAdapter = plugin.getWorldGuardAdapter();
        this.config = plugin.getMenuConfig();
        this.messageAdapter = plugin.getMessageAdapter();
        this.itemStackmodifier = plugin.getItemStackmodifier();
        this.plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }
    
    @EventHandler
    public void onFlagInventoryClick(final InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getClickedInventory().getName().equals(this.config.getMemberAddName())) {
            final ItemStack clickedStack = event.getCurrentItem();
            event.setCancelled(true);
            if (clickedStack == null) {
                return;
            }
            if (clickedStack.getType() == Material.AIR) {
                return;
            }
            final SkullMeta sk = (SkullMeta)clickedStack.getItemMeta();
            this.worldGuardAdapter.addMember(Bukkit.getOfflinePlayer(sk.getOwner()), event.getWhoClicked().getLocation());
            event.getWhoClicked().closeInventory();
            event.getWhoClicked().sendMessage(this.messageAdapter.getMemberAddedSuccessfully());
        }
        else if (event.getClickedInventory().getName().equals(this.config.getMemberRemoveName())) {
            final ItemStack clickedStack = event.getCurrentItem();
            event.setCancelled(true);
            if (clickedStack == null) {
                return;
            }
            if (clickedStack.getType() == Material.AIR) {
                return;
            }
            final SkullMeta sk = (SkullMeta)clickedStack.getItemMeta();
            this.targets.put((Player)event.getWhoClicked(), Bukkit.getOfflinePlayer(sk.getOwner()));
            event.getWhoClicked().openInventory(this.itemStackmodifier.createAcceptDecline(this.config.getAccept()));
        }
        else if (event.getClickedInventory().getName().equals(this.config.getAccept())) {
            final ItemStack clickedStack = event.getCurrentItem();
            event.setCancelled(true);
            if (clickedStack == null) {
                return;
            }
            if (clickedStack.getType() == Material.AIR) {
                return;
            }
            final String displayName = clickedStack.getItemMeta().getDisplayName();
            switch (displayName) {
                case "Akzeptieren": {
                    this.worldGuardAdapter.removeMember(this.targets.get(event.getWhoClicked()), event.getWhoClicked().getLocation());
                    event.getWhoClicked().sendMessage(this.messageAdapter.getMemberRemovedSuccessfully());
                    break;
                }
                case "Abbrechen": {
                    event.getWhoClicked().sendMessage(this.messageAdapter.getCanceled());
                    break;
                }
            }
            event.getWhoClicked().closeInventory();
            this.targets.remove(event.getWhoClicked());
        }
    }
}
