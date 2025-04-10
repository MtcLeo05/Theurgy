package com.leo.theurgy.api.research;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public abstract class ResearchType {
    public abstract ResourceLocation researchToUnlock();

    public abstract boolean tryComplete(ServerPlayer player);

    public abstract MapCodec<? extends ResearchType> getCodec();
}
