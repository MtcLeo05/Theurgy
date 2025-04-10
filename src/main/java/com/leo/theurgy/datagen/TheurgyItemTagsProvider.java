package com.leo.theurgy.datagen;

import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.init.TheurgyBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TheurgyItemTagsProvider extends ItemTagsProvider {
    public TheurgyItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, TheurgyConstants.MODID, existingFileHelper);
    }

    public static TagKey<Item> MION_GEODES = create("mion_geodes");
    public static TagKey<Item> GREATWOOD_LOGS = create("greatwood_logs");

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        this.tag(ItemTags.LOGS)
            .add(
            TheurgyBlocks.GREATWOOD_LOG.get().asItem(),
            TheurgyBlocks.STRIPPED_GREATWOOD_LOG.get().asItem(),
            TheurgyBlocks.GREATWOOD_WOOD.get().asItem(),
            TheurgyBlocks.STRIPPED_GREATWOOD_WOOD.get().asItem()
        );

        this.tag(ItemTags.LOGS_THAT_BURN)
            .add(
                TheurgyBlocks.GREATWOOD_LOG.get().asItem(),
                TheurgyBlocks.STRIPPED_GREATWOOD_LOG.get().asItem(),
                TheurgyBlocks.GREATWOOD_WOOD.get().asItem(),
                TheurgyBlocks.STRIPPED_GREATWOOD_WOOD.get().asItem()
            );

        this.tag(ItemTags.PLANKS)
            .add(
                TheurgyBlocks.GREATWOOD_PLANKS.get().asItem()
            );

        this.tag(GREATWOOD_LOGS)
            .add(
                TheurgyBlocks.GREATWOOD_LOG.get().asItem(),
                TheurgyBlocks.STRIPPED_GREATWOOD_LOG.get().asItem(),
                TheurgyBlocks.GREATWOOD_WOOD.get().asItem(),
                TheurgyBlocks.STRIPPED_GREATWOOD_WOOD.get().asItem()
            );

        this.tag(MION_GEODES)
            .add(
                TheurgyBlocks.IGNIS_GEODE.get().asItem(),
                TheurgyBlocks.AQUA_GEODE.get().asItem(),
                TheurgyBlocks.AER_GEODE.get().asItem(),
                TheurgyBlocks.TERRA_GEODE.get().asItem(),
                TheurgyBlocks.PERDITIO_GEODE.get().asItem(),
                TheurgyBlocks.ORDO_GEODE.get().asItem()
            );
    }

    private static TagKey<Item> create(String pName) {
        return TagKey.create(Registries.ITEM, TheurgyConstants.modLoc(pName));
    }
}
