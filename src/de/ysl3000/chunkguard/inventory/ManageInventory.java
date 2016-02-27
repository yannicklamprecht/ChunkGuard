package de.ysl3000.chunkguard.inventory;

import de.ysl3000.chunkguard.*;
import de.ysl3000.chunkguard.inventory.items.*;
import de.ysl3000.chunkguard.lib.inventory.*;

public class ManageInventory extends ClickableInventory
{
    public ManageInventory(final ChunkGuardPlugin plugin) {
        super(plugin, "ChunkGuard-Manager");
        this.setClickableItem(0, new FlagItem());
    }
}
