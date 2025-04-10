package com.leo.theurgy.impl.world;

import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.init.TheurgyBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class TheurgyConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> GREATWOOD_TREE = registerKey("greatwood_tree");

    public static final ResourceKey<ConfiguredFeature<?, ?>> IGNIS_ORE = registerKey("ignis_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TERRA_ORE = registerKey("terra_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> AQUA_ORE = registerKey("aqua_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> AER_ORE = registerKey("aer_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORDO_ORE = registerKey("ordo_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PERDITIO_ORE = registerKey("perditio_ore");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        register(
            context,
            GREATWOOD_TREE,
            Feature.TREE,
            new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(TheurgyBlocks.GREATWOOD_LOG.get()),
                new DarkOakTrunkPlacer(6, 4, 2),
                BlockStateProvider.simple(TheurgyBlocks.GREATWOOD_LEAVES.get()),
                new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new TwoLayersFeatureSize(1, 0, 2)
            ).build()
        );

        fastRegisterOre(context, IGNIS_ORE, TheurgyBlocks.IGNIS_ORE.get(), TheurgyBlocks.DEEPSLATE_IGNIS_ORE.get());
        fastRegisterOre(context, TERRA_ORE, TheurgyBlocks.TERRA_ORE.get(), TheurgyBlocks.DEEPSLATE_TERRA_ORE.get());
        fastRegisterOre(context, AQUA_ORE, TheurgyBlocks.AQUA_ORE.get(), TheurgyBlocks.DEEPSLATE_AQUA_ORE.get());
        fastRegisterOre(context, AER_ORE, TheurgyBlocks.AER_ORE.get(), TheurgyBlocks.DEEPSLATE_AER_ORE.get());
        fastRegisterOre(context, ORDO_ORE, TheurgyBlocks.ORDO_ORE.get(), TheurgyBlocks.DEEPSLATE_ORDO_ORE.get());
        fastRegisterOre(context, PERDITIO_ORE, TheurgyBlocks.PERDITIO_ORE.get(), TheurgyBlocks.DEEPSLATE_PERDITIO_ORE.get());
    }

    private static void fastRegisterOre(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, Block stoneOre, Block deepslateOre) {
        RuleTest stoneOres = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateOres = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        register(
            context,
            key,
            Feature.ORE,
            new OreConfiguration(
                List.of(
                    OreConfiguration.target(stoneOres, stoneOre.defaultBlockState()),
                    OreConfiguration.target(deepslateOres, deepslateOre.defaultBlockState())
                ),
                7
            )
        );
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, TheurgyConstants.modLoc(name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }

}
