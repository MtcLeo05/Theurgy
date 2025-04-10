package com.leo.theurgy.api.guidebook.data;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GuideBookReloadListener extends SimpleJsonResourceReloadListener {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final Gson GSON_INSTANCE = new Gson();
    private static final String folder = "guidebook";
    private static GuideBookReloadListener INSTANCE;
    private BiMap<ResourceLocation, GuideBookCategory> registeredGuideBookCategories = HashBiMap.create();

    private GuideBookReloadListener() {
        super(GSON_INSTANCE, folder);
    }

    public static GuideBookReloadListener getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GuideBookReloadListener();
        }
        return INSTANCE;
    }

    public BiMap<ResourceLocation, GuideBookCategory> getRegisteredGuideBookCategories() {
        return registeredGuideBookCategories;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceList, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        BiMap<ResourceLocation, GuideBookCategory> partialMap = HashBiMap.create();

        for (ResourceLocation location : resourceList.keySet()) {
            JsonElement json = resourceList.get(location);
            GuideBookCategory.CODEC.parse(JsonOps.INSTANCE, json)
                // log error if parse fails
                .resultOrPartial(errorMsg -> LOGGER.error("Could not decode guidebook category with json id {} - error: {}", location, errorMsg))
                // add quest if parse succeeds
                .ifPresent(research -> {
                    partialMap.put(location, research);
                });
        }

        registeredGuideBookCategories = HashBiMap.create();
        Comparator<GuideBookCategory> comparator = Comparator.comparingInt(GuideBookCategory::order);

        List<Map.Entry<ResourceLocation, GuideBookCategory>> sortedNoDoubles = partialMap
            .entrySet()
            .stream()
            .sorted((a, b) -> comparator.compare(a.getValue(), b.getValue()))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Function.identity(),
                (existing, replacement) -> {
                    LOGGER.error("Found more than one guidebook category with id: {}", existing.getKey());
                    return existing;
                }
            )).values().stream()
            .toList();

        sortedNoDoubles.forEach(e -> registeredGuideBookCategories.put(e.getKey(), e.getValue()));
    }

}
