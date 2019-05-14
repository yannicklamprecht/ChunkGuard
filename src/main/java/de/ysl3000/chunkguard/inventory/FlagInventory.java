package de.ysl3000.chunkguard.inventory;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.ysl3000.chunkguard.ChunkGuardPlugin;
import de.ysl3000.chunkguard.lib.interfaces.I7WorldGuardAdapter;
import de.ysl3000.chunkguard.lib.interfaces.IMessageAdapter;
import de.ysl3000.chunkguard.lib.inventory.ItemStackmodifier;
import de.ysl3000.chunkguard.lib.inventory.MenuInventoryConfig;
import java.util.Optional;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class FlagInventory implements Listener {

    private ChunkGuardPlugin plugin;
    private I7WorldGuardAdapter worldGuardAdapter;
    private MenuInventoryConfig config;
    private IMessageAdapter messageAdapter;
    private ItemStackmodifier itemStackmodifier;

    public FlagInventory(final ChunkGuardPlugin plugin) {
        this.plugin = plugin;
        this.worldGuardAdapter = plugin.getWorldGuardAdapter();
        this.config = plugin.getMenuConfig();
        this.messageAdapter = plugin.getMessageAdapter();
        this.itemStackmodifier = plugin.getItemStackmodifier();
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onFlagClick(final InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getView().getTitle().equals(this.config.getFlagsInfo())) {
            event.setCancelled(true);
            final ItemStack clickedStack = event.getCurrentItem();
            if (clickedStack == null) {
                return;
            }
            if (clickedStack.getType() == Material.AIR) {
                return;
            }
            event.getWhoClicked().openInventory(this.itemStackmodifier.createStateInventory(this.config, clickedStack.getItemMeta().getDisplayName()));
        } else if (event.getView().getTitle().equals(this.config.getFlagInvName())) {
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
                final Flag flag = worldGuardAdapter.fuzzyMatchFlag(flagName);
                if (flag instanceof StateFlag) {
                    final StateFlag flag2 = (StateFlag) flag;
                    region.get().setFlag(flag2, StateFlag.State.valueOf(state));
                    this.worldGuardAdapter.saveChanges(event.getWhoClicked().getWorld());
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().sendMessage(this.messageAdapter.getFlagSet().replace("{flag}", flagName).replace("{state}", state));
                }
            }
        }
    }
}
