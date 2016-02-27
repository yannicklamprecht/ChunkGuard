package de.ysl3000.chunkguard.lib.inventory;

import de.ysl3000.chunkguard.lib.config.*;
import java.util.*;

public class MenuInventoryConfig extends YamlConfigLoader
{
    public MenuInventoryConfig() {
        super("ChunkGuard", "Menu.yml");
        this.addDefault("mainmenu.members.add.info", new String[] { "Es werden Spieler im Umkreis von 5 Bl\u00f6cken gelistet", "Auf die Kopf klicken um ihn hinzuzuf\u00fcgen" });
        this.addDefault("mainmenu.members.add.name", "Spieler hinzuf\u00fcgen");
        this.addDefault("mainmenu.members.remove.info", new String[] { "Werden aufgelistet", "K\u00f6nnen mit 2x Klicks entfernt werden" });
        this.addDefault("mainmenu.members.remove.name", "Aktuelle Mitglieder");
        this.addDefault("mainmenu.members.remove.accept", "Best\u00e4tigung");
        this.addDefault("mainmenu.flags.manage.info", "Grundst\u00fcckeinstellungen");
        this.addDefault("mainmenu.flags.manage.description", new String[] { "Flags des Grundst\u00fccks \u00e4ndern" });
        this.addDefault("mainmenu.flags.manage.disable", "Deaktivieren");
        this.addDefault("mainmenu.flags.manage.enable", "Aktivieren");
        this.addDefault("mainmenu.flags.manage.name", "Flag");
        this.addDefault("mainmenu.sell.name", "Chunk an Server Verkaufen");
        this.addDefault("mainmenu.sell.description", new String[] { "Hier kannst du den Chunk zum vorgegebenen Preis verkaufen" });
        this.addDefault("mainmenu.buy.name", "Chunk von Server kaufen");
        this.addDefault("mainmenu.sell.description", new String[] { "Hier kannst du den Chunk zum vorgegebenen Preis kaufen" });
    }
    
    public List<String> getMemberRemoveDescription() {
        return (List<String>)this.config.getStringList("mainmenu.members.remove.info");
    }
    
    public String getMemberRemoveName() {
        return this.config.getString("mainmenu.members.remove.name");
    }
    
    public List<String> getMemberAddDescription() {
        return (List<String>)this.config.getStringList("mainmenu.members.add.info");
    }
    
    public String getMemberAddName() {
        return this.config.getString("mainmenu.members.add.name");
    }
    
    public String getAccept() {
        return this.config.getString("mainmenu.members.remove.accept");
    }
    
    public String getFlagsInfo() {
        return this.config.getString("mainmenu.flags.manage.info");
    }
    
    public String getFlagsEnable() {
        return this.config.getString("mainmenu.flags.manage.enable");
    }
    
    public String getFlagsDisable() {
        return this.config.getString("mainmenu.flags.manage.disable");
    }
    
    public List<String> getFlagsDescription() {
        return (List<String>)this.config.getStringList("mainmenu.flags.manage.description");
    }
    
    public String getFlagInvName() {
        return this.config.getString("mainmenu.flags.manage.name");
    }
    
    public String SellMenuName() {
        return this.config.getString("mainmenu.sell.name");
    }
    
    public List<String> getSellMenuDescription() {
        return (List<String>)this.config.getStringList("mainmenu.sell.description");
    }
    
    public String BuyMenuName() {
        return this.config.getString("mainmenu.buy.name");
    }
    
    public List<String> getBuyMenuDescription() {
        return (List<String>)this.config.getStringList("mainmenu.buy.description");
    }
    
    private class Path
    {
        public static final String MEMBERS_ADD_NAME = "mainmenu.members.add.name";
        public static final String MEMBERS_ADD_INFO = "mainmenu.members.add.info";
        public static final String MEMBERS_REMOVE_NAME = "mainmenu.members.remove.name";
        public static final String MEMBERS_REMOVE_INFO = "mainmenu.members.remove.info";
        public static final String MEMBERS_REMOVE_ACCEPT = "mainmenu.members.remove.accept";
        public static final String FLAGS_MANAGE = "mainmenu.flags.manage.info";
        public static final String FLAGS_MANAGE_DISABLE = "mainmenu.flags.manage.disable";
        public static final String FLAGS_MANAGE_ENABLE = "mainmenu.flags.manage.enable";
        public static final String FLAGS_MANAGE_DESCRIPTION = "mainmenu.flags.manage.description";
        public static final String FLAGS_MANAGE_NAME = "mainmenu.flags.manage.name";
        public static final String SELL_NAME = "mainmenu.sell.name";
        public static final String SELL_DESCRIPTION = "mainmenu.sell.description";
        public static final String BUY_NAME = "mainmenu.buy.name";
        public static final String BUY_DESCRIPTION = "mainmenu.buy.description";
    }
}
