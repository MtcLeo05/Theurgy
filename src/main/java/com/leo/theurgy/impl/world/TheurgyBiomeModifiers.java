package com.leo.theurgy.impl.world;

import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.world.biome.TheurgyBiomes;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class TheurgyBiomeModifiers {

    public static ResourceKey<BiomeModifier> GREATWOOD_FOREST_GEN = registerKey("greatwood_forest_gen");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        context.register(
            GREATWOOD_FOREST_GEN,
            new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(
                    biomes.getOrThrow(TheurgyBiomes.MAGICAL_FOREST)
                ),
                HolderSet.direct(
                    placedFeatures.getOrThrow(TheurgyPlacedFeatures.IGNIS_ORES_PLACED_KEY),
                    placedFeatures.getOrThrow(TheurgyPlacedFeatures.TERRA_ORES_PLACED_KEY),
                    placedFeatures.getOrThrow(TheurgyPlacedFeatures.AQUA_ORES_PLACED_KEY),
                    placedFeatures.getOrThrow(TheurgyPlacedFeatures.AER_ORES_PLACED_KEY),
                    placedFeatures.getOrThrow(TheurgyPlacedFeatures.ORDO_ORES_PLACED_KEY),
                    placedFeatures.getOrThrow(TheurgyPlacedFeatures.PERDITIO_ORES_PLACED_KEY)
                ),
                GenerationStep.Decoration.UNDERGROUND_ORES
            )
        );
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, TheurgyConstants.modLoc(name));
    }

}
