package com.leo.theurgy.impl.aspectus;

import com.leo.theurgy.api.aspectus.IAspectusHolderContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AspectusHolderItemContext implements IAspectusHolderContext {

    private ItemStack stack;
    private @Nullable Player holder;

    private AspectusHolderItemContext(@NotNull ItemStack stack, @Nullable Player holder){
        this.stack = stack;
        this.holder = holder;
    }

    public static AspectusHolderItemContext create(@NotNull ItemStack stack, @Nullable Player holder) {
        return new AspectusHolderItemContext(stack, holder);
    }

    @Override
    public @Nullable Entity entity() {
        return player();
    }

    @Override
    public @Nullable Player player() {
        return holder;
    }

    @Override
    public @Nullable Level level() {
        return holder != null? holder.level(): null;
    }

    @Override
    public @Nullable ItemStack stack() {
        return stack;
    }
}
