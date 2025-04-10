package com.leo.theurgy.impl.command;

import com.leo.theurgy.api.data.player.PlayerData;
import com.leo.theurgy.api.research.ResearchProgress;
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

public class ResearchCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("research")
            .then(
                Commands.literal("clear_all")
                        .executes(ResearchCommand::clearAll)
            );
    }

    private static int clearAll(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = Objects.requireNonNull(context.getSource().getPlayer());

        PlayerData data = ResearchProgress.clearResearch(player);
        player.setData(TheurgyAttachmentTypes.PLAYER_DATA_ATTACHMENT, data);
        PacketDistributor.sendToPlayer(player, data);

        context.getSource().sendSuccess(
            () -> Component.translatable("command." + TheurgyConstants.MODID + ".research.clear_all.success"),
            false
        );

        return 0;
    }
}
