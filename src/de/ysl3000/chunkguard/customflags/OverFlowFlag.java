package de.ysl3000.chunkguard.customflags;

import com.sk89q.worldguard.protection.flags.*;

public class OverFlowFlag
{
    public static final StateFlag WATER_OVERFLOW;
    public static final StateFlag LAVA_OVERFLOW;
    public static final StateFlag BLOCK_FORM_OVERFLOW;
    
    static {
        WATER_OVERFLOW = new StateFlag("water-overflow", false);
        LAVA_OVERFLOW = new StateFlag("lava-overflow", false);
        BLOCK_FORM_OVERFLOW = new StateFlag("block-form-overflow", false);
    }
}
