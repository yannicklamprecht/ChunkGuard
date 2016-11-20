package de.ysl3000.chunkguard.customflags;

import com.sk89q.worldguard.protection.flags.*;

public class OverFlowFlag {
    public static final StateFlag WATER_OVERFLOW = new StateFlag("water-overflow", false);
    public static final StateFlag LAVA_OVERFLOW = new StateFlag("lava-overflow", false);
    public static final StateFlag BLOCK_FORM_OVERFLOW = new StateFlag("block-form-overflow", false);
}
