package com.leo.theurgy.impl.block;

import com.leo.theurgy.api.util.ShapeUtil;
import com.leo.theurgy.impl.block.entity.TheurgistsCauldronBE;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TheurgistsCauldronBlock extends Block implements EntityBlock {
    public static final VoxelShape SHAPE = Shapes.or(
        ShapeUtil.shapeFromDimension(0, 0, 0, 2, 2, 2),
        ShapeUtil.shapeFromDimension(14, 0, 0, 2, 2, 2),
        ShapeUtil.shapeFromDimension(14, 0, 14, 2, 2, 2),
        ShapeUtil.shapeFromDimension(0, 0, 14, 2, 2, 2),
        ShapeUtil.shapeFromDimension(0, 2, 0, 16, 1, 16),
        ShapeUtil.shapeFromDimension(15, 3, 0, 1, 13, 15),
        ShapeUtil.shapeFromDimension(1, 3, 15, 15, 13, 1),
        ShapeUtil.shapeFromDimension(0, 3, 1, 1, 13, 15),
        ShapeUtil.shapeFromDimension(0, 3, 0, 15, 13, 1)
    );

    public TheurgistsCauldronBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return simpleCodec(TheurgistsCauldronBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TheurgistsCauldronBE(pos, state);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!(placer instanceof ServerPlayer player)) return;
        getOrCreateBE(pos, state, level).onPlace(player);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!(entity instanceof ItemEntity item)) return;
        if (!(level instanceof ServerLevel sLevel)) return;

        List<ItemStack> result = getOrCreateBE(pos, state, level).handleItem(item.getItem(), sLevel);
        for (ItemStack stack : result) {
            if(stack.isEmpty()) continue;
            ItemEntity iE = new ItemEntity(level, item.getX(), item.getY(), item.getZ(), stack);
            iE.teleportRelative(0, 0.5, 0);
            iE.addDeltaMovement(new Vec3(level.random.nextFloat() / 4, 0.1, level.random.nextFloat() / 4));
            level.addFreshEntity(iE);
        }

        item.discard();
    }

    private TheurgistsCauldronBE getOrCreateBE(BlockPos pos, BlockState state, Level level) {
        if (level.getBlockEntity(pos) instanceof TheurgistsCauldronBE be) return be;

        return new TheurgistsCauldronBE(pos, state);
    }
}
