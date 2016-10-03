package de.ysl3000.chunkguard.lib.nbt;

import net.minecraft.server.v1_10_R1.*;

public interface INBTModifier
{
    INBTModifier setShort(final String p0, final short p1);
    
    INBTModifier setInt(final String p0, final int p1);
    
    INBTModifier setLong(final String p0, final long p1);
    
    INBTModifier setFloat(final String p0, final float p1);
    
    INBTModifier setDouble(final String p0, final double p1);
    
    INBTModifier setString(final String p0, final String p1);
    
    INBTModifier setByteArray(final String p0, final byte[] p1);
    
    INBTModifier setIntArray(final String p0, final int[] p1);
    
    INBTModifier setBoolean(final String p0, final boolean p1);
    
    NBTTagCompound getCompound();
    
    byte getByte(final String p0);
    
    short getShort(final String p0);
    
    int getInt(final String p0);
    
    long getLong(final String p0);
    
    float getFloat(final String p0);
    
    double getDouble(final String p0);
    
    String getString(final String p0);
    
    byte[] getByteArray(final String p0);
    
    int[] getIntArray(final String p0);
    
    boolean getBoolean(final String p0);
    
    boolean hasKey(final String p0);
    
    boolean hasKeyOfType(final String p0, final NBTType p1);
}
