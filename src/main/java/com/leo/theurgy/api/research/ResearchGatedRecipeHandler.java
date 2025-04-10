package com.leo.theurgy.api.research;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public interface ResearchGatedRecipeHandler {

    boolean hasResearch(ServerPlayer player);

    @Nullable
    ServerPlayer owner();
}
