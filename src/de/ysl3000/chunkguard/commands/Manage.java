package de.ysl3000.chunkguard.commands;

import de.ysl3000.chunkguard.ChunkGuardPlugin;
import de.ysl3000.chunkguard.lib.interfaces.IChunkGuardAdapter;
import de.ysl3000.chunkguard.lib.inventory.ItemStackmodifier;
import de.ysl3000.chunkguard.lib.inventory.MenuInventoryConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class Manage implements CommandExecutor, Listener
{
    private final String invName = "ChunkGuard-Men\u00fc";
    private ChunkGuardPlugin chunkGuardPlugin;
    private IChunkGuardAdapter worldGuardAdapter;
    private Inventory menu;
    private ItemStackmodifier itemStackmodifier;
    private MenuInventoryConfig config;
    
    public Manage(final ChunkGuardPlugin plugin) {
        this.chunkGuardPlugin = plugin;
        this.itemStackmodifier = plugin.getItemStackmodifier();
        this.worldGuardAdapter = this.chunkGuardPlugin.getWorldGuardAdapter();
        this.config = plugin.getMenuConfig();
        this.chunkGuardPlugin.getCommand("cgmanage").setExecutor(this);
        this.chunkGuardPlugin.getServer().getPluginManager().registerEvents(this, this.chunkGuardPlugin);
    }
    
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings) {
        if (commandSender instanceof Player) {
            final Player p = (Player)commandSender;
            if (!this.worldGuardAdapter.getRegion(p.getLocation()).isPresent()) {
                this.worldGuardAdapter.generateChunk(p.getLocation().getChunk());
            }
            this.menu = Bukkit.createInventory(null, 9, "ChunkGuard-Men\u00fc");
            if (this.worldGuardAdapter.getMembers(p.getLocation()).size() == 0 && !this.worldGuardAdapter.hasOwner(p.getLocation())) {
                this.menu.setItem(0, this.itemStackmodifier.create(ChunkGuardMainItems.BUY.key, ItemStackmodifier.ItemState.ENABLED, this.config.BuyMenuName(), this.config.getBuyMenuDescription()));
                p.openInventory(this.menu);
            }
            else if (this.worldGuardAdapter.isOwner((OfflinePlayer)p, p.getLocation())) {
                this.menu.setItem(0, this.itemStackmodifier.create(ChunkGuardMainItems.ADD.key, ItemStackmodifier.ItemState.ENABLED, this.config.getMemberAddName(), this.config.getMemberAddDescription()));
                this.menu.setItem(1, this.itemStackmodifier.create(ChunkGuardMainItems.REMOVE.key, ItemStackmodifier.ItemState.ENABLED, this.config.getMemberRemoveName(), this.config.getMemberRemoveDescription()));
                this.menu.setItem(2, this.itemStackmodifier.create(ChunkGuardMainItems.FLAGS.key, ItemStackmodifier.ItemState.ENABLED, this.config.getFlagsInfo(), this.config.getFlagsDescription()));
                this.menu.setItem(3, this.itemStackmodifier.create(ChunkGuardMainItems.SELL.key, ItemStackmodifier.ItemState.ENABLED, this.config.SellMenuName(), this.config.getSellMenuDescription()));
                p.openInventory(this.menu);
            }
            else {
                p.sendMessage(this.chunkGuardPlugin.getMessageAdapter().notYourChunk());
            }
        }
        return true;
    }
    
    @EventHandler
    public void onItemClick(final InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getClickedInventory().getName().equals("ChunkGuard-Men\u00fc")) {
            final ItemStack clickedStack = event.getCurrentItem();
            event.setCancelled(true);
            if (clickedStack == null) {
                return;
            }
            if (clickedStack.getType() == Material.AIR) {
                return;
            }
            if (this.itemStackmodifier.hasKey(clickedStack, ChunkGuardMainItems.ADD)) {
                if (this.itemStackmodifier.getState(clickedStack, ChunkGuardMainItems.ADD) == ItemStackmodifier.ItemState.ENABLED && this.worldGuardAdapter.getRegion(event.getWhoClicked().getLocation()).isPresent()) {
                    final Set<OfflinePlayer> players = new HashSet<>();
                    event.getWhoClicked().getNearbyEntities(5.0, 5.0, 5.0).stream().filter(p -> p.getType() == EntityType.PLAYER).forEach(onp -> players.add((Player)onp));
                    event.getWhoClicked().openInventory(this.itemStackmodifier.createSkull(players, this.config.getMemberAddName()));
                }
            }
            else if (this.itemStackmodifier.hasKey(clickedStack, ChunkGuardMainItems.REMOVE)) {
                if (this.itemStackmodifier.getState(clickedStack, ChunkGuardMainItems.REMOVE) == ItemStackmodifier.ItemState.ENABLED) {
                    event.getWhoClicked().openInventory(this.itemStackmodifier.createSkull(this.worldGuardAdapter.getMembers(event.getWhoClicked().getLocation()), this.config.getMemberRemoveName()));
                }
            }
            else if (this.itemStackmodifier.hasKey(clickedStack, ChunkGuardMainItems.FLAGS)) {
                if (this.itemStackmodifier.getState(clickedStack, ChunkGuardMainItems.FLAGS) == ItemStackmodifier.ItemState.ENABLED && this.worldGuardAdapter.getRegion(event.getWhoClicked().getLocation()).isPresent()) {
                    event.getWhoClicked().openInventory(this.itemStackmodifier.create(this.chunkGuardPlugin.getWG(), event.getWhoClicked().getLocation(), this.worldGuardAdapter.getRegion(event.getWhoClicked().getLocation()).get(), this.config.getFlagsInfo()));
                }
            }
            else if (this.itemStackmodifier.hasKey(clickedStack, ChunkGuardMainItems.SELL)) {
                if (this.itemStackmodifier.getState(clickedStack, ChunkGuardMainItems.SELL) == ItemStackmodifier.ItemState.ENABLED && this.worldGuardAdapter.isOwner((OfflinePlayer)event.getWhoClicked(), event.getWhoClicked().getLocation())) {
                    event.getWhoClicked().openInventory(this.itemStackmodifier.createAcceptDecline(this.config.SellMenuName()));
                }
            }
            else if (this.itemStackmodifier.hasKey(clickedStack, ChunkGuardMainItems.BUY) && this.itemStackmodifier.getState(clickedStack, ChunkGuardMainItems.BUY) == ItemStackmodifier.ItemState.ENABLED && !this.worldGuardAdapter.hasOwner(event.getWhoClicked().getLocation())) {
                event.getWhoClicked().openInventory(this.itemStackmodifier.createAcceptDecline(this.config.BuyMenuName()));
            }
        }
        else if (event.getInventory().getName().equals("ChunkGuard-Men\u00fc")) {
            event.setCancelled(true);
        }
    }
    
    public enum ChunkGuardMainItems
    {
        ADD("chunkguard.members.add"), 
        REMOVE("chunkguard.members.remove"), 
        FLAGS("chunkguard.flags.manage"), 
        SELL("chunkguard.sell"), 
        BUY("chunkguard.buy");
        
        private String key;
        
        private ChunkGuardMainItems(final String key) {
            this.key = key;
        }
        
        public String getKey() {
            return this.key;
        }
    }
}
