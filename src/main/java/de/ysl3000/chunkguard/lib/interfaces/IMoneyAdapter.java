package de.ysl3000.chunkguard.lib.interfaces;

import org.bukkit.*;

public interface IMoneyAdapter
{
    boolean pay(final OfflinePlayer p0, final OfflinePlayer p1, final double p2);
    
    boolean sellToBank(final OfflinePlayer p0, final double p1);
    
    boolean buyFromBank(final OfflinePlayer p0, final double p1);
}
