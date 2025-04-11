package com.leo.theurgy.api.data.player;

import com.leo.theurgy.api.aspectus.IAspectusHolderContext;
import com.leo.theurgy.api.data.aspectus.Aspectus;
import com.leo.theurgy.api.data.mion.ChunkMion;
import com.leo.theurgy.api.guidebook.data.GuideBookCategory;
import com.leo.theurgy.api.guidebook.data.GuideBookEntry;
import com.leo.theurgy.api.guidebook.data.GuideBookReloadListener;
import com.leo.theurgy.api.research.ResearchProgress;
import com.leo.theurgy.api.util.ListUtil;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.init.TheurgyAttachmentTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record PlayerData(int checkDelay, int maxCheckDelay, ResearchProgress researchProgress,
                         List<ResourceLocation> guideBookEntries,
                         boolean bookGiven, PlayerKnowledge knowledge) implements CustomPacketPayload {
    public static final Type<PlayerData> TYPE = new Type<>(TheurgyConstants.modLoc("player_data"));

    public static final Codec<PlayerData> CODEC = RecordCodecBuilder.create(
        inst -> inst.group(
            Codec.INT.fieldOf("check_delay").forGetter(PlayerData::checkDelay),
            Codec.INT.fieldOf("max_check_delay").forGetter(PlayerData::maxCheckDelay),
            ResearchProgress.CODEC.fieldOf("research_progress").forGetter(PlayerData::researchProgress),
            ResourceLocation.CODEC.listOf().fieldOf("guidebook_entries").forGetter(PlayerData::guideBookEntries),
            Codec.BOOL.fieldOf("book_given").forGetter(PlayerData::bookGiven),
            PlayerKnowledge.CODEC.fieldOf("knowledge").forGetter(PlayerData::knowledge)
        ).apply(inst, PlayerData::new)
    );

    public static final StreamCodec<FriendlyByteBuf, PlayerData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT,
        PlayerData::checkDelay,
        ByteBufCodecs.INT,
        PlayerData::maxCheckDelay,
        ResearchProgress.STREAM_CODEC,
        PlayerData::researchProgress,
        ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)),
        PlayerData::guideBookEntries,
        ByteBufCodecs.BOOL,
        PlayerData::bookGiven,
        PlayerKnowledge.STREAM_CODEC,
        PlayerData::knowledge,
        PlayerData::new
    );

    public static PlayerData clearGuideBook(ServerPlayer player) {
        PlayerData data = PlayerData.getOrCreate(player);
        return new PlayerData(data.checkDelay, data.maxCheckDelay, data.researchProgress, new ArrayList<>(), data.bookGiven, data.knowledge);
    }

    public static PlayerData unlockGuideBook(ServerPlayer player) {
        PlayerData data = PlayerData.getOrCreate(player);

        GuideBookReloadListener.getInstance().getRegisteredGuideBookCategories().forEach(
            (k, c) -> {
                for (GuideBookEntry entry : c.entries()) {
                    data.addCompletedBookEntry(c, entry);
                }
            }
        );

        return data;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public PlayerData() {
        this(0, 20, new ResearchProgress(), new ArrayList<>(), false, new PlayerKnowledge());
    }

    public static void handleTick(ServerPlayer player) {
        PlayerData data = PlayerData.getOrCreate(player);

        if (data.checkDelay() < data.maxCheckDelay()) {
            data = data.increaseCheckDelay();
            player.setData(TheurgyAttachmentTypes.PLAYER_DATA_ATTACHMENT, data);
            PacketDistributor.sendToPlayer(player, data);

            return;
        }

        PlayerData.forceChunkUpdate(player);
        player.setData(TheurgyAttachmentTypes.PLAYER_DATA_ATTACHMENT, data.resetCheckDelay());
    }

    public static void forceChunkUpdate(ServerPlayer player) {
        BlockPos pos = player.getOnPos();
        Level level = player.level();
        BlockPos cPos = level.getChunkAt(pos).getPos().getMiddleBlockPosition(pos.getY());

        ChunkMion mion = ChunkMion.getOrCreateMion(pos, level, level.getBiome(cPos));
        //send mion to player
        //Theurgy.LOGGER.info("ChunkMion at pos ${}: ${}", pos, mion);
    }

    public PlayerData increaseCheckDelay() {
        return new PlayerData(checkDelay() + 1, maxCheckDelay(), researchProgress(), guideBookEntries(), bookGiven(), knowledge());
    }

    public PlayerData addResearch(ResourceLocation research) {
        return ResearchProgress.addResearch(this, research);
    }

    public PlayerData addResearch(List<ResourceLocation> research) {
        return ResearchProgress.addResearch(this, research);
    }

    public PlayerData resetCheckDelay() {
        return new PlayerData(0, maxCheckDelay(), researchProgress(), guideBookEntries(), bookGiven(), knowledge());
    }

    public PlayerData giveBook() {
        return new PlayerData(checkDelay(), maxCheckDelay(), researchProgress(), guideBookEntries(), true, knowledge());
    }

    public static PlayerData getOrCreate(Player player) {
        if (player.hasData(TheurgyAttachmentTypes.PLAYER_DATA_ATTACHMENT)) {
            return player.getData(TheurgyAttachmentTypes.PLAYER_DATA_ATTACHMENT);
        }

        PlayerData playerData = new PlayerData();
        player.setData(TheurgyAttachmentTypes.PLAYER_DATA_ATTACHMENT, playerData);

        return playerData;
    }

    public static PlayerData getOrCreateClient() {
        return PlayerData.getOrCreate(Minecraft.getInstance().player);
    }

    public boolean hasResearch(ResourceLocation researchId) {
        return researchProgress().completedResearch().contains(researchId);
    }

    public static void handleData(PlayerData data, IPayloadContext context) {
        new DirectionalPayloadHandler<>(
            handleDataOnClient(),
            handleDataOnServer()
        ).handle(data, context);
    }

    private static IPayloadHandler<PlayerData> handleDataOnClient() {
        return (data, context) -> Minecraft.getInstance().player.setData(TheurgyAttachmentTypes.PLAYER_DATA_ATTACHMENT, data);
    }

    private static IPayloadHandler<PlayerData> handleDataOnServer() {
        return (data, context) -> context.player().setData(TheurgyAttachmentTypes.PLAYER_DATA_ATTACHMENT, data);
    }

    public PlayerData addCompletedBookEntry(GuideBookCategory category, GuideBookEntry entry) {
        ResourceLocation cId = category.id();
        ResourceLocation eId = entry.id();

        ResourceLocation entryId = TheurgyConstants.makeGuideBookEntry(cId, eId);
        return addCompletedBookEntry(entryId);
    }

    public PlayerData addCompletedBookEntry(ResourceLocation id) {
        List<ResourceLocation> mutable = ListUtil.mutable(guideBookEntries);

        if (!mutable.contains(id)) mutable.add(id);

        return new PlayerData(checkDelay(), maxCheckDelay(), researchProgress(), mutable, bookGiven(), knowledge());
    }

    public boolean isEntryCompleted(GuideBookCategory category, GuideBookEntry entry) {
        ResourceLocation cId = category.id();
        ResourceLocation eId = entry.id();

        ResourceLocation entryId = ResourceLocation.fromNamespaceAndPath(cId.getNamespace(), cId.getPath() + "/" + eId.getPath());

        return guideBookEntries.contains(entryId);
    }

    public boolean hasResearches(List<ResourceLocation> research) {
        for (ResourceLocation r : research) {
            if (!hasResearch(r)) return false;
        }

        return true;
    }

    public PlayerData scanItem(IAspectusHolderContext context) {
        if (scannedItem(context.stack())) return this;

        return new PlayerData(checkDelay, maxCheckDelay, researchProgress, guideBookEntries, bookGiven, knowledge.scanItem(context));
    }


    public PlayerData scanEntity(IAspectusHolderContext context) {
        if (knowledge().knowsEntity(context)) return this;

        return null;
    }

    public boolean scannedItem(ItemStack item) {
        return knowledge().knowsItem(item);
    }

    public Map<Aspectus, Integer> getAspectus(Level level, ItemStack item) {
        return knowledge().getAspectus(level, item);
    }

    public PlayerData clearKnowledge() {
        return new PlayerData(checkDelay, maxCheckDelay, researchProgress, guideBookEntries, bookGiven, knowledge.clear());
    }
}
