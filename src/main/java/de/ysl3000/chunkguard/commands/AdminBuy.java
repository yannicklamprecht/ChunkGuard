package de.ysl3000.chunkguard.commands;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.ysl3000.chunkguard.ChunkGuardPlugin;
import de.ysl3000.chunkguard.config.Config;
import de.ysl3000.chunkguard.customflags.OverFlowFlag;
import de.ysl3000.chunkguard.lib.interfaces.I7WorldGuardAdapter;
import de.ysl3000.chunkguard.lib.visualisation.ChunkLookupListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AdminBuy implements CommandExecutor, Listener {

    private Map<UUID, SpeedContainer> enabledAdmin;
    private SpeedContainer maxSpeed;
    private ChunkGuardPlugin chunkGuardPlugin;
    private Config config;
    private I7WorldGuardAdapter worldGuardAdapter;

    public AdminBuy(final ChunkGuardPlugin chunkGuardPlugin) {
        this.enabledAdmin = new HashMap<>();
        this.maxSpeed = new SpeedContainer(0.04f, 0.4f);
        this.worldGuardAdapter = chunkGuardPlugin.getWorldGuardAdapter();
        this.chunkGuardPlugin = chunkGuardPlugin;
        this.config = chunkGuardPlugin.getConfiguration();
        chunkGuardPlugin.getCommand("adminbuy").setExecutor(this);
        chunkGuardPlugin.getServer().getPluginManager().registerEvents(this, chunkGuardPlugin);
    }

    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings) {
        if (!(commandSender instanceof Player)) {
            return true;
        }
        final Player player = (Player) commandSender;
        final UUID playerUUID = player.getUniqueId();
        if (this.enabledAdmin.containsKey(playerUUID)) {
            this.enabledAdmin.get(playerUUID).resetAllSpeed(player);
            this.enabledAdmin.remove(playerUUID);
            player.sendMessage(this.chunkGuardPlugin.getMessageAdapter().youAreNowRemovedFromEasyBuy());
        } else {
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
            this.worldGuardAdapter.saveChanges(location.getWorld());
        }
        if (this.worldGuardAdapter.getRegion(location).isPresent()) {
            final ProtectedRegion rg = this.worldGuardAdapter.getRegion(location).get();
            if (rg.hasMembersOrOwners()) {
                e.getPlayer().sendMessage(this.chunkGuardPlugin.getMessageAdapter().chunkAvailableMessage());
                return;
            }
            rg.setFlag(OverFlowFlag.BUYABLE, false);
            this.worldGuardAdapter.saveChanges(location.getWorld());
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
            Particle effect;
            if (this.worldGuardAdapter.hasOwner(e.getTo())) {
                effect = Particle.BARRIER;
            } else {
                effect = Particle.CLOUD;
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
        player.getInventory().getItemInMainHand();
        if (player.getInventory().getItemInMainHand().getType().equals(this.config.getMassBuyItem())) {
            return true;
        }
        player.sendMessage(this.chunkGuardPlugin.getMessageAdapter().youNeedAnItemOfCoalInYourHandToUseThisMode().replace("{itemtype}", this.config.getMassBuyItem().name()));
        return false;
    }

    private void setSpeed(final Player p, final float speed) {
        if (p.isFlying()) {
            p.setFlySpeed(speed);
        } else {
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

    private class SpeedContainer {

        private float flySpeed;
        private float walkSpeed;

        SpeedContainer(final Player p) {
            this.flySpeed = p.getFlySpeed();
            this.walkSpeed = p.getWalkSpeed();
        }

        SpeedContainer(final float flySpeed, final float walkSpeed) {
            this.flySpeed = flySpeed;
            this.walkSpeed = walkSpeed;
        }

        public float getWalkSpeed() {
            return this.walkSpeed;
        }

        public float getFlySpeed() {
            return this.flySpeed;
        }

        boolean hasAllowedSpeed(final Player p) {
            if (p.isFlying()) {
                return p.getFlySpeed() < this.flySpeed;
            }
            return p.getWalkSpeed() < this.walkSpeed;
        }

        void resetSpeed(final Player p) {
            if (p.isFlying()) {
                p.setFlySpeed(this.flySpeed);
            } else {
                p.setWalkSpeed(this.walkSpeed);
            }
        }

        void resetAllSpeed(final Player p) {
            p.setFlySpeed(this.flySpeed);
            p.setWalkSpeed(this.walkSpeed);
        }
    }
}
