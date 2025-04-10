package com.leo.theurgy.impl.world.biome;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.ParameterUtils;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.VanillaParameterOverlayBuilder;

import java.util.function.Consumer;

public class TheurgyOverworldRegion extends Region {

    public TheurgyOverworldRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        VanillaParameterOverlayBuilder builder = new VanillaParameterOverlayBuilder();

        new ParameterUtils.ParameterPointListBuilder()
            .temperature(ParameterUtils.Temperature.NEUTRAL)
            .humidity(ParameterUtils.Humidity.WET)
            .continentalness(ParameterUtils.Continentalness.INLAND)
            .erosion(ParameterUtils.Erosion.EROSION_0, ParameterUtils.Erosion.EROSION_1)
            .depth(ParameterUtils.Depth.FULL_RANGE)
            .weirdness(ParameterUtils.Weirdness.FULL_RANGE)
            .build().forEach(parameterPoint -> builder.add(parameterPoint, TheurgyBiomes.MAGICAL_FOREST));

        builder.build().forEach(mapper);
    }
}
