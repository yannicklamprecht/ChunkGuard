package de.ysl3000.chunkguard.commands;

import de.ysl3000.chunkguard.ChunkGuardPlugin;
import de.ysl3000.chunkguard.lib.BukkitLib;
import de.ysl3000.chunkguard.lib.interfaces.I7WorldGuardAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChunkInfo implements CommandExecutor {

    private I7WorldGuardAdapter worldGuardAdapter;
    private ChunkGuardPlugin chunkGuardPlugin;
    private SimpleDateFormat simpleDateFormat;

    public ChunkInfo(final ChunkGuardPlugin chunkGuardPlugin) {
        this.simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
        this.chunkGuardPlugin = chunkGuardPlugin;
        this.worldGuardAdapter = chunkGuardPlugin.getWorldGuardAdapter();
        chunkGuardPlugin.getCommand("cgInfo").setExecutor(this);
    }

    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings) {
        if (commandSender instanceof Player) {
            final Player p = (Player) commandSender;
            if (this.worldGuardAdapter.getRegion(p.getLocation()).isPresent()) {
                final Optional<OfflinePlayer> owner = this.worldGuardAdapter.getOwner(p.getLocation());
                if (owner.isPresent()) {
                    if (owner.get().isOnline()) {
                        p.sendMessage(this.chunkGuardPlugin.getMessageAdapter().getChunkOwnerInfoOnline().replace("{owner}", owner.get().getName()));
                    } else {
                        p.sendMessage(this.chunkGuardPlugin.getMessageAdapter().getChunkOwnerInfo().replace("{owner}", owner.get().getName()).replace("{lastonline}", this.simpleDateFormat.format(new Date(owner.get().getLastPlayed()))));
                    }
                } else {
                    p.sendMessage(this.chunkGuardPlugin.getMessageAdapter().chunkAvailableMessage());
                }
            } else {
                p.sendMessage(this.chunkGuardPlugin.getMessageAdapter().noChunkAvailable());
            }
            BukkitLib.displayRegion(this.chunkGuardPlugin, p, p.getLocation());
        }
        return true;
    }
}
