package de.ysl3000.chunkguard.lib.inventory;

import org.bukkit.inventory.*;
import org.bukkit.entity.*;

public abstract class ClickableItem
{
    private ItemStack itemStack;
    
    public ClickableItem(final ItemStack clickedItem) {
        this.itemStack = clickedItem;
    }
    
    public abstract void run(final Player p0);
    
    public ItemStack getItemStack() {
        return this.itemStack;
    }
}
