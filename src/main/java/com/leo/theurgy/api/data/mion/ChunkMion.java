package com.leo.theurgy.api.data.mion;

import com.google.gson.annotations.Expose;
import com.leo.theurgy.impl.config.ChunkMionConfig;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.config.ConfigLoader;
import com.leo.theurgy.impl.init.TheurgyAttachmentTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;

public record ChunkMion(@Expose int currentMion, @Expose int maxMion,
                        @Expose int corruptMion, @Expose int mionRegenCD) implements CustomPacketPayload {

    public static final Type<ChunkMion> TYPE = new Type<>(TheurgyConstants.modLoc("chunk_mion"));


    public static void handleChunkTick(ServerLevel level, LevelChunk chunk, int tickCount) {
        ChunkMion mion = ChunkMion.getOrCreateMion(chunk.getPos().getMiddleBlockPosition(0), level);

        if(tickCount % mion.mionRegenCD() != 0) return;

        chunk.setData(TheurgyAttachmentTypes.CHUNK_MION_ATTACHMENT, mion.increaseMion(1));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final Codec<ChunkMion> CODEC = RecordCodecBuilder.create(
        inst -> inst.group(
            Codec.INT.fieldOf("currentMion").forGetter(ChunkMion::currentMion),
            Codec.INT.fieldOf("maxMion").forGetter(ChunkMion::maxMion),
            Codec.INT.fieldOf("corruptMion").forGetter(ChunkMion::corruptMion),
            Codec.INT.fieldOf("mionRegenCD").forGetter(ChunkMion::mionRegenCD)
        ).apply(inst, ChunkMion::new)
    );

    public static final StreamCodec<FriendlyByteBuf, ChunkMion> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT,
        ChunkMion::currentMion,
        ByteBufCodecs.INT,
        ChunkMion::maxMion,
        ByteBufCodecs.INT,
        ChunkMion::corruptMion,
        ByteBufCodecs.INT,
        ChunkMion::mionRegenCD,
        ChunkMion::new
    );

    public static ChunkMion getOrCreateMion(BlockPos pos, Level level) {
        Holder<Biome> biome = level.getBiome(pos);
        return ChunkMion.getOrCreateMion(pos, level, biome);
    }

    public static ChunkMion getOrCreateMion(BlockPos pos, Level level, Holder<Biome> biome) {
        LevelChunk chunk = level.getChunkAt(pos);

        if (chunk.hasData(TheurgyAttachmentTypes.CHUNK_MION_ATTACHMENT)) {
            ChunkMion data = chunk.getData(TheurgyAttachmentTypes.CHUNK_MION_ATTACHMENT);

            if(data.mionRegenCD() > 0) return data;
            return new ChunkMion(data.currentMion(), data.maxMion(), data.corruptMion(), 120);
        }

        RandomSource random = level.getRandom();

        ChunkMion chunkMion = null;

        ChunkMionConfig mionConfig = ConfigLoader.getInstance().getMion(biome.getKey());

        if (mionConfig != null) {
            int maxMion = mionConfig.maxMion();
            maxMion = variatedMion(maxMion, mionConfig.variance(), random);

            chunkMion = new ChunkMion(maxMion, maxMion, 0, mionConfig.mionRegenCD());
        }

        mionConfig = ConfigLoader.getInstance().getTagMion(biome);

        if (mionConfig != null) {
            int maxMion = mionConfig.maxMion();
            maxMion = variatedMion(maxMion, mionConfig.variance(), random);

            chunkMion = new ChunkMion(maxMion, maxMion, 0, mionConfig.mionRegenCD());
        }

        if (chunkMion == null) {
            chunkMion = new ChunkMion(0, 0, 0, 120);
        }
        chunk.setData(TheurgyAttachmentTypes.CHUNK_MION_ATTACHMENT, chunkMion);

        return chunkMion;
    }

    private static int variatedMion(int maxMion, float variance, RandomSource random) {
        float randomValue = (random.nextFloat() * 2 - 1) * variance; //Random number between 0 - 1, * 2 - 1 becomes between -1 and 1, * variance becomes between -variance and variance

        return (int) (maxMion + (maxMion * randomValue));
    }

    public ChunkMion increaseMion(int amount) {
        if(maxMion() <= 0) return this;

        int increasedMion = currentMion() + amount;
        int maxMion = maxMion() - corruptMion();

        int newMion = Mth.clamp(increasedMion, 0, maxMion);
        return new ChunkMion(newMion, maxMion(), corruptMion(), mionRegenCD());
    }
}
