package de.ysl3000.chunkguard.lib.nbt;

import net.minecraft.server.v1_11_R1.*;

public class NBTModifier implements INBTModifier
{
    private NBTTagCompound compound;
    
    public NBTModifier(final NBTTagCompound compound) {
        this.compound = compound;
    }

    public NBTTagCompound getCompound() {
        return this.compound;
    }
    
    @Override
    public INBTModifier setShort(final String key, final short value) {
        this.compound.setShort(key, value);
        return this;
    }
    
    @Override
    public INBTModifier setInt(final String key, final int value) {
        this.compound.setInt(key, value);
        return this;
    }
    
    @Override
    public INBTModifier setLong(final String key, final long value) {
        this.compound.setLong(key, value);
        return this;
    }
    
    @Override
    public INBTModifier setFloat(final String key, final float value) {
        this.compound.setFloat(key, value);
        return this;
    }
    
    @Override
    public INBTModifier setDouble(final String key, final double value) {
        this.compound.setDouble(key, value);
        return this;
    }
    
    @Override
    public INBTModifier setString(final String key, final String value) {
        this.compound.setString(key, value);
        return this;
    }
    
    @Override
    public INBTModifier setByteArray(final String key, final byte[] value) {
        this.compound.setByteArray(key, value);
        return this;
    }
    
    @Override
    public INBTModifier setIntArray(final String key, final int[] value) {
        this.compound.setIntArray(key, value);
        return this;
    }
    
    @Override
    public INBTModifier setBoolean(final String key, final boolean value) {
        this.compound.setBoolean(key, value);
        return this;
    }
    
    @Override
    public byte getByte(final String key) {
        return this.compound.getByte(key);
    }
    
    @Override
    public short getShort(final String key) {
        return this.compound.getShort(key);
    }
    
    @Override
    public int getInt(final String key) {
        return this.compound.getInt(key);
    }
    
    @Override
    public long getLong(final String key) {
        return this.compound.getLong(key);
    }
    
    @Override
    public float getFloat(final String key) {
        return this.compound.getFloat(key);
    }
    
    @Override
    public double getDouble(final String key) {
        return this.compound.getDouble(key);
    }
    
    @Override
    public String getString(final String key) {
        return this.compound.getString(key);
    }
    
    @Override
    public byte[] getByteArray(final String key) {
        return this.compound.getByteArray(key);
    }
    
    @Override
    public int[] getIntArray(final String key) {
        return this.compound.getIntArray(key);
    }
    
    @Override
    public boolean getBoolean(final String key) {
        return this.compound.getBoolean(key);
    }
    
    @Override
    public boolean hasKey(final String key) {
        return this.compound.hasKey(key);
    }
    
    @Override
    public boolean hasKeyOfType(final String key, final NBTType type) {
        return this.compound.hasKeyOfType(key, type.getType());
    }
}
