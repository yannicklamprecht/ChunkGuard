package de.ysl3000.chunkguard.listener;

import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.ysl3000.chunkguard.ChunkGuardPlugin;
import de.ysl3000.chunkguard.customflags.OverFlowFlag;
import de.ysl3000.chunkguard.lib.interfaces.I7WorldGuardAdapter;
import java.util.Arrays;
import java.util.Optional;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class ChunkListener implements Listener {

    private I7WorldGuardAdapter worldGuardAdapter;

    public ChunkListener(final ChunkGuardPlugin chunkGuardPlugin) {
        chunkGuardPlugin.getServer().getPluginManager().registerEvents(this, chunkGuardPlugin);
        this.worldGuardAdapter = chunkGuardPlugin.getWorldGuardAdapter();
    }

    @EventHandler
    public void waterflow(final BlockFromToEvent e) {
        if (e.getBlock().getChunk().equals(e.getToBlock().getChunk())) {
            return;
        }
        final Optional<ProtectedRegion> rg = this.worldGuardAdapter.getRegion(e.getToBlock().getLocation());
        if (rg.isPresent()) {
            if (this.isMaterial(e.getBlock().getType(), Material.WATER)) {
                if (rg.get().getFlag(OverFlowFlag.WATER_OVERFLOW) != null && rg.get().getFlag(OverFlowFlag.WATER_OVERFLOW).compareTo(StateFlag.State.DENY) == 0) {
                    e.setCancelled(true);
                }
            } else if (this.isMaterial(e.getBlock().getType(), Material.LAVA) && rg.get().getFlag(OverFlowFlag.LAVA_OVERFLOW) != null && rg.get().getFlag(OverFlowFlag.LAVA_OVERFLOW).compareTo(StateFlag.State.DENY) == 0) {
                e.setCancelled(true);
            }
        }
    }

    private boolean isMaterial(final Material key, final Material... values) {
        return Arrays.asList(values).contains(key);
    }
}
