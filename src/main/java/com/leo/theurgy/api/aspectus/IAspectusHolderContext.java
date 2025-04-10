package com.leo.theurgy.api.aspectus;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface IAspectusHolderContext {

    @Nullable Entity entity();
    @Nullable Player player();
    @Nullable Level level();
    @Nullable ItemStack stack();
}
