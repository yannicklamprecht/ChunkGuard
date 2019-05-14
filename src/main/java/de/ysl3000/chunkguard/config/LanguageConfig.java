package de.ysl3000.chunkguard.config;

import de.ysl3000.chunkguard.lib.config.*;
import de.ysl3000.chunkguard.lib.interfaces.*;
import org.bukkit.*;

public class LanguageConfig extends YamlConfigLoader implements IMessageAdapter
{
    public LanguageConfig() {
        super("ChunkGuard", "message.yml");
        this.addDefault("chunk-created", "&aDer Chunk wurde erstellt");
        this.addDefault("no-chunk-available", "&4Keine Region verf\u00fcgbar");
        this.addDefault("chunk-available-message", "Dieser Chunk ist noch verf\u00fcgbar");
        this.addDefault("chunk-owner-info", "Der Besitzer {owner} dieses Chunks ist seit dem {lastonline} offline");
        this.addDefault("chunk-owner-info-online", "Der Besitzer {owner} dieses Chunks ist online");
        this.addDefault("chunk-already-exist", "Dieser Chunk ist bereits belegt");
        this.addDefault("easy-buy.need-item-message", "&rDu musst ein St\u00fcck {itemtype} in der Hand halten um diesen Modus zu nutzen!");
        this.addDefault("not-enough-money", "&4Du hast nicht gen\u00fcgend Geld");
        this.addDefault("couldn-buy-own-chunk", "&4Du kannst deinen eigenen Chunk nicht kaufen");
        this.addDefault("bought-chunk", "&aDu besitzt diesen Chunk nun");
        this.addDefault("easy-buy.add", "&4EasyBuy-Mode aktiviert");
        this.addDefault("easy-buy.remove", "&4EasyBuy-Mode deaktiviert");
        this.addDefault("must-be-a-number", "&4Die Eingabe {input} muss eine Zahl sein");
        this.addDefault("buying-transaction-failed", "&4Der Kauf ist fehlgeschlagen");
        this.addDefault("easy-buy.mod-message", "&rGeschwindigkeit wurde auf {speed} heruntergesetzt um den Server zu schonen");
        this.addDefault("not-the-owner", "&4Du bist nicht der Besitzer.");
        this.addDefault("flag-set", "&aDie Flag {flag} wurde erfolgreich auf {state} gesetzt.");
        this.addDefault("manage.member.removed-canceled", "&4Du has den Vorgang abgebrochen");
        this.addDefault("manage.member.removed-successfully", "&aDu has den Spieler erfolgreich entfernt");
        this.addDefault("manage.member.added-successfully", "&aDu has den Spieler erfolgreich hinzugef\u00fcgt");
        this.addDefault("key.accept", "Akzeptieren");
        this.addDefault("key.decline", "Abbrechen");
        this.addDefault("sold-chunk", "&aDu has den Chunk erfolgreich an die Bank verkauft");
    }
    
    @Override
    public String youNowOwnThisChunk() {
        return this.translate(this.config.getString("bought-chunk"));
    }
    
    @Override
    public String youCouldNotBuyYourOwnChunk() {
        return this.translate(this.config.getString("couldn-buy-own-chunk"));
    }
    
    @Override
    public String youAreNowRemovedFromEasyBuy() {
        return this.translate(this.config.getString("easy-buy.remove"));
    }
    
    @Override
    public String youAreNowAddedToEasyBuy() {
        return this.translate(this.config.getString("easy-buy.add"));
    }
    
    @Override
    public String youNeedAnItemOfCoalInYourHandToUseThisMode() {
        return this.translate(this.config.getString("easy-buy.need-item-message"));
    }
    
    @Override
    public String chunkAlreadyExist() {
        return this.translate(this.config.getString("chunk-already-exist"));
    }
    
    @Override
    public String chunkIsNowCreated() {
        return this.translate(this.config.getString("chunk-created"));
    }
    
