package de.ysl3000.chunkguard.lib.visualisation;

import org.bukkit.plugin.java.*;
import java.util.*;
import org.bukkit.entity.*;
import de.ysl3000.chunkguard.lib.visualisation.locationmanagement.*;
import org.bukkit.*;
import org.bukkit.plugin.*;
import org.bukkit.scheduler.*;

public class ChunkEdges implements ChunkLookup
{
    private JavaPlugin plugin;
    
    public ChunkEdges(final JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void playAtChunk(final Optional<Player> p, final Chunk chunk) {
        this.playAtChunk(p, chunk, ParticleEffect.FLAME);
    }
    
    @Override
    public void playAtChunk(final Optional<Player> p, final Chunk chunk, final ParticleEffect particleEffect) {
        this.playAtChunk(p, chunk, (p1, location) -> particleEffect.display(0.0f, 0.0f, 0.0f, 0.0f, 1, location, p1));
    }
    
    @Override
    public void playAtChunk(final Optional<Player> p, final Chunk chunk, final Play play) {
        if (p.isPresent() && p.get().getVelocity().length() > 7.0) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)this.plugin, (Runnable)new Runnable() {
            @Override
            public void run() {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0         /* this */
                //     1: getfield        de/ysl3000/chunkguard/lib/visualisation/ChunkEdges$1.val$p:Ljava/util/Optional;
                //     4: invokevirtual   java/util/Optional.isPresent:()Z
                //     7: ifeq            78
                //    10: new             Lde/ysl3000/chunkguard/lib/visualisation/locationmanagement/Manager;
                //    13: dup            
                //    14: invokespecial   de/ysl3000/chunkguard/lib/visualisation/locationmanagement/Manager.<init>:()V
                //    17: astore_1        /* manager */
                //    18: aload_1         /* manager */
                //    19: aload_0         /* this */
                //    20: getfield        de/ysl3000/chunkguard/lib/visualisation/ChunkEdges$1.val$chunk:Lorg/bukkit/Chunk;
                //    23: iconst_1       
                //    24: anewarray       Lde/ysl3000/chunkguard/lib/visualisation/locationmanagement/ILocationValidator;
                //    27: dup            
                //    28: iconst_0       
                //    29: new             Lde/ysl3000/chunkguard/lib/visualisation/locationmanagement/EdgeValidator;
                //    32: dup            
                //    33: invokespecial   de/ysl3000/chunkguard/lib/visualisation/locationmanagement/EdgeValidator.<init>:()V
                //    36: aastore        
                //    37: invokeinterface de/ysl3000/chunkguard/lib/visualisation/locationmanagement/IManager.getChunkLocations:(Lorg/bukkit/Chunk;[Lde/ysl3000/chunkguard/lib/visualisation/locationmanagement/ILocationValidator;)Ljava/util/Set;
                //    42: astore_2        /* lc */
                //    43: aload_2         /* lc */
                //    44: invokeinterface java/util/Set.stream:()Ljava/util/stream/Stream;
                //    49: invokedynamic   compare:()Ljava/util/Comparator;
                //    54: invokeinterface java/util/stream/Stream.sorted:(Ljava/util/Comparator;)Ljava/util/stream/Stream;
                //    59: aload_0         /* this */
                //    60: aload_0         /* this */
                //    61: getfield        de/ysl3000/chunkguard/lib/visualisation/ChunkEdges$1.val$p:Ljava/util/Optional;
                //    64: aload_0         /* this */
                //    65: getfield        de/ysl3000/chunkguard/lib/visualisation/ChunkEdges$1.val$play:Lde/ysl3000/chunkguard/lib/visualisation/ChunkLookup$Play;
                //    68: invokedynamic   accept:(Lde/ysl3000/chunkguard/lib/visualisation/ChunkEdges$1;Ljava/util/Optional;Lde/ysl3000/chunkguard/lib/visualisation/ChunkLookup$Play;)Ljava/util/function/Consumer;
                //    73: invokeinterface java/util/stream/Stream.forEach:(Ljava/util/function/Consumer;)V
                //    78: return         
                //    LocalVariableTable:
                //  Start  Length  Slot  Name     Signature
                //  -----  ------  ----  -------  ---------------------------------------------------------------------
                //  18     60      1     manager  Lde/ysl3000/chunkguard/lib/visualisation/locationmanagement/IManager;
                //  43     35      2     lc       Ljava/util/Set;
                //  0      79      0     this     Lde/ysl3000/chunkguard/lib/visualisation/ChunkEdges$1;
                //    LocalVariableTypeTable:
                //  Start  Length  Slot  Name  Signature
                //  -----  ------  ----  ----  --------------------------------------
                //  43     35      2     lc    Ljava/util/Set<Lorg/bukkit/Location;>;
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
        });
    }
}
