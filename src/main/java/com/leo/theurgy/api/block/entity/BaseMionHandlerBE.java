package com.leo.theurgy.api.block.entity;

import com.leo.theurgy.api.data.mion.ChunkMion;
import com.leo.theurgy.impl.init.TheurgyAttachmentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

public abstract class BaseMionHandlerBE extends BaseTheurgyInventoryBE {

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int i) {
            return i == 0? getMionDataInChunk().currentMion(): -1;
        }

        @Override
        public void set(int i, int i1) {

        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public BaseMionHandlerBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    protected ChunkMion getMionDataInChunk(){
        return ChunkMion.getOrCreateMion(getBlockPos(), getLevel());
    }

    private ChunkAccess getChunk() {
        return getLevel().getChunk(getBlockPos());
    }

    protected void removeMionToChunk(int mion){
        ChunkMion cMion = getMionDataInChunk();

        int mionValue = cMion.currentMion() - mion;
        mionValue = Math.clamp(mionValue, 0, cMion.maxMion());

        cMion = new ChunkMion(mionValue, cMion.maxMion(), cMion.corruptMion(), cMion.mionRegenCD());
        getChunk().setData(TheurgyAttachmentTypes.CHUNK_MION_ATTACHMENT, cMion);
    }


    protected boolean doesChunkHaveMion(int mion) {
        ChunkMion cMion = getMionDataInChunk();
        return cMion.currentMion() >= mion;
    }
}
