package com.leo.theurgy.impl.world.biome;

import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.world.TheurgyPlacedFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;

public class TheurgyBiomes {

    public static final ResourceKey<Biome> MAGICAL_FOREST = ResourceKey.create(Registries.BIOME, TheurgyConstants.modLoc("magical_forest"));

    public static void bootstrap(BootstrapContext<Biome> context) {
        context.register(
            MAGICAL_FOREST, ebonyForest(context)
        );
    }

    private static Biome ebonyForest(BootstrapContext<Biome> context) {

        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        spawnBuilder.creatureGenerationProbability(0f);

        BiomeGenerationSettings.Builder biomeBuilder =
            new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        globalOverworldGeneration(biomeBuilder);

        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_PLAINS);
        BiomeDefaultFeatures.addDefaultMushrooms(biomeBuilder);
        BiomeDefaultFeatures.addDefaultExtraVegetation(biomeBuilder);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TheurgyPlacedFeatures.GREATWOOD_TREE_PLACED_KEY);

        return new Biome.BiomeBuilder()
            .hasPrecipitation(false)
            .downfall(0f)
            .temperature(0.2f)
            .generationSettings(biomeBuilder.build())
            .mobSpawnSettings(spawnBuilder.build())
            .specialEffects(
                new BiomeSpecialEffects.Builder()
                    .waterColor(0x7ecafc)
                    .waterFogColor(0x5a7b91)
                    .skyColor(0x309bff)
                    .grassColorOverride(TheurgyConstants.MION_COLOR)
                    .foliageColorOverride(0x56b8bf)
                    .fogColor(0xa1ced1)
                    .build()
            )
            .build();
    }

    public static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultSprings(builder);
        BiomeDefaultFeatures.addSurfaceFreezing(builder);
    }

}
