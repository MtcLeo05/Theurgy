package com.leo.theurgy.impl.capability.aspectus;

import com.leo.theurgy.api.capability.aspectus.IAspectusStorage;
import com.leo.theurgy.api.data.aspectus.Aspectus;
import com.leo.theurgy.impl.Theurgy;
import com.leo.theurgy.impl.TheurgyConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class CompoundAspectusStorage implements IAspectusStorage {

    private int maxAspectus;
    private Map<ResourceLocation, Integer> aspectuses;

    public CompoundAspectusStorage(int maxAspectus) {
        this.maxAspectus = maxAspectus;
        this.aspectuses = new HashMap<>();
    }

    public boolean addAspectus(Level level, Aspectus aspectus, int amount, boolean simulate) {
        if(amount > maxAspectus() && maxAspectus != -1) return false;

        ResourceLocation key = level.registryAccess().registryOrThrow(TheurgyConstants.ASPECTUS_REGISTRY_KEY).getKey(aspectus);
        if(!this.aspectuses.containsKey(key)) return amount > maxAspectus();

        return addAspectus(key, amount, simulate);
    }

    public boolean addAspectus(ResourceLocation key, int amount, boolean simulate) {
        if(amount > maxAspectus() && maxAspectus != -1) return false;

        if(!this.aspectuses.containsKey(key)) {
            aspectuses.put(key, amount);
            return true;
        }

        int current = currentAspectus(key);

        int toPut = current + amount;

        if(maxAspectus != -1) toPut = Math.min(toPut, maxAspectus);

        if(!simulate) aspectuses.put(key, toPut);
        return false;
    }

    public boolean removeAspectus(Level level, Aspectus aspectus, int amount, boolean simulate) {
        int current = currentAspectus(aspectus, level);

        if(current <= 0) return false;
        if(current - amount <= 0) return false;

        ResourceLocation key = level.registryAccess().registryOrThrow(TheurgyConstants.ASPECTUS_REGISTRY_KEY).getKey(aspectus);
        return removeAspectus(key, amount, simulate);
    }

    public boolean removeAspectus(ResourceLocation key, int amount, boolean simulate) {
        int current = currentAspectus(key);

        if(current <= 0) return false;
        if(current - amount <= 0) return false;

        if(!simulate) aspectuses.put(key, Math.max(current - amount, 0));

        return true;
    }

    public int currentAspectus(ResourceLocation key) {
        return aspectuses.getOrDefault(key, 0);
    }

    public int currentAspectus(Aspectus aspectus, Level level) {
        ResourceLocation key = level.registryAccess().registryOrThrow(TheurgyConstants.ASPECTUS_REGISTRY_KEY).getKey(aspectus);

        return aspectuses.getOrDefault(key, 0);
    }

    @Override
    public int maxAspectus() {
        return maxAspectus;
    }

    @Override
    public int currentAspectus() {
        Theurgy.LOGGER.error("Use the Aspectus specific currentAspectus!");
        return 0;
    }

    @Override
    public boolean addAspectus(int amount, boolean simulate) {
        Theurgy.LOGGER.error("Use the Aspectus specific addAspectus!");
        return false;
    }

    @Override
    public boolean removeAspectus(int amount, boolean simulate) {
        Theurgy.LOGGER.error("Use the Aspectus specific removeAspectus!");
        return false;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();

        tag.putInt("maxAspectus", maxAspectus);

        CompoundTag aspectuses = new CompoundTag();

        this.aspectuses.forEach((key, value) -> aspectuses.putInt(key.toString(), value));

        tag.put("aspectuses", aspectuses);

        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        maxAspectus = tag.getInt("maxAspectus");

        if(aspectuses == null) aspectuses = new HashMap<>();

        if(!tag.contains("aspectuses")) return;

        CompoundTag aspectuses = tag.getCompound("aspectuses");

        for (String key : aspectuses.getAllKeys()) {
            int value = aspectuses.getInt(key);
            this.aspectuses.put(ResourceLocation.parse(key), value);
        }
    }

    public Map<ResourceLocation, Integer> allAspectuses() {
        return this.aspectuses;
    }
}
