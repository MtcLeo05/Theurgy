package com.leo.theurgy.impl.world;

import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.init.TheurgyBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class TheurgyPlacedFeatures {

    public static final ResourceKey<PlacedFeature> GREATWOOD_TREE_PLACED_KEY = registerKey("greatwood_tree");

    public static final ResourceKey<PlacedFeature> IGNIS_ORES_PLACED_KEY = registerKey("ignis_ore");
    public static final ResourceKey<PlacedFeature> TERRA_ORES_PLACED_KEY = registerKey("terra_ore");
    public static final ResourceKey<PlacedFeature> AQUA_ORES_PLACED_KEY = registerKey("aqua_ore");
    public static final ResourceKey<PlacedFeature> AER_ORES_PLACED_KEY = registerKey("aer_ore");
    public static final ResourceKey<PlacedFeature> ORDO_ORES_PLACED_KEY = registerKey("ordo_ore");
    public static final ResourceKey<PlacedFeature> PERDITIO_ORES_PLACED_KEY = registerKey("perditio_ore");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, GREATWOOD_TREE_PLACED_KEY, configuredFeatures.getOrThrow(TheurgyConfiguredFeatures.GREATWOOD_TREE),
            VegetationPlacements.treePlacement(PlacementUtils.countExtra(3, 0.1f, 1),
                TheurgyBlocks.GREATWOOD_SAPLING.get()));

        fastRegisterOre(context, IGNIS_ORES_PLACED_KEY, configuredFeatures, TheurgyConfiguredFeatures.IGNIS_ORE);
        fastRegisterOre(context, TERRA_ORES_PLACED_KEY, configuredFeatures, TheurgyConfiguredFeatures.TERRA_ORE);
        fastRegisterOre(context, AQUA_ORES_PLACED_KEY, configuredFeatures, TheurgyConfiguredFeatures.AQUA_ORE);
        fastRegisterOre(context, AER_ORES_PLACED_KEY, configuredFeatures, TheurgyConfiguredFeatures.AQUA_ORE);
        fastRegisterOre(context, ORDO_ORES_PLACED_KEY, configuredFeatures, TheurgyConfiguredFeatures.ORDO_ORE);
        fastRegisterOre(context, PERDITIO_ORES_PLACED_KEY, configuredFeatures, TheurgyConfiguredFeatures.PERDITIO_ORE);
    }

    private static void fastRegisterOre(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures, ResourceKey<ConfiguredFeature<?, ?>> oreKey) {
        register(context, key, configuredFeatures.getOrThrow(oreKey),
            List.of(
                HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64)),
                CountPlacement.of(UniformInt.of(3, 5))
            )
        );
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, TheurgyConstants.modLoc(name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

}
