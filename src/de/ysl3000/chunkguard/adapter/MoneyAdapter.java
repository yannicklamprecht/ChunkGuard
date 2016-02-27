package de.ysl3000.chunkguard.adapter;

import de.ysl3000.chunkguard.lib.interfaces.*;
import net.milkbowl.vault.economy.*;
import de.ysl3000.chunkguard.config.*;
import org.bukkit.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;

public class MoneyAdapter implements IMoneyAdapter {

    private JavaPlugin javaPlugin;
    private Economy economy;
    private OfflinePlayer bankAccountName;
    private Config config;

    public MoneyAdapter(JavaPlugin javaPlugin, final Config config) {
        this.javaPlugin=javaPlugin;
        this.config = config;
        if(!setupEconomy()){
            this.javaPlugin.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", javaPlugin.getDescription().getName()));
            Bukkit.getServer().getPluginManager().disablePlugin(this.javaPlugin);
        }
    }

    @Override
    public boolean pay(final OfflinePlayer sender, final OfflinePlayer receiver, double money) {
        money = Math.abs(money);
        return this.economy != null && this.economy.has(sender, money) && this.economy.withdrawPlayer(sender, money).transactionSuccess() && this.economy.depositPlayer(receiver, money).transactionSuccess();
    }

    @Override
    public boolean sellToBank(final OfflinePlayer player, double money) {
        money = Math.abs(money);
        if (this.economy == null) {
            return false;
        }
        if (!this.economy.has(player, money)) {
            return false;
        }
        if (this.config.getBankAccountMoney() - money > 0.0 && this.economy.depositPlayer(player, money).transactionSuccess()) {
            this.config.setBankAccountMoney(this.config.getBankAccountMoney() - money);
            return this.config.saveConfig();
        }
        return false;
    }

    @Override
    public boolean buyFromBank(final OfflinePlayer player, double money) {
        money = Math.abs(money);
        if (this.economy == null) {
            return false;
        }
        if (!this.economy.has(player, money)) {
            return false;
        }
        if (this.economy.withdrawPlayer(player, money).transactionSuccess()) {
            this.config.setBankAccountMoney(this.config.getBankAccountMoney() + money);
            return this.config.saveConfig();
        }
        return false;
    }

    private boolean setupEconomy() {

        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        final RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            this.economy = economyProvider.getProvider();
        }
        return this.economy != null;
    }
}
