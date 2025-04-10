package com.leo.theurgy.api.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

public class RotatedStrippableBlock extends RotatedPillarBlock {
    private final Block stripBlock;

    public RotatedStrippableBlock(Properties properties, Block stripBlock) {
        super(properties);
        this.stripBlock = stripBlock;
    }

    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
        if(!itemAbility.equals(ItemAbilities.AXE_STRIP) || simulate) return state;
        Direction.Axis axis = state.getValue(RotatedStrippableBlock.AXIS);
        return stripBlock.defaultBlockState().setValue(RotatedStrippableBlock.AXIS, axis);
    }

}