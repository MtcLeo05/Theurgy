package com.leo.theurgy.api.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseMionHandlerMenuProviderBE extends BaseMionHandlerBE implements MenuProvider {
    public BaseMionHandlerMenuProviderBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }
}
