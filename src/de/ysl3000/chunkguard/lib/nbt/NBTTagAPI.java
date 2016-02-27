package de.ysl3000.chunkguard.lib.nbt;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;

public class NBTTagAPI
{
    private static NBTTagAPI instance;
    
    public static NBTTagAPI inst() {
        return NBTTagAPI.instance;
    }
    
    public INBTModifier getNBTFromItemStack(final ItemStack itemStack) {
        final net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        if (nmsItem.getTag() == null) {
            return new NBTModifier(new NBTTagCompound());
        }
        return new NBTModifier(nmsItem.getTag());
    }
    
    public ItemStack setNBT(final ItemStack itemStack, final INBTModifier inbtModifier) {
        final net.minecraft.server.v1_8_R3.ItemStack its = CraftItemStack.asNMSCopy(itemStack);
        its.setTag(inbtModifier.getCompound());
        return (ItemStack)CraftItemStack.asCraftMirror(its);
    }
    
    public INBTModifier getNBTFromItemStack(final Entity entity) {
        return new NBTModifier(((CraftEntity)entity).getHandle().getNBTTag());
    }
    
    static {
        NBTTagAPI.instance = new NBTTagAPI();
    }
}
