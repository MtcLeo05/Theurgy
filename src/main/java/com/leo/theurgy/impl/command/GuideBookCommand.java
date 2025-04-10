package com.leo.theurgy.impl.command;

import com.leo.theurgy.api.data.player.PlayerData;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.init.TheurgyAttachmentTypes;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Objects;

public class GuideBookCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("guidebook")
            .then(
                Commands.literal("clear_all")
                        .executes(GuideBookCommand::clearAll)
            )
            .then(
                Commands.literal("unlock_all")
                    .executes(GuideBookCommand::unlockAll)
            );
    }

    private static int unlockAll(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = Objects.requireNonNull(context.getSource().getPlayer());

        PlayerData data = PlayerData.unlockGuideBook(player);
        
        player.setData(TheurgyAttachmentTypes.PLAYER_DATA_ATTACHMENT, data);
        PacketDistributor.sendToPlayer(player, data);

        context.getSource().sendSuccess(
            () -> Component.translatable("command." + TheurgyConstants.MODID + ".guidebook.unlock_all.success"),
            false
        );

        return 0;
    }

    private static int clearAll(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = Objects.requireNonNull(context.getSource().getPlayer());

        PlayerData data = PlayerData.clearGuideBook(player);

        player.setData(TheurgyAttachmentTypes.PLAYER_DATA_ATTACHMENT, data);
        PacketDistributor.sendToPlayer(player, data);

        context.getSource().sendSuccess(
            () -> Component.translatable("command." + TheurgyConstants.MODID + ".guidebook.clear_all.success"),
            false
        );

        return 0;
    }
}
