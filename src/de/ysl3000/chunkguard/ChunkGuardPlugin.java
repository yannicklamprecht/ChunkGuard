package de.ysl3000.chunkguard;

import org.bukkit.plugin.java.*;
import org.bukkit.event.*;
import de.ysl3000.chunkguard.lib.interfaces.*;
import de.ysl3000.chunkguard.lib.nbt.*;
import de.ysl3000.chunkguard.lib.inventory.*;
import de.ysl3000.chunkguard.config.*;
import com.sk89q.worldguard.bukkit.*;
import de.ysl3000.chunkguard.adapter.*;
import de.ysl3000.chunkguard.customflags.*;
import de.ysl3000.chunkguard.listener.*;
import de.ysl3000.chunkguard.commands.*;
import de.ysl3000.chunkguard.inventory.*;

public class ChunkGuardPlugin extends JavaPlugin implements Listener
{
    private WorldGuardPlugin worldGuardPlugin;
    private Config config;
    private IMoneyAdapter money;
    private IChunkGuardAdapter worldGuardAdapter;
    private IMessageAdapter messageAdapter;
    private IBuyingAdapter transaction;
    private NBTTagAPI nbtTagAPI;
    private ItemStackmodifier itemStackmodifier;
    private AdminBuy adminBuy;
    private MenuInventoryConfig menuConfig;
    
    public void onEnable() {
        this.config = new Config();
        this.menuConfig = new MenuInventoryConfig();
        this.nbtTagAPI = NBTTagAPI.inst();
        this.itemStackmodifier = new ItemStackmodifier(this);
        this.messageAdapter = new LanguageConfig();
        this.worldGuardPlugin = WGBukkit.getPlugin();
        this.worldGuardAdapter = new ChunkGuardAdapter(this.getWG());
        this.money = new MoneyAdapter(this,this.config);
        this.transaction = new BuyingAdapter(this);
        new FlagRegistry(this.worldGuardPlugin).registerFlags();
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
    
    public WorldGuardPlugin getWG() {
        return this.worldGuardPlugin;
    }
    
    public IChunkGuardAdapter getWorldGuardAdapter() {
        return this.worldGuardAdapter;
    }
    
    public IMessageAdapter getMessageAdapter() {
        return this.messageAdapter;
    }
    
    public IBuyingAdapter getBuyingAdapter() {
        return this.transaction;
    }
    
    public NBTTagAPI getNbtTagAPI() {
        return this.nbtTagAPI;
    }
    
    public ItemStackmodifier getItemStackmodifier() {
        return this.itemStackmodifier;
    }
}
