package de.ysl3000.chunkguard.inventory;

import de.ysl3000.chunkguard.*;
import de.ysl3000.chunkguard.lib.interfaces.*;
import de.ysl3000.chunkguard.lib.inventory.*;
import org.bukkit.plugin.*;
import org.bukkit.event.inventory.*;
import org.bukkit.*;
import com.sk89q.worldguard.protection.regions.*;
import com.sk89q.worldguard.protection.flags.*;
import org.bukkit.inventory.*;
import java.util.*;
import org.bukkit.event.*;

public class FlagInventory implements Listener
{
    private ChunkGuardPlugin plugin;
    private IWorldGuardAdapter worldGuardAdapter;
    private MenuInventoryConfig config;
    private IMessageAdapter messageAdapter;
    private ItemStackmodifier itemStackmodifier;
    
    public FlagInventory(final ChunkGuardPlugin plugin) {
        this.plugin = plugin;
        this.worldGuardAdapter = plugin.getWorldGuardAdapter();
        this.config = plugin.getMenuConfig();
        this.messageAdapter = plugin.getMessageAdapter();
        this.itemStackmodifier = plugin.getItemStackmodifier();
        this.plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }
    
    @EventHandler
    public void onFlagClick(final InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getClickedInventory().getName().equals(this.config.getFlagsInfo())) {
            event.setCancelled(true);
            final ItemStack clickedStack = event.getCurrentItem();
            if (clickedStack == null) {
                return;
            }
            if (clickedStack.getType() == Material.AIR) {
                return;
            }
            event.getWhoClicked().openInventory(this.itemStackmodifier.createStateInventory(this.config, clickedStack.getItemMeta().getDisplayName()));
        }
        else if (event.getClickedInventory().getName().equals(this.config.getFlagInvName())) {
            event.setCancelled(true);
            final ItemStack clickedStack = event.getCurrentItem();
            if (clickedStack == null) {
                return;
            }
            if (clickedStack.getType() == Material.AIR) {
                return;
            }
            final String flagName = clickedStack.getItemMeta().getLore().get(0);
            final String state = clickedStack.getItemMeta().getDisplayName();
            final Optional<ProtectedRegion> region = this.worldGuardAdapter.getRegion(event.getWhoClicked().getLocation());
            if (region.isPresent()) {
                final Flag flag = DefaultFlag.fuzzyMatchFlag(flagName);
                if (flag instanceof StateFlag) {
                    final StateFlag flag2 = (StateFlag)flag;
                    region.get().setFlag((Flag)flag2, (Object)StateFlag.State.valueOf(state));
                    this.worldGuardAdapter.safeChanges(event.getWhoClicked().getWorld());
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().sendMessage(this.messageAdapter.getFlagSet().replace("{flag}", flagName).replace("{state}", state));
                }
            }
        }
    }
}
