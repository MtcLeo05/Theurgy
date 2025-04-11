package com.leo.theurgy.datagen;

import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.init.TheurgyBlocks;
import com.leo.theurgy.impl.init.TheurgyItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

public class TheurgyItemModelProvider extends ItemModelProvider {
    public TheurgyItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TheurgyConstants.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(TheurgyItems.MION_CRYSTAL);
        simpleItem(TheurgyItems.GUIDE_BOOK);
        simpleItem(TheurgyItems.CRYSTAL_SCANNER);

        simpleBlockItem(TheurgyBlocks.GREATWOOD_SAPLING);
    }

    private void simpleItem(DeferredHolder<Item, ? extends Item> item) {
        simpleItem(item.getId().getPath());
    }

    private void simpleBlockItem(DeferredHolder<Block, ? extends Block> block) {
        simpleBlockItem(block.getId().getPath());
    }

    private void simpleBlockItem(String name) {
        withExistingParent(name, ResourceLocation.withDefaultNamespace("item/generated"))
            .texture("layer0", TheurgyConstants.modLoc("block/" + name));
    }

    private void simpleItem(String name) {
        withExistingParent(name, ResourceLocation.withDefaultNamespace("item/generated"))
            .texture("layer0", TheurgyConstants.modLoc("item/" + name));
    }
}
