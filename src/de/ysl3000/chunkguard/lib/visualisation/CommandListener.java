package de.ysl3000.chunkguard.lib.visualisation;

import org.bukkit.plugin.java.*;
import org.bukkit.event.player.*;
import java.util.*;
import org.bukkit.event.*;

public class CommandListener implements Listener
{
    private ChunkLookup ls;
    
    public CommandListener(final JavaPlugin plugin) {
        this.ls = new ChunkLookupListener(plugin);
    }
    
    @EventHandler
    public void onCommand(final AsyncPlayerChatEvent e) {
        this.ls.playAtChunk(Optional.of(e.getPlayer()), e.getPlayer().getLocation().getChunk());
    }
}
