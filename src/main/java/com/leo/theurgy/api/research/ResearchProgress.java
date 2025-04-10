package com.leo.theurgy.api.research;

import com.leo.theurgy.api.data.player.PlayerData;
import com.leo.theurgy.api.util.ListUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public record ResearchProgress(List<ResourceLocation> completedResearch) {

    public static final Codec<ResearchProgress> CODEC = RecordCodecBuilder.create(
        inst -> inst.group(
            ResourceLocation.CODEC.listOf().fieldOf("completed_research").forGetter(ResearchProgress::completedResearch)
        ).apply(inst, ResearchProgress::new)
    );

    public static final StreamCodec<FriendlyByteBuf, ResearchProgress> STREAM_CODEC = StreamCodec.composite(
        ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)),
        ResearchProgress::completedResearch,
        ResearchProgress::new
    );

    public ResearchProgress() {
        this(new ArrayList<>());
    }

    public static PlayerData clearResearch(ServerPlayer player) {
        PlayerData old = PlayerData.getOrCreate(player);

        return new PlayerData(old.checkDelay(), old.maxCheckDelay(), new ResearchProgress(), old.guideBookEntries(), old.bookGiven());
    }

    public static PlayerData addResearch(PlayerData data, ResourceLocation research) {
        List<ResourceLocation> completedResearch = ListUtil.mutable(data.researchProgress().completedResearch);
        completedResearch.add(research);

        return new PlayerData(data.checkDelay(),data.maxCheckDelay(), new ResearchProgress(completedResearch), data.guideBookEntries(), data.bookGiven());
    }

    public static PlayerData addResearch(PlayerData data, List<ResourceLocation> research) {
        List<ResourceLocation> completedResearch = ListUtil.mutable(data.researchProgress().completedResearch);
        completedResearch.addAll(research);

        return new PlayerData(data.checkDelay(),data.maxCheckDelay(), new ResearchProgress(completedResearch), data.guideBookEntries(), data.bookGiven());
    }
}
