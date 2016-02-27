package de.ysl3000.chunkguard.lib.inventory;

import org.bukkit.plugin.java.*;
import java.util.*;
import org.bukkit.plugin.*;
import org.bukkit.event.inventory.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.*;
import org.bukkit.inventory.*;

public abstract class ClickableInventory<T extends JavaPlugin> implements Listener
{
    protected String inventoryName;
    private HashMap<Integer, ClickableItem> items;
    
    public ClickableInventory(final T plugin, final String inventoryName) {
        this.items = new HashMap<Integer, ClickableItem>();
        plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
        this.inventoryName = inventoryName;
    }
    
    public void setClickableItem(final int position, final ClickableItem item) {
        this.items.put(position, item);
    }
    
    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        if (event.getWhoClicked() == null) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getClickedInventory().getName().equals(this.inventoryName) && event.getCurrentItem() != null) {
            event.setCancelled(true);
            this.items.get(event.getSlot()).run((Player)event.getWhoClicked());
        }
    }
    
    public Inventory getInventory() {
        int size = this.items.size() / 9;
        if (this.items.size() % 9 != 0) {
            ++size;
        }
        size *= 9;
        if (size == 0) {
            size = 9;
        }
        final Inventory inventory = Bukkit.createInventory((InventoryHolder)null, size, this.inventoryName);
        this.items.forEach((position, stack) -> inventory.setItem((int)position, stack.getItemStack()));
        return inventory;
    }
}
