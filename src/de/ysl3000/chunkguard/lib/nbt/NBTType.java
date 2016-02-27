package de.ysl3000.chunkguard.lib.nbt;

public enum NBTType
{
    SHORT(1), 
    INT(2), 
    LONG(3), 
    FLOAT(4), 
    DOUBLE(5), 
    STRING(6), 
    BYTE_ARRAY(7), 
    INT_ARRAY(8), 
    BOOLEAN(9);
    
    private int type;
    
    private NBTType(final int type) {
        this.type = type;
    }
    
    public int getType() {
        return this.type;
    }
}
