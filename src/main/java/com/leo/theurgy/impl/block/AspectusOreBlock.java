package com.leo.theurgy.impl.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;

public class AspectusOreBlock extends Block {
    final MionGeodeBlock block;

    public AspectusOreBlock(Properties properties, DeferredHolder<Block, MionGeodeBlock> crystal) {
        super(properties);
        block = crystal.get();
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if(random.nextFloat() > 0.1f) return;

        Direction dir = Direction.getRandom(random);

        BlockPos target = pos.relative(dir, 1);
        BlockState targetState = level.getBlockState(target);
        if(!targetState.isAir() && !targetState.canBeReplaced()) return;

        level.setBlock(target, block.defaultBlockState().setValue(MionGeodeBlock.FACING, dir), Block.UPDATE_ALL);
        super.randomTick(state, level, pos, random);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }
}
