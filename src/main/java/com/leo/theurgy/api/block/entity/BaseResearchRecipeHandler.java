package com.leo.theurgy.api.block.entity;

import com.leo.theurgy.api.research.ResearchGatedRecipeHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseResearchRecipeHandler extends BaseTheurgyBE implements ResearchGatedRecipeHandler {
    public BaseResearchRecipeHandler(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }
}
