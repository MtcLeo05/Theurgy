package com.leo.theurgy.impl.item;

import com.leo.theurgy.api.data.player.PlayerData;
import com.leo.theurgy.impl.aspectus.AspectusHolderEntityContext;
import com.leo.theurgy.impl.aspectus.AspectusHolderItemContext;
import com.leo.theurgy.impl.init.TheurgyAttachmentTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

public class CrystalScannerItem extends Item {
    public CrystalScannerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if(!(player instanceof ServerPlayer sPlayer)) return super.useOn(context);

        PlayerData data = PlayerData.getOrCreate(sPlayer);
        ServerLevel level = sPlayer.serverLevel();
        BlockState state = level.getBlockState(context.getClickedPos());

        data = data.scanItem(AspectusHolderItemContext.create(state.getBlock().asItem().getDefaultInstance(), sPlayer));

        sPlayer.setData(TheurgyAttachmentTypes.PLAYER_DATA_ATTACHMENT, data);
        PacketDistributor.sendToPlayer(sPlayer, data);

        return InteractionResult.CONSUME;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand usedHand) {
        if(!(player instanceof ServerPlayer sPlayer)) return super.interactLivingEntity(stack, player, target, usedHand);

        PlayerData data = PlayerData.getOrCreate(sPlayer);

        data = data.scanEntity(AspectusHolderEntityContext.create(target, sPlayer));

        sPlayer.setData(TheurgyAttachmentTypes.PLAYER_DATA_ATTACHMENT, data);
        PacketDistributor.sendToPlayer(sPlayer, data);

        return InteractionResult.CONSUME;
    }
}
