package com.leo.theurgy.impl.init;

import com.leo.theurgy.api.data.aspectus.AspectusHolder;
import com.leo.theurgy.api.data.mion.ChunkMion;
import com.leo.theurgy.api.data.player.PlayerData;
import com.leo.theurgy.impl.TheurgyConstants;
import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class TheurgyAttachmentTypes {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, TheurgyConstants.MODID);

    public static final Supplier<AttachmentType<ChunkMion>> CHUNK_MION_ATTACHMENT = ATTACHMENT_TYPES.register(
        "chunk_mion",
        () -> AttachmentType.builder(() -> new ChunkMion(0, 0, 0, 120))
            .serialize(ChunkMion.CODEC)
            .build()
    );

    public static final Supplier<AttachmentType<Integer>> CHUNK_TICK_AMOUNT = ATTACHMENT_TYPES.register(
        "chunk_ticks",
        () -> AttachmentType.builder(() -> 0)
            .serialize(Codec.INT)
            .build()
    );

    public static final Supplier<AttachmentType<AspectusHolder>> ITEM_ASPECTUS_ATTACHMENT = ATTACHMENT_TYPES.register(
        "item_aspectus",
        () -> AttachmentType.builder(() -> new AspectusHolder(null, -1))
            .serialize(AspectusHolder.CODEC)
            .build()
    );

    public static final Supplier<AttachmentType<PlayerData>> PLAYER_DATA_ATTACHMENT = ATTACHMENT_TYPES.register(
        "player_data",
        () -> AttachmentType.builder(PlayerData::new)
            .serialize(PlayerData.CODEC)
            .copyOnDeath()
            .build()
    );
}
