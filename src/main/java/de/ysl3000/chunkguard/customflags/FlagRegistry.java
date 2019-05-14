package de.ysl3000.chunkguard.customflags;

import com.sk89q.worldguard.WorldGuard;

public class FlagRegistry {

    private WorldGuard worldGuardPlugin;

    public FlagRegistry(final WorldGuard worldGuardPlugin) {
        this.worldGuardPlugin = worldGuardPlugin;
    }

    public void registerFlags() {
        worldGuardPlugin.getFlagRegistry().register(OverFlowFlag.WATER_OVERFLOW);
        worldGuardPlugin.getFlagRegistry().register(OverFlowFlag.LAVA_OVERFLOW);
    }
}
