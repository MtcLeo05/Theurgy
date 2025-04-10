package com.leo.theurgy.datagen;

import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.init.TheurgyBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TheurgyBlockTagsProvider extends BlockTagsProvider {

    public TheurgyBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, TheurgyConstants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        vanillaWoodStuff();

        this.tag(MION_GEODES)
            .add(
                TheurgyBlocks.IGNIS_GEODE.get(),
                TheurgyBlocks.AQUA_GEODE.get(),
                TheurgyBlocks.AER_GEODE.get(),
                TheurgyBlocks.TERRA_GEODE.get(),
                TheurgyBlocks.PERDITIO_GEODE.get(),
                TheurgyBlocks.ORDO_GEODE.get()
            );

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(
                TheurgyBlocks.IGNIS_GEODE.get(),
                TheurgyBlocks.AQUA_GEODE.get(),
                TheurgyBlocks.AER_GEODE.get(),
                TheurgyBlocks.TERRA_GEODE.get(),
                TheurgyBlocks.PERDITIO_GEODE.get(),
                TheurgyBlocks.ORDO_GEODE.get(),

                TheurgyBlocks.IGNIS_ORE.get(),
                TheurgyBlocks.DEEPSLATE_IGNIS_ORE.get(),
                TheurgyBlocks.TERRA_ORE.get(),
                TheurgyBlocks.DEEPSLATE_TERRA_ORE.get(),
                TheurgyBlocks.AQUA_ORE.get(),
                TheurgyBlocks.DEEPSLATE_AQUA_ORE.get(),
                TheurgyBlocks.AER_ORE.get(),
                TheurgyBlocks.DEEPSLATE_AER_ORE.get(),
                TheurgyBlocks.ORDO_ORE.get(),
                TheurgyBlocks.DEEPSLATE_ORDO_ORE.get(),
                TheurgyBlocks.PERDITIO_ORE.get(),
                TheurgyBlocks.DEEPSLATE_PERDITIO_ORE.get()
            );

        this.tag(GREATWOOD_LOGS)
            .add(
                TheurgyBlocks.GREATWOOD_LOG.get(),
                TheurgyBlocks.STRIPPED_GREATWOOD_LOG.get(),
                TheurgyBlocks.GREATWOOD_WOOD.get(),
                TheurgyBlocks.STRIPPED_GREATWOOD_WOOD.get()
            );

        this.tag(BlockTags.NEEDS_STONE_TOOL)
            .add(
                TheurgyBlocks.IGNIS_ORE.get(),
                TheurgyBlocks.DEEPSLATE_IGNIS_ORE.get(),
                TheurgyBlocks.TERRA_ORE.get(),
                TheurgyBlocks.DEEPSLATE_TERRA_ORE.get(),
                TheurgyBlocks.AQUA_ORE.get(),
                TheurgyBlocks.DEEPSLATE_AQUA_ORE.get(),
                TheurgyBlocks.AER_ORE.get(),
                TheurgyBlocks.DEEPSLATE_AER_ORE.get(),
                TheurgyBlocks.ORDO_ORE.get(),
                TheurgyBlocks.DEEPSLATE_ORDO_ORE.get(),
                TheurgyBlocks.PERDITIO_ORE.get(),
                TheurgyBlocks.DEEPSLATE_PERDITIO_ORE.get()
            );
    }

    private void vanillaWoodStuff() {
        this.tag(BlockTags.MINEABLE_WITH_HOE)
            .add(
                TheurgyBlocks.GREATWOOD_LEAVES.get()
            );

        this.tag(BlockTags.MINEABLE_WITH_AXE)
            .add(
                TheurgyBlocks.GREATWOOD_LOG.get(),
                TheurgyBlocks.STRIPPED_GREATWOOD_LOG.get(),
                TheurgyBlocks.GREATWOOD_WOOD.get(),
                TheurgyBlocks.STRIPPED_GREATWOOD_WOOD.get(),
                TheurgyBlocks.GREATWOOD_SAPLING.get(),
                TheurgyBlocks.GREATWOOD_PLANKS.get(),
                TheurgyBlocks.GREATWOOD_STAIRS.get(),
                TheurgyBlocks.GREATWOOD_SLAB.get(),
                TheurgyBlocks.THEURGISTS_BENCH.get()
            );

        this.tag(BlockTags.PLANKS)
            .add(
                TheurgyBlocks.GREATWOOD_PLANKS.get()
            );

        this.tag(BlockTags.STAIRS)
            .add(
                TheurgyBlocks.GREATWOOD_STAIRS.get()
            );

        this.tag(BlockTags.WOODEN_STAIRS)
            .add(
                TheurgyBlocks.GREATWOOD_STAIRS.get()
            );

        this.tag(BlockTags.SLABS)
            .add(
                TheurgyBlocks.GREATWOOD_SLAB.get()
            );

        this.tag(BlockTags.WOODEN_SLABS)
            .add(
                TheurgyBlocks.GREATWOOD_SLAB.get()
            );

        this.tag(BlockTags.SAPLINGS)
            .add(
                TheurgyBlocks.GREATWOOD_SAPLING.get()
            );

        this.tag(BlockTags.LOGS)
            .add(
                TheurgyBlocks.GREATWOOD_LOG.get(),
                TheurgyBlocks.STRIPPED_GREATWOOD_LOG.get(),
                TheurgyBlocks.GREATWOOD_WOOD.get(),
                TheurgyBlocks.STRIPPED_GREATWOOD_WOOD.get()
            );

        this.tag(BlockTags.LOGS_THAT_BURN)
            .add(
                TheurgyBlocks.GREATWOOD_LOG.get(),
                TheurgyBlocks.STRIPPED_GREATWOOD_LOG.get(),
                TheurgyBlocks.GREATWOOD_WOOD.get(),
                TheurgyBlocks.STRIPPED_GREATWOOD_WOOD.get()
            );

        this.tag(BlockTags.OVERWORLD_NATURAL_LOGS)
            .add(
                TheurgyBlocks.GREATWOOD_LOG.get(),
                TheurgyBlocks.GREATWOOD_WOOD.get()
            );

        this.tag(BlockTags.COMPLETES_FIND_TREE_TUTORIAL)
            .add(
                TheurgyBlocks.GREATWOOD_LOG.get(),
                TheurgyBlocks.STRIPPED_GREATWOOD_LOG.get(),
                TheurgyBlocks.GREATWOOD_WOOD.get(),
                TheurgyBlocks.STRIPPED_GREATWOOD_WOOD.get(),
                TheurgyBlocks.GREATWOOD_LEAVES.get()
            );

        this.tag(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE)
            .add(
                TheurgyBlocks.GREATWOOD_LOG.get(),
                TheurgyBlocks.STRIPPED_GREATWOOD_LOG.get(),
                TheurgyBlocks.GREATWOOD_WOOD.get(),
                TheurgyBlocks.STRIPPED_GREATWOOD_WOOD.get(),
                TheurgyBlocks.GREATWOOD_LEAVES.get()
            );

        this.tag(BlockTags.PARROTS_SPAWNABLE_ON)
            .add(
                TheurgyBlocks.GREATWOOD_LEAVES.get()
            );

        this.tag(BlockTags.REPLACEABLE_BY_TREES)
            .add(
                TheurgyBlocks.GREATWOOD_LEAVES.get()
            );

        this.tag(BlockTags.SWORD_EFFICIENT)
            .add(
                TheurgyBlocks.GREATWOOD_LEAVES.get(),
                TheurgyBlocks.GREATWOOD_SAPLING.get()
            );

        this.tag(BlockTags.LEAVES)
            .add(
                TheurgyBlocks.GREATWOOD_LEAVES.get()
            );
    }

    public static TagKey<Block> MION_GEODES = create("mion_geodes");
    public static TagKey<Block> GREATWOOD_LOGS = create("greatwood_logs");

    private static TagKey<Block> create(String pName) {
        return TagKey.create(Registries.BLOCK, TheurgyConstants.modLoc(pName));
    }
}
