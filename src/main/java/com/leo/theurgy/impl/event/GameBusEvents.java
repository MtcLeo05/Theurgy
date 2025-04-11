package com.leo.theurgy.impl.event;

import com.leo.theurgy.api.data.mion.ChunkMion;
import com.leo.theurgy.api.data.player.PlayerData;
import com.leo.theurgy.api.event.ChunkTickEvent;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.config.ConfigReloadListener;
import com.leo.theurgy.impl.config.ConfigLoader;
import com.leo.theurgy.impl.init.TheurgyAttachmentTypes;
import com.leo.theurgy.impl.init.TheurgyItems;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EventBusSubscriber(modid = TheurgyConstants.MODID, bus = EventBusSubscriber.Bus.GAME)
public class GameBusEvents {

    @SubscribeEvent
    public static void playerTickEvent(PlayerTickEvent.Post event){
        if(event.getEntity().level().isClientSide) return;

        if(!(event.getEntity() instanceof ServerPlayer player)) return;

        PlayerData.handleTick(player);
    }

    @SubscribeEvent
    public static void addReloadListenersEvent(AddReloadListenerEvent event) {
        event.addListener(new ConfigReloadListener());
    }

    @SubscribeEvent
    public static void playerChangeChunkEvent(EntityEvent.EnteringSection event) {
        if(!event.didChunkChange()) return;
        if(!(event.getEntity() instanceof ServerPlayer player)) return;

        PlayerData.forceChunkUpdate(player);
    }

    @SubscribeEvent
    public static void levelTickEvent(LevelTickEvent.Post event) {
        if(!(event.getLevel() instanceof ServerLevel level)) return;

        List<ChunkHolder> chunkHolders = new ArrayList<>();
        level.getChunkSource().chunkMap.getChunks().forEach(chunkHolders::add);

        List<LevelChunk> tickingChunks = chunkHolders
            .stream()
            .map(ChunkHolder::getTickingChunk)
            .filter(Objects::nonNull)
            .toList();

        for (LevelChunk chunk : tickingChunks) {
            NeoForge.EVENT_BUS.post(new ChunkTickEvent(chunk));
        }
    }

    @SubscribeEvent
    public static void chunkTickEvent(ChunkTickEvent event) {
        LevelChunk chunk = event.chunk();
        if(!(chunk.getLevel() instanceof ServerLevel level)) return;

        int tickCount = chunk.getData(TheurgyAttachmentTypes.CHUNK_TICK_AMOUNT);
        if(++tickCount >= 255) tickCount = 0;
        chunk.setData(TheurgyAttachmentTypes.CHUNK_TICK_AMOUNT, tickCount);

        ChunkMion.handleChunkTick(level, chunk, tickCount);
    }

    @SubscribeEvent
    public static void playerJoinLevel(EntityJoinLevelEvent event) {
        if(!(event.getEntity() instanceof ServerPlayer player)) return;
        if(!ConfigLoader.getInstance().giveBook) return;
        PlayerData data = PlayerData.getOrCreate(player);

        if(data.bookGiven()) return;

        data = data.giveBook();
        player.setData(TheurgyAttachmentTypes.PLAYER_DATA_ATTACHMENT, data);
        PacketDistributor.sendToPlayer(player, data);
        player.addItem(new ItemStack(TheurgyItems.GUIDE_BOOK.get()));
    }
}
