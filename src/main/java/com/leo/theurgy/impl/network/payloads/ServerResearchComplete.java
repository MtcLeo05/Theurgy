package com.leo.theurgy.impl.network.payloads;

import com.leo.theurgy.api.data.player.PlayerData;
import com.leo.theurgy.api.research.ResearchType;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.init.TheurgyAttachmentTypes;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record ServerResearchComplete(List<ResourceLocation> researches, Optional<ResourceLocation> entryToComplete) implements CustomPacketPayload {
    public static final Type<ServerResearchComplete> TYPE = new Type<>(TheurgyConstants.modLoc("complete_research"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerResearchComplete> STREAM_CODEC = StreamCodec.composite(
        ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)),
        ServerResearchComplete::researches,
        ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC),
        ServerResearchComplete::entryToComplete,
        ServerResearchComplete::new
    );

    public static void complete(ServerResearchComplete packetData, IPayloadContext context) {
        if(!(context.player() instanceof ServerPlayer player)) return;

        Registry<ResearchType> registry = player.serverLevel().registryAccess().registryOrThrow(TheurgyConstants.RESEARCH_REGISTRY_KEY);

        boolean allComplete = true;

        PlayerData pData = PlayerData.getOrCreate(player);

        for (ResourceLocation research : packetData.researches) {
            ResearchType researchType = registry.get(research);

            if(researchType == null) throw new RuntimeException("Tried to get a research that doesn't exist!");
            if(pData.hasResearch(research)) continue;

            if(researchType.tryComplete(player)) {
                pData = pData.addResearch(research);
                continue;
            }

            allComplete = false;
        }

        if(allComplete && packetData.entryToComplete().isPresent()) {
            pData = pData.addCompletedBookEntry(packetData.entryToComplete().get());
        }

        player.setData(TheurgyAttachmentTypes.PLAYER_DATA_ATTACHMENT, pData);
        PacketDistributor.sendToPlayer(player, pData);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
