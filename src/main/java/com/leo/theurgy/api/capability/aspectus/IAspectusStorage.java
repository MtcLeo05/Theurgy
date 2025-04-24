package com.leo.theurgy.api.capability.aspectus;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface IAspectusStorage extends INBTSerializable<CompoundTag> {
    int maxAspectus();

    int currentAspectus();

    boolean addAspectus(int amount, boolean simulate);

    boolean removeAspectus(int amount, boolean simulate);
}
