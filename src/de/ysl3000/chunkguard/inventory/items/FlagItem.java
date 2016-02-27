package de.ysl3000.chunkguard.inventory.items;

import de.ysl3000.chunkguard.lib.inventory.*;
import de.ysl3000.chunkguard.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;

public class FlagItem extends ClickableItem
{
    private ClickableInventory<ChunkGuardPlugin> flagInventory;
    
    public FlagItem() {
        super(new ItemStack(Material.STAINED_CLAY));
    }
    
    @Override
    public void run(final Player player) {
        player.openInventory(this.flagInventory.getInventory());
    }
}
