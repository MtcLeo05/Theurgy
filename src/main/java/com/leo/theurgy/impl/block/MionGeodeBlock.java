package com.leo.theurgy.impl.block;

import com.leo.theurgy.api.block.DirectionalBlock;
import com.leo.theurgy.api.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MionGeodeBlock extends DirectionalBlock {

    public static VoxelShape UP_SHAPE = Shapes.or(
        ShapeUtil.shapeFromDimension(7, 6, 7, 3, 6, 3),
        ShapeUtil.shapeFromDimension(7, 5, 5, 2, 4, 2),
        ShapeUtil.shapeFromDimension(5, 4, 7, 2, 4, 2),
        ShapeUtil.shapeFromDimension(7, 0, 7, 4, 6, 4),
        ShapeUtil.shapeFromDimension(6, 3, 6, 1, 3, 1),
        ShapeUtil.shapeFromDimension(7, 0, 4, 3, 5, 3),
        ShapeUtil.shapeFromDimension(4, 0, 7, 3, 4, 3),
        ShapeUtil.shapeFromDimension(5, 0, 5, 2, 3, 2)
    );

    public static VoxelShape NORTH_SHAPE = Shapes.or(
        ShapeUtil.shapeFromDimension(7, 7, 10, 4, 4, 6),
        ShapeUtil.shapeFromDimension(7, 4, 11, 3, 3, 5),
        ShapeUtil.shapeFromDimension(4, 7, 12, 3, 3, 4),
        ShapeUtil.shapeFromDimension(5, 5, 13, 2, 2, 3),
        ShapeUtil.shapeFromDimension(7, 7, 4, 3, 3, 6),
        ShapeUtil.shapeFromDimension(5, 7, 8, 2, 2, 4),
        ShapeUtil.shapeFromDimension(6, 6, 10, 1, 1, 3),
        ShapeUtil.shapeFromDimension(7, 5, 7, 2, 2, 4)
    );

    public MionGeodeBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case DOWN -> ShapeUtil.multiRotation(UP_SHAPE, 2, 0);
            case UP -> UP_SHAPE;
            case SOUTH -> ShapeUtil.multiRotation(NORTH_SHAPE, 0, 2);
            case WEST -> ShapeUtil.multiRotation(NORTH_SHAPE, 0, 3);
            case NORTH -> ShapeUtil.multiRotation(NORTH_SHAPE,  0, 0);
            case EAST -> ShapeUtil.multiRotation(NORTH_SHAPE, 0, 1);
        };
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if(!state.hasProperty(FACING)) return false;

        Direction opposite = state.getValue(FACING).getOpposite();
        BlockState potentialSupport = level.getBlockState(pos.relative(opposite, 1));

        return !potentialSupport.is(this) && canSupportCenter(level, pos.relative(opposite, 1), opposite);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if(canSurvive(state, level, pos)) return state;

        return Blocks.AIR.defaultBlockState();
    }
}
