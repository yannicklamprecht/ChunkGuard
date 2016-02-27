package de.ysl3000.chunkguard.commands;

import de.ysl3000.chunkguard.lib.interfaces.*;
import de.ysl3000.chunkguard.*;
import java.text.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import de.ysl3000.chunkguard.lib.*;
import java.util.*;

public class ChunkInfo implements CommandExecutor
{
    private IWorldGuardAdapter worldGuardAdapter;
    private ChunkGuardPlugin chunkGuardPlugin;
    private SimpleDateFormat simpleDateFormat;
    
    public ChunkInfo(final ChunkGuardPlugin chunkGuardPlugin) {
        this.simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
        this.chunkGuardPlugin = chunkGuardPlugin;
        this.worldGuardAdapter = chunkGuardPlugin.getWorldGuardAdapter();
        chunkGuardPlugin.getCommand("cgInfo").setExecutor((CommandExecutor)this);
    }
    
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings) {
        if (commandSender instanceof Player) {
            final Player p = (Player)commandSender;
            if (this.worldGuardAdapter.getRegion(p.getLocation()).isPresent()) {
                final Optional<OfflinePlayer> owner = this.worldGuardAdapter.getOwner(p.getLocation());
                if (owner.isPresent()) {
                    if (owner.get().isOnline()) {
                        p.sendMessage(this.chunkGuardPlugin.getMessageAdapter().getChunkOwnerInfoOnline().replace("{owner}", owner.get().getName()));
                    }
                    else {
                        p.sendMessage(this.chunkGuardPlugin.getMessageAdapter().getChunkOwnerInfo().replace("{owner}", owner.get().getName()).replace("{lastonline}", this.simpleDateFormat.format(new Date(owner.get().getLastPlayed()))));
                    }
                }
                else {
                    p.sendMessage(this.chunkGuardPlugin.getMessageAdapter().chunkAvailableMessage());
                }
            }
            else {
                p.sendMessage(this.chunkGuardPlugin.getMessageAdapter().noChunkAvailable());
            }
            BukkitLib.displayRegion(this.chunkGuardPlugin, p, p.getLocation());
        }
        return true;
    }
}
