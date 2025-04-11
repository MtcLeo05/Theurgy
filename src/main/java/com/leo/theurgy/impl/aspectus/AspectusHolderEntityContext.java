package com.leo.theurgy.impl.aspectus;

import com.leo.theurgy.api.aspectus.IAspectusHolderContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AspectusHolderEntityContext implements IAspectusHolderContext {

    private final Entity entity;
    private final @Nullable Player player;

    private AspectusHolderEntityContext(@NotNull Entity entity, Player player){
        this.entity = entity;
        this.player = player;
    }

    public static AspectusHolderEntityContext create(@NotNull Entity entity, Player player) {
        return new AspectusHolderEntityContext(entity, player);
    }

    @Override
    public @Nullable Entity entity() {
        return entity;
    }

    @Override
    public @Nullable Player player() {
        return player;
    }

    @Override
    public @Nullable Level level() {
        return player.level();
    }

    @Override
    public @Nullable ItemStack stack() {
        return null;
    }
}
