package de.ysl3000.chunkguard.commands;

import de.ysl3000.chunkguard.*;
import java.util.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.*;
import org.bukkit.inventory.meta.*;

public class ItemCreateCommand implements CommandExecutor
{
    private ChunkGuardPlugin plugin;
    private List<String> buyitemType;
    
    public ItemCreateCommand(final ChunkGuardPlugin plugin) {
        this.buyitemType = new ArrayList<String>();
        this.plugin = plugin;
        plugin.getCommand("cgitem").setExecutor((CommandExecutor)this);
        this.buyitemType.add(plugin.getConfiguration().getMassBuyItem().name());
        this.buyitemType.add(String.valueOf(plugin.getConfiguration().getBuyPriceFromServer()));
    }
    
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings) {
        if (commandSender instanceof Player) {
            final Player p = (Player)commandSender;
            final ItemStack its = new ItemStack(this.plugin.getConfiguration().getMassBuyItem());
            if (!its.hasItemMeta()) {
                its.setItemMeta(Bukkit.getItemFactory().getItemMeta(this.plugin.getConfiguration().getMassBuyItem()));
            }
            final ItemMeta im = its.getItemMeta();
            im.setLore((List)this.buyitemType);
            its.setItemMeta(im);
            p.getInventory().addItem(new ItemStack[] { its });
            return true;
        }
        return false;
    }
}
