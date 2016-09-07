package de.ysl3000.chunkguard.customflags;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class FlagRegistry
{
    private WorldGuardPlugin worldGuardPlugin;

    public FlagRegistry(final WorldGuardPlugin worldGuardPlugin) {
        this.worldGuardPlugin = worldGuardPlugin;
    }
    
    public void registerFlags() {
        worldGuardPlugin.getFlagRegistry().register(OverFlowFlag.WATER_OVERFLOW);
        worldGuardPlugin.getFlagRegistry().register(OverFlowFlag.LAVA_OVERFLOW);
    }
}
