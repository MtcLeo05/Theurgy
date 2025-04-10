package com.leo.theurgy.api.block.entity;

import com.leo.theurgy.api.data.mion.ChunkMion;
import com.leo.theurgy.impl.init.TheurgyAttachmentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMionHandlerBE extends BlockEntity {

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

    protected ItemStackHandler itemHandler;

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if(tag.contains("inventory")) {
            itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if(itemHandler != null) {
            CompoundTag inventory = itemHandler.serializeNBT(registries);
            tag.put("inventory", inventory);
        }
    }

    protected ChunkMion getMionDataInChunk(){
        return ChunkMion.getOrCreateMion(getBlockPos(), getLevel());
    }

    private ChunkAccess getChunk() {
        return getLevel().getChunk(getBlockPos());
    }

    protected void sync(){
        setChanged();

        if(level != null){
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    public ItemStackHandler getInventory() {
        return itemHandler;
    }

    public void dropContents() {
        if(itemHandler == null) return;

        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            items.add(itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(getLevel(), getBlockPos(), new SimpleContainer(items.toArray(new ItemStack[]{})));
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

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }
}
