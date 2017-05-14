package de.ysl3000.chunkguard.lib.nbt;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import net.minecraft.server.v1_12_R1.ItemStack;



public class NBTTagAPI {
    private static NBTTagAPI instance;

    public static NBTTagAPI inst() {
        return NBTTagAPI.instance;
    }

    public INBTModifier getNBTFromItemStack(final org.bukkit.inventory.ItemStack itemStack) {
        final ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        if (nmsItem.getTag() == null) {
            return new NBTModifier(new NBTTagCompound());
        }
        return new NBTModifier(nmsItem.getTag());
    }

    public org.bukkit.inventory.ItemStack setNBT(final org.bukkit.inventory.ItemStack itemStack, final INBTModifier inbtModifier) {
        final ItemStack its = CraftItemStack.asNMSCopy(itemStack);
        its.setTag(((NBTModifier)inbtModifier).getCompound());
        return CraftItemStack.asCraftMirror(its);
    }

    static {
        NBTTagAPI.instance = new NBTTagAPI();
    }
}
