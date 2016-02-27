package de.ysl3000.chunkguard.commands;

import de.ysl3000.chunkguard.*;
import de.ysl3000.chunkguard.config.*;
import de.ysl3000.chunkguard.lib.interfaces.*;
import org.bukkit.plugin.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.sk89q.worldguard.protection.regions.*;
import com.sk89q.worldguard.protection.flags.*;
import org.bukkit.event.*;
import de.ysl3000.chunkguard.lib.visualisation.locationmanagement.*;
import de.ysl3000.chunkguard.lib.visualisation.*;
import org.bukkit.plugin.java.*;
import java.util.*;
import org.bukkit.event.player.*;
import org.bukkit.*;

public class AdminBuy implements CommandExecutor, Listener
{
    private Map<UUID, SpeedContainer> enabledAdmin;
    private SpeedContainer maxSpeed;
    private ChunkGuardPlugin chunkGuardPlugin;
    private Config config;
    private IWorldGuardAdapter worldGuardAdapter;
    
    public AdminBuy(final ChunkGuardPlugin chunkGuardPlugin) {
        this.enabledAdmin = new HashMap<UUID, SpeedContainer>();
        this.maxSpeed = new SpeedContainer(0.04f, 0.4f);
        this.worldGuardAdapter = chunkGuardPlugin.getWorldGuardAdapter();
        this.chunkGuardPlugin = chunkGuardPlugin;
        this.config = chunkGuardPlugin.getConfiguration();
        chunkGuardPlugin.getCommand("adminbuy").setExecutor((CommandExecutor)this);
        chunkGuardPlugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)chunkGuardPlugin);
    }
    
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings) {
        if (!(commandSender instanceof Player)) {
            return true;
        }
        final Player player = (Player)commandSender;
        final UUID playerUUID = player.getUniqueId();
        if (this.enabledAdmin.containsKey(playerUUID)) {
            this.enabledAdmin.get(playerUUID).resetAllSpeed(player);
            this.enabledAdmin.remove(playerUUID);
            player.sendMessage(this.chunkGuardPlugin.getMessageAdapter().youAreNowRemovedFromEasyBuy());
        }
        else {
            this.enabledAdmin.put(playerUUID, new SpeedContainer(player));
            this.maxSpeed.resetSpeed(player);
            player.sendMessage(this.chunkGuardPlugin.getMessageAdapter().youAreNowAddedToEasyBuy());
        }
        return true;
    }
    
    @EventHandler
    public void interact(final PlayerInteractEvent e) {
        if (!this.enabledAdmin.containsKey(e.getPlayer().getUniqueId())) {
            return;
        }
        if (!this.hasMassBuyItemInHand(e.getPlayer())) {
            return;
        }
        e.setCancelled(true);
        final Location location = e.getPlayer().getLocation();
        if (!this.worldGuardAdapter.getRegion(location).isPresent()) {
            this.worldGuardAdapter.generateChunk(location.getChunk());
            this.worldGuardAdapter.safeChanges(location.getWorld());
        }
        if (this.worldGuardAdapter.getRegion(location).isPresent()) {
            final ProtectedRegion rg = this.worldGuardAdapter.getRegion(location).get();
            if (rg.hasMembersOrOwners()) {
                e.getPlayer().sendMessage(this.chunkGuardPlugin.getMessageAdapter().chunkAvailableMessage());
                return;
            }
            rg.setFlag((Flag)DefaultFlag.BUYABLE, (Object)false);
            this.worldGuardAdapter.safeChanges(location.getWorld());
            e.getPlayer().sendMessage(this.chunkGuardPlugin.getMessageAdapter().chunkIsNowCreated());
        }
    }
    
    @EventHandler
    public void onChunkChange(final PlayerMoveEvent e) {
        if (!this.enabledAdmin.containsKey(e.getPlayer().getUniqueId())) {
            return;
        }
        if (!this.maxSpeed.hasAllowedSpeed(e.getPlayer())) {
            this.maxSpeed.resetSpeed(e.getPlayer());
        }
        if (e.getFrom().getChunk().equals(e.getTo().getChunk())) {
            return;
        }
        if (!this.hasMassBuyItemInHand(e.getPlayer())) {
            return;
        }
        final Optional<ProtectedRegion> regionOp = this.worldGuardAdapter.getRegion(e.getTo());
        final boolean isOccupied = regionOp.isPresent();
        if (isOccupied) {
            ParticleEffect effect;
            if (this.worldGuardAdapter.hasOwner(e.getTo())) {
                effect = ParticleEffect.BARRIER;
            }
            else {
                effect = ParticleEffect.CLOUD;
            }
            new ChunkLookupListener(this.chunkGuardPlugin).playAtChunk(Optional.of(e.getPlayer()), e.getTo().getChunk(), effect);
        }
    }
    
    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        if (this.enabledAdmin.containsKey(e.getPlayer().getUniqueId())) {
            this.enabledAdmin.get(e.getPlayer().getUniqueId()).resetSpeed(e.getPlayer());
        }
        this.enabledAdmin.remove(e.getPlayer().getUniqueId());
    }
    
    private boolean hasMassBuyItemInHand(final Player player) {
        if (player.getItemInHand() != null && player.getItemInHand().getType().equals((Object)this.config.getMassBuyItem())) {
            return true;
        }
        player.sendMessage(this.chunkGuardPlugin.getMessageAdapter().youNeedAnItemOfCoalInYourHandToUseThisMode().replace("{itemtype}", this.config.getMassBuyItem().name()));
        return false;
    }
    
    private void setSpeed(final Player p, final float speed) {
        if (p.isFlying()) {
            p.setFlySpeed(speed);
        }
        else {
            p.setWalkSpeed(speed);
        }
    }
    
    private float getSpeed(final Player p) {
        if (p.isFlying()) {
            return p.getFlySpeed();
        }
        return p.getWalkSpeed();
    }
    
    public void onDisable() {
        this.enabledAdmin.forEach((id, container) -> container.resetSpeed(Bukkit.getPlayer(id)));
    }
    
    private class SpeedContainer
    {
        private float flySpeed;
        private float walkSpeed;
        
        public SpeedContainer(final Player p) {
            this.flySpeed = p.getFlySpeed();
            this.walkSpeed = p.getWalkSpeed();
        }
        
        public SpeedContainer(final float flySpeed, final float walkSpeed) {
            this.flySpeed = flySpeed;
            this.walkSpeed = walkSpeed;
        }
        
        public float getWalkSpeed() {
            return this.walkSpeed;
        }
        
        public float getFlySpeed() {
            return this.flySpeed;
        }
        
        public boolean hasAllowedSpeed(final Player p) {
            if (p.isFlying()) {
                return p.getFlySpeed() < this.flySpeed;
            }
            return p.getWalkSpeed() < this.walkSpeed;
        }
        
        public void resetSpeed(final Player p) {
            if (p.isFlying()) {
                p.setFlySpeed(this.flySpeed);
            }
            else {
                p.setWalkSpeed(this.walkSpeed);
            }
        }
        
        public void resetAllSpeed(final Player p) {
            p.setFlySpeed(this.flySpeed);
            p.setWalkSpeed(this.walkSpeed);
        }
    }
}
