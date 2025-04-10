package com.leo.theurgy.impl.block;

import com.leo.theurgy.api.util.ShapeUtil;
import com.leo.theurgy.impl.block.entity.TheurgistsBenchBE;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class TheurgistsBenchBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public static final VoxelShape SHAPE = Shapes.or(
        ShapeUtil.shapeFromDimension(0, 0,0, 16, 2, 16),
        ShapeUtil.shapeFromDimension(12, 2,2, 2, 6, 2),
        ShapeUtil.shapeFromDimension(2, 2,2, 2, 6, 2),
        ShapeUtil.shapeFromDimension(12, 2,12, 2, 6, 2),
        ShapeUtil.shapeFromDimension(2, 2,12, 2, 6, 2),
        ShapeUtil.shapeFromDimension(1, 8,1, 14, 1, 14),
        ShapeUtil.shapeFromDimension(0, 9,0, 16, 7, 16)
    );

    public TheurgistsBenchBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(TheurgistsBenchBlock::new);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TheurgistsBenchBE(pos, state);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(level.getBlockEntity(pos) != null) {
            (getOrCreateBE(pos, state, level)).dropContents();
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player instanceof ServerPlayer sPlayer) {
            sPlayer.openMenu(state.getMenuProvider(level, pos), p -> p.writeBlockPos(pos));
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player instanceof ServerPlayer sPlayer) {
            sPlayer.openMenu(state.getMenuProvider(level, pos), p -> p.writeBlockPos(pos));
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return getOrCreateBE(pos, state, level);
    }

    private TheurgistsBenchBE getOrCreateBE(BlockPos pos, BlockState state, Level level) {
        if(level.getBlockEntity(pos) instanceof TheurgistsBenchBE be) return be;

        return new TheurgistsBenchBE(pos, state);
    }
}
