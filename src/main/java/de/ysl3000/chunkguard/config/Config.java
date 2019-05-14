package de.ysl3000.chunkguard.config;

import de.ysl3000.chunkguard.lib.config.*;
import org.bukkit.*;

public class Config extends YamlConfigLoader
{
    public Config() {
        super("ChunkGuard", "Config.yml");
        this.addDefault("massBuy-Item", Material.COAL.name());
        this.addDefault("price.buy_from_server", 20.0);
        this.addDefault("price.sell_to_server", 20.0);
        this.addDefault("bankAccountName-money", 2000.0);
        this.addDefault("user-license-material", Material.NAME_TAG.name());
    }
    
    public Material getMassBuyItem() {
        return Material.valueOf(this.config.getString("massBuy-Item"));
    }
    
    public Material getChunkBuyLicense() {
        return Material.valueOf(this.config.getString("user-license-material"));
    }
    
    public double getBuyPriceFromServer() {
        return this.config.getDouble("price.buy_from_server");
    }
    
    public double getSellPriceToServer() {
        return this.config.getDouble("price.sell_to_server");
    }
    
    public double getBankAccountMoney() {
        return this.config.getDouble("bankAccountName-money");
    }
    
    public void setBankAccountMoney(final double money) {
        this.config.set("bankAccountName-money", (Object)money);
    }
    
    public static class Path
    {
        public static final String MASS_BUY_ITEM = "massBuy-Item";
        public static final String LICENSE = "user-license-material";
        public static final String PRICE_BUY_FROM_SERVER = "price.buy_from_server";
        public static final String PRICE_SELL_TO_SERVER = "price.sell_to_server";
        public static final String BANK_ACOUNT_MONEY = "bankAccountName-money";
    }
}
