package de.ysl3000.chunkguard.lib.nbt;

import net.minecraft.server.v1_10_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTTagAPI {
    private static NBTTagAPI instance;

    public static NBTTagAPI inst() {
        return NBTTagAPI.instance;
    }

    public INBTModifier getNBTFromItemStack(final ItemStack itemStack) {
        final net.minecraft.server.v1_10_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        if (nmsItem.getTag() == null) {
            return new NBTModifier(new NBTTagCompound());
        }
        return new NBTModifier(nmsItem.getTag());
    }

    public ItemStack setNBT(final ItemStack itemStack, final INBTModifier inbtModifier) {
        final net.minecraft.server.v1_10_R1.ItemStack its = CraftItemStack.asNMSCopy(itemStack);
        its.setTag(inbtModifier.getCompound());
        return CraftItemStack.asCraftMirror(its);
    }

    static {
        NBTTagAPI.instance = new NBTTagAPI();
    }
}
