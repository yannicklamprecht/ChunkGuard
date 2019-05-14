package de.ysl3000.chunkguard.lib.inventory;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ClickableInventory<T extends JavaPlugin> implements Listener {

    protected String inventoryName;
    private Map<Integer, ClickableItem> items;

    public ClickableInventory(T plugin, String inventoryName) {
        this.items = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.inventoryName = inventoryName;
    }

    protected void setClickableItem(int position, ClickableItem item) {
        this.items.put(position, item);
    }

    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getView().getTitle().equals(this.inventoryName) && event.getCurrentItem() != null) {
            event.setCancelled(true);
            this.items.get(event.getSlot()).run((Player) event.getWhoClicked());
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
        final Inventory inventory = Bukkit.createInventory(null, size, this.inventoryName);
        this.items.forEach((position, stack) -> inventory.setItem(position, stack.getItemStack()));
        return inventory;
    }
}