    @Override
    public String chunkAvailableMessage() {
        return this.translate(this.config.getString("chunk-available-message"));
    }
    
    @Override
    public String getChunkOwnerInfo() {
        return this.translate(this.config.getString("chunk-owner-info"));
    }
    
    @Override
    public String getChunkOwnerInfoOnline() {
        return this.translate(this.config.getString("chunk-owner-info-online"));
    }
    
    @Override
    public String notEnoughMoney() {
        return this.translate(this.config.getString("not-enough-money"));
    }
    
    @Override
    public String musBeANumber() {
        return this.translate(this.config.getString("must-be-a-number"));
    }
    
    @Override
    public String noChunkAvailable() {
        return this.translate(this.config.getString("no-chunk-available"));
    }
    
    @Override
    public String buyingTransactionFailed() {
        return this.translate(this.config.getString("buying-transaction-failed"));
    }
    
    @Override
    public String speedModMessage() {
        return this.translate(this.config.getString("easy-buy.mod-message"));
    }
    
    @Override
    public String notYourChunk() {
        return this.translate(this.config.getString("not-the-owner"));
    }
    
    @Override
    public String getFlagSet() {
        return this.translate(this.config.getString("flag-set"));
    }
    
    @Override
    public String getMemberRemovedSuccessfully() {
        return this.translate(this.config.getString("manage.member.removed-successfully"));
    }
    
    @Override
    public String getMemberAddedSuccessfully() {
        return this.translate(this.config.getString("manage.member.added-successfully"));
    }
    
    @Override
    public String getCanceled() {
        return this.translate(this.config.getString("manage.member.removed-canceled"));
    }
    
    @Override
    public String getChunkSoldSuccessfully() {
        return this.translate(this.config.getString("sold-chunk"));
    }
    
    @Override
    public String getKeyAccept() {
        return this.translate(this.config.getString("key.accept"));
    }
    
    @Override
    public String getKeyDencline() {
        return this.translate(this.config.getString("key.decline"));
    }
    
    private String translate(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    public class Path
    {
        public static final String CHUNK_IS_NOW_CREATED = "chunk-created";
        public static final String YOU_NOW_OWN_CHUNK = "bought-chunk";
        public static final String YOU_NOW_SOLD_CHUNK = "sold-chunk";
        public static final String COULDNT_BUY_OWN_CHUNK = "couldn-buy-own-chunk";
        public static final String CHUNK_ALREADY_EXIST = "chunk-already-exist";
        public static final String EASY_BUY_REMOVE = "easy-buy.remove";
        public static final String EASY_BUY_ADD = "easy-buy.add";
        public static final String NEED_COAL_EASY_BUY = "easy-buy.need-item-message";
        public static final String CHUNK_AVAILABLE_MESSAGE = "chunk-available-message";
        public static final String CHUNK_OWNER_INFO = "chunk-owner-info";
        public static final String CHUNK_OWNER_INFO_ONLINE = "chunk-owner-info-online";
        public static final String NOT_ENOUGH_MONEY = "not-enough-money";
        public static final String MUST_BE_A_NUMBER = "must-be-a-number";
        public static final String NO_CHUNK_AVAILABLE = "no-chunk-available";
        public static final String BUYING_TRANSACTION_FAILED = "buying-transaction-failed";
        public static final String EASY_BUY_MOD_SPEED = "easy-buy.mod-message";
        public static final String NOT_THE_OWNER = "not-the-owner";
        public static final String FLAG_SET = "flag-set";
        public static final String MEMBER_REMOVED_SUCCESSFULLY = "manage.member.removed-successfully";
        public static final String MEMBER_ADDED_SUCCESSFULLY = "manage.member.added-successfully";
        public static final String MEMBER_REMOVED_CANCELED = "manage.member.removed-canceled";
        public static final String KEY_ACCEPT = "key.accept";
        public static final String KEY_DECLINE = "key.decline";
    }
}
