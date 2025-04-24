package com.leo.theurgy.api.capability;

import com.leo.theurgy.api.capability.aspectus.IAspectusStorage;
import com.leo.theurgy.impl.TheurgyConstants;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

public class TheurgyCapabilities {

    public static final BlockCapability<IAspectusStorage, @Nullable Direction> ASPECTUS_BLOCK = BlockCapability.createSided(TheurgyConstants.modLoc("aspectus"), IAspectusStorage.class);

}
