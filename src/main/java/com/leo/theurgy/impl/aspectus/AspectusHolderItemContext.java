package com.leo.theurgy.impl.aspectus;

import com.leo.theurgy.api.aspectus.IAspectusHolderContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AspectusHolderItemContext implements IAspectusHolderContext {

    private final ItemStack stack;
    private final @Nullable Player holder;
    private final @Nullable Level level;

    private AspectusHolderItemContext(@NotNull ItemStack stack, @Nullable Player holder, @Nullable Level level){
        this.stack = stack;
        this.holder = holder;
        this.level = level;
    }

    public static AspectusHolderItemContext create(@NotNull ItemStack stack, @Nullable Player holder) {
        return new AspectusHolderItemContext(stack, holder, null);
    }

    public static AspectusHolderItemContext create(@NotNull ItemStack stack, @Nullable Level level) {
        return new AspectusHolderItemContext(stack, null, level);
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
        return holder != null? holder.level(): level;
    }

    @Override
    public @Nullable ItemStack stack() {
        return stack;
    }
}
