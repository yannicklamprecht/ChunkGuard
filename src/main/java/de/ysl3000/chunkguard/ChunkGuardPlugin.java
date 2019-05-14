package de.ysl3000.chunkguard;

import de.ysl3000.chunkguard.adapter.BuyingAdapter;
import de.ysl3000.chunkguard.adapter.MoneyAdapter;
import de.ysl3000.chunkguard.adapter.WorldGuard7Adapter;
import de.ysl3000.chunkguard.commands.AdminBuy;
import de.ysl3000.chunkguard.commands.ChunkInfo;
import de.ysl3000.chunkguard.commands.ItemCreateCommand;
import de.ysl3000.chunkguard.commands.Manage;
import de.ysl3000.chunkguard.commands.UserBuy;
import de.ysl3000.chunkguard.config.Config;
import de.ysl3000.chunkguard.config.LanguageConfig;
import de.ysl3000.chunkguard.inventory.FlagInventory;
import de.ysl3000.chunkguard.inventory.MemberInventory;
import de.ysl3000.chunkguard.inventory.SellMenuInventory;
import de.ysl3000.chunkguard.lib.interfaces.I7WorldGuardAdapter;
import de.ysl3000.chunkguard.lib.interfaces.IBuyingAdapter;
import de.ysl3000.chunkguard.lib.interfaces.IMessageAdapter;
import de.ysl3000.chunkguard.lib.interfaces.IMoneyAdapter;
import de.ysl3000.chunkguard.lib.inventory.ItemStackmodifier;
import de.ysl3000.chunkguard.lib.inventory.MenuInventoryConfig;
import de.ysl3000.chunkguard.listener.ChunkListener;
import de.ysl3000.chunkguard.listener.UserSellListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ChunkGuardPlugin extends JavaPlugin implements Listener {

    private Config config;
    private IMoneyAdapter money;
    private I7WorldGuardAdapter worldGuardAdapter;
    private IMessageAdapter messageAdapter;
    private IBuyingAdapter transaction;
    private ItemStackmodifier itemStackmodifier;
    private AdminBuy adminBuy;
    private MenuInventoryConfig menuConfig;

    public void onEnable() {
        this.config = new Config();
        this.menuConfig = new MenuInventoryConfig();
        this.itemStackmodifier = new ItemStackmodifier(this);
        this.messageAdapter = new LanguageConfig();
        this.worldGuardAdapter = new WorldGuard7Adapter();
        this.money = new MoneyAdapter(this, this.config);
        this.transaction = new BuyingAdapter(this);
        new ChunkListener(this);
        this.adminBuy = new AdminBuy(this);
        new UserBuy(this);
        new ItemCreateCommand(this);
        new UserSellListener(this);
        new ChunkInfo(this);
        new Manage(this);
        new MemberInventory(this);
        new FlagInventory(this);
        new SellMenuInventory(this);
    }

    public void onDisable() {
        this.adminBuy.onDisable();
    }

    public Config getConfiguration() {
        return this.config;
    }

    public MenuInventoryConfig getMenuConfig() {
        return this.menuConfig;
    }

    public IMoneyAdapter getMoneyAdapter() {
        return this.money;
    }

    public I7WorldGuardAdapter getWorldGuardAdapter() {
        return this.worldGuardAdapter;
    }

    public IMessageAdapter getMessageAdapter() {
        return this.messageAdapter;
    }

    public IBuyingAdapter getBuyingAdapter() {
        return this.transaction;
    }

    public ItemStackmodifier getItemStackmodifier() {
        return this.itemStackmodifier;
    }
}
