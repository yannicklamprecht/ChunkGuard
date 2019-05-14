package de.ysl3000.chunkguard.lib.inventory;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.DelayedRegionOverlapAssociation;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import de.ysl3000.chunkguard.ChunkGuardPlugin;
import de.ysl3000.chunkguard.commands.Manage;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemStackmodifier {

    private ChunkGuardPlugin plugin;

    public ItemStackmodifier( ChunkGuardPlugin plugin) {
        this.plugin = plugin;
    }

    public ItemStack create( String recognitionKey,  ItemState state,  String name,  List<String> lore) {
        ItemStack itemStack = null;
        if (state.equals(ItemState.DISABLED)) {
            itemStack = this.setColorToClay(Material.RED_TERRACOTTA, name, lore);
        } else if (state.equals(ItemState.ENABLED)) {
            itemStack = this.setColorToClay(Material.LIME_TERRACOTTA, name, lore);
        } else if (state.equals(ItemState.NO_RIGHT)) {
            itemStack = this.setColorToClay(Material.BLACK_TERRACOTTA, name, lore);
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, recognitionKey), PersistentDataType.STRING, state.state);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public Inventory createAcceptDecline( String name) {
         Inventory inv = Bukkit.createInventory(null, 9, name);
        inv.setItem(0, this.setColorToClay(Material.LIME_TERRACOTTA, "Akzeptieren", Collections.singletonList("Dies ist die Best\u00e4tigung")));
        inv.setItem(1, this.setColorToClay(Material.RED_TERRACOTTA, "Abbrechen", Collections.singletonList("Wenn du dir nicht sicher bist, klicke hier.")));
        return inv;
    }

    public Inventory createStateInventory( MenuInventoryConfig config,  String flag) {
         Inventory inv = Bukkit.createInventory(null, 9, config.getFlagInvName());
        inv.setItem(0, this.setColorToClay(Material.LIME_TERRACOTTA, StateFlag.State.ALLOW.name(), Collections.singletonList(flag)));
        inv.setItem(1, this.setColorToClay(Material.RED_TERRACOTTA, StateFlag.State.DENY.name(), Collections.singletonList(flag)));
        return inv;
    }

    public Inventory createSkull( Set<OfflinePlayer> members,  String inventoryName) {
        int size = members.size() / 9;
        if (members.size() % 9 != 0) {
            ++size;
        }
        size *= 9;
        if (size == 0) {
            size = 9;
        }
         Inventory memberInventory = Bukkit.createInventory(null, size, inventoryName);
        members.forEach(m -> {
            ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta skull = (SkullMeta) itemStack.getItemMeta();
            skull.setOwningPlayer(m);
            itemStack.setItemMeta(skull);
            memberInventory.addItem(itemStack);
        });
        return memberInventory;
    }

    public ItemStack setColorToClay(Material coloredTerracotta, String name, List<String> info) {
         ItemStack itemStack = new ItemStack(coloredTerracotta, 1);
         ItemMeta im = itemStack.getItemMeta();
        if (name != null) {
            im.setDisplayName(name);
        }
        if (info != null) {
            im.setLore(info);
        }
        itemStack.setItemMeta(im);
        return itemStack;
    }

    public ItemState getState( ItemStack itemStack,  Manage.ChunkGuardMainItems key) {
         String state = itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, key.getKey()), PersistentDataType.STRING);
        if (state.equals(ItemState.ENABLED.getState())) {
            return ItemState.ENABLED;
        }
        if (state.equals(ItemState.DISABLED.getState())) {
            return ItemState.DISABLED;
        }
        return ItemState.NO_RIGHT;
    }

    public boolean hasKey( ItemStack itemStack,  Manage.ChunkGuardMainItems key) {
        return itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, key.getKey()), PersistentDataType.STRING);
    }

    public Inventory create(Location location,  ProtectedRegion region,  String inventoryName) {
         HashMap<String, ItemState> boolFlags = new HashMap<>();
         Map<Flag<?>, Object> map = region.getFlags();
         ApplicableRegionSet regions = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(location.getWorld())).getApplicableRegions(BukkitAdapter.asBlockVector(location));
         RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
         RegionAssociable ra = new DelayedRegionOverlapAssociation(query, BukkitAdapter.adapt(location));
        map.forEach((f, o) -> {
            if (f instanceof StateFlag) {
                if (regions.testState(ra, (StateFlag) f)) {
                    boolFlags.put(f.getName(), ItemState.ENABLED);
                } else {
                    boolFlags.put(f.getName(), ItemState.DISABLED);
                }
            }
        });
        int size = boolFlags.keySet().size() / 9;
        if (boolFlags.keySet().size() % 9 != 0) {
            ++size;
        }
        size *= 9;
         Inventory flagInventory = Bukkit.createInventory(null, size, inventoryName);
        boolFlags.forEach((s, b) -> flagInventory.addItem(this.create(s, b, s, Collections.emptyList())));
        return flagInventory;
    }

    public enum ItemState {
        DISABLED("disabled"),
        ENABLED("enabled"),
        NO_RIGHT("no-right");

        private String state;

        ItemState( String state) {
            this.state = state;
        }

        public String getState() {
            return this.state;
        }
    }
}
