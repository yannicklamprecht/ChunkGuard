package de.ysl3000.chunkguard.lib.inventory;

import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.bukkit.protection.DelayedRegionOverlapAssociation;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.ysl3000.chunkguard.ChunkGuardPlugin;
import de.ysl3000.chunkguard.commands.Manage;
import de.ysl3000.chunkguard.lib.nbt.INBTModifier;
import de.ysl3000.chunkguard.lib.nbt.NBTTagAPI;
import org.bukkit.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Colorable;

import java.util.*;

public class ItemStackmodifier
{
    private ChunkGuardPlugin plugin;
    private NBTTagAPI nbtTagAPI;
    
    public ItemStackmodifier(final ChunkGuardPlugin plugin) {
        this.plugin = plugin;
        this.nbtTagAPI = plugin.getNbtTagAPI();
    }
    
    public ItemStack create(final String recognitionKey, final ItemState state, final String name, final List<String> lore) {
        ItemStack itemStack = null;
        if (state.equals(ItemState.DISABLED)) {
            itemStack = this.setColorToClay(DyeColor.RED, name, lore);
        }
        else if (state.equals(ItemState.ENABLED)) {
            itemStack = this.setColorToClay(DyeColor.LIME, name, lore);
        }
        else if (state.equals(ItemState.NO_RIGHT)) {
            itemStack = this.setColorToClay(DyeColor.BLACK, name, lore);
        }
        final INBTModifier modifier = this.nbtTagAPI.getNBTFromItemStack(itemStack);
        modifier.setString(recognitionKey, state.getState());
        return this.nbtTagAPI.setNBT(itemStack, modifier);
    }
    
    public Inventory createAcceptDecline(final String name) {
        final Inventory inv = Bukkit.createInventory(null, 9, name);
        inv.setItem(0, this.setColorToClay(DyeColor.LIME, "Akzeptieren", Arrays.asList("Dies ist die Best\u00e4tigung")));
        inv.setItem(1, this.setColorToClay(DyeColor.RED, "Abbrechen", Arrays.asList("Wenn du dir nicht sicher bist, klicke hier.")));
        return inv;
    }
    
    public Inventory createStateInventory(final MenuInventoryConfig config, final String flag) {
        final Inventory inv = Bukkit.createInventory(null, 9, config.getFlagInvName());
        inv.setItem(0, this.setColorToClay(DyeColor.LIME, StateFlag.State.ALLOW.name(), Arrays.asList(flag)));
        inv.setItem(1, this.setColorToClay(DyeColor.RED, StateFlag.State.DENY.name(), Arrays.asList(flag)));
        return inv;
    }
    
    public Inventory createSkull(final Set<OfflinePlayer> members, final String inventoryName) {
        int size = members.size() / 9;
        if (members.size() % 9 != 0) {
            ++size;
        }
        size *= 9;
        if (size == 0) {
            size = 9;
        }
        final Inventory memberInventory = Bukkit.createInventory(null, size, inventoryName);
        members.forEach(m -> {
             ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());
             SkullMeta  skull = (SkullMeta)itemStack.getItemMeta();
            skull.setOwner(m.getName());
            itemStack.setItemMeta(skull);
            memberInventory.addItem(new ItemStack[] { itemStack });
            return;
        });
        return memberInventory;
    }
    
    public ItemStack setColorToClay(final DyeColor color, final String name, final List<String> info) {
        final ItemStack itemStack = new ItemStack(Material.STAINED_CLAY, 1);
        if(itemStack instanceof Colorable){
            ((Colorable)itemStack).setColor(color);
        }
        final ItemMeta im = itemStack.getItemMeta();
        if (name != null) {
            im.setDisplayName(name);
        }
        if (info != null) {
            im.setLore((List)info);
        }
        itemStack.setItemMeta(im);
        return itemStack;
    }
    
    public ItemState getState(final ItemStack itemStack, final Manage.ChunkGuardMainItems key) {
        final String state = this.nbtTagAPI.getNBTFromItemStack(itemStack).getString(key.getKey());
        if (state.equals(ItemState.ENABLED.getState())) {
            return ItemState.ENABLED;
        }
        if (state.equals(ItemState.DISABLED.getState())) {
            return ItemState.DISABLED;
        }
        return ItemState.NO_RIGHT;
    }
    
    public boolean hasKey(final ItemStack itemStack, final Manage.ChunkGuardMainItems key) {
        return this.nbtTagAPI.getNBTFromItemStack(itemStack).hasKey(key.getKey());
    }
    
    public Inventory create(final WorldGuardPlugin worldGuardPlugin, final Location location, final ProtectedRegion region, final String inventoryName) {
        final HashMap<String, ItemState> boolFlags = new HashMap<>();
        final Map<Flag<?>, Object> map = region.getFlags();
        final ApplicableRegionSet regions = worldGuardPlugin.getRegionManager(location.getWorld()).getApplicableRegions(location);
        final RegionQuery query = worldGuardPlugin.getRegionContainer().createQuery();
        final RegionAssociable ra = new DelayedRegionOverlapAssociation(query, location);
        map.forEach((f, o) -> {
            if (f instanceof StateFlag) {
                if (regions.testState(ra, new StateFlag[] { (StateFlag) f })) {
                    boolFlags.put(((Flag)f).getName(), ItemState.ENABLED);
                }
                else {
                    boolFlags.put(((Flag)f).getName(), ItemState.DISABLED);
                }
            }
            return;
        });
        int size = boolFlags.keySet().size() / 9;
        if (boolFlags.keySet().size() % 9 != 0) {
            ++size;
        }
        size *= 9;
        final Inventory flagInventory = Bukkit.createInventory(null, size, inventoryName);
        boolFlags.forEach((s, b) -> flagInventory.addItem(new ItemStack[] { this.create(s, b, s, Arrays.asList(new String[0])) }));
        return flagInventory;
    }
    
    public enum ItemState
    {
        DISABLED("disabled"), 
        ENABLED("enabled"), 
        NO_RIGHT("no-right");
        
        private String state;
        
        ItemState(final String state) {
            this.state = state;
        }
        
        public String getState() {
            return this.state;
        }
    }
}
