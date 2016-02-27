package de.ysl3000.chunkguard.customflags;

import com.sk89q.worldguard.bukkit.*;
import com.mewin.WGCustomFlags.*;
import org.bukkit.*;
import com.sk89q.worldguard.protection.flags.*;

public class FlagRegistry
{
    private WorldGuardPlugin worldGuardPlugin;
    private WGCustomFlagsPlugin wgCustomFlagsPlugin;
    
    public FlagRegistry(final WorldGuardPlugin worldGuardPlugin) {
        this.worldGuardPlugin = worldGuardPlugin;
        this.wgCustomFlagsPlugin = (WGCustomFlagsPlugin)Bukkit.getServer().getPluginManager().getPlugin("WGCustomFlags");
    }
    
    public void registerFlags() {
        this.wgCustomFlagsPlugin.addCustomFlag((Flag)OverFlowFlag.WATER_OVERFLOW);
        this.wgCustomFlagsPlugin.addCustomFlag((Flag)OverFlowFlag.LAVA_OVERFLOW);
    }
}
