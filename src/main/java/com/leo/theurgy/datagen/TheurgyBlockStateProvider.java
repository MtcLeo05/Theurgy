package com.leo.theurgy.datagen;

import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.init.TheurgyBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

public class TheurgyBlockStateProvider extends BlockStateProvider {

    ExistingFileHelper existingFileHelper;

    public TheurgyBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, TheurgyConstants.MODID, exFileHelper);
        this.existingFileHelper = exFileHelper;
    }

    @Override
    protected void registerStatesAndModels() {
        simpleHorizontalBlockWithItem(TheurgyBlocks.THEURGISTS_BENCH);
        simpleBlockWithItem(TheurgyBlocks.THEURGISTS_CAULDRON);

        logBlock(TheurgyBlocks.GREATWOOD_LOG.get());
        simpleBlockItem(TheurgyBlocks.GREATWOOD_LOG);

        simpleCubeAllWithItem(TheurgyBlocks.GREATWOOD_WOOD, "greatwood_log");

        logBlock(TheurgyBlocks.STRIPPED_GREATWOOD_LOG.get());
        simpleBlockItem(TheurgyBlocks.STRIPPED_GREATWOOD_LOG);

        simpleCubeAllWithItem(TheurgyBlocks.STRIPPED_GREATWOOD_WOOD, "stripped_greatwood_log");

        simpleCubeAllWithItem(TheurgyBlocks.GREATWOOD_PLANKS);

        simpleCubeAllWithItem(TheurgyBlocks.IGNIS_ORE);
        simpleCubeAllWithItem(TheurgyBlocks.DEEPSLATE_IGNIS_ORE);
        simpleCubeAllWithItem(TheurgyBlocks.AQUA_ORE);
        simpleCubeAllWithItem(TheurgyBlocks.DEEPSLATE_AQUA_ORE);
        simpleCubeAllWithItem(TheurgyBlocks.TERRA_ORE);
        simpleCubeAllWithItem(TheurgyBlocks.DEEPSLATE_TERRA_ORE);
        simpleCubeAllWithItem(TheurgyBlocks.AER_ORE);
        simpleCubeAllWithItem(TheurgyBlocks.DEEPSLATE_AER_ORE);
        simpleCubeAllWithItem(TheurgyBlocks.ORDO_ORE);
        simpleCubeAllWithItem(TheurgyBlocks.DEEPSLATE_ORDO_ORE);
        simpleCubeAllWithItem(TheurgyBlocks.PERDITIO_ORE);
        simpleCubeAllWithItem(TheurgyBlocks.DEEPSLATE_PERDITIO_ORE);

        stairsWithItem(TheurgyBlocks.GREATWOOD_STAIRS, TheurgyBlocks.GREATWOOD_PLANKS);
        slabWithItem(TheurgyBlocks.GREATWOOD_SLAB, TheurgyBlocks.GREATWOOD_PLANKS);

        simpleBlockWithItem(TheurgyBlocks.GREATWOOD_LEAVES.get(), model(TheurgyBlocks.GREATWOOD_LEAVES));

        saplingBlock(TheurgyBlocks.GREATWOOD_SAPLING);

        directionalBlockWithItem(TheurgyBlocks.IGNIS_GEODE);
        directionalBlockWithItem(TheurgyBlocks.AER_GEODE);
        directionalBlockWithItem(TheurgyBlocks.AQUA_GEODE);
        directionalBlockWithItem(TheurgyBlocks.TERRA_GEODE);
        directionalBlockWithItem(TheurgyBlocks.ORDO_GEODE);
        directionalBlockWithItem(TheurgyBlocks.PERDITIO_GEODE);
    }

    private void stairsWithItem(DeferredHolder<Block, StairBlock> block, DeferredHolder<Block, Block> baseBlock) {
        ResourceLocation baseLocation = texture(baseBlock);
        stairsBlock(block.get(), baseLocation);
        simpleBlockItem(block.get(), model(texture(block)));
    }

    private void saplingBlock(DeferredHolder<Block, ? extends Block> block){
        simpleBlock(block.get(),
            models().cross(
                key(block.get()).getPath(),
                blockTexture(block.get())
            ).renderType("cutout")
        );
    }

    private void slabWithItem(DeferredHolder<Block, SlabBlock> block, DeferredHolder<Block, Block> baseBlock) {
        ResourceLocation baseLocation = texture(baseBlock);
        slabBlock(block.get(), baseLocation, baseLocation);
        simpleBlockItem(block.get(), model(texture(block)));
    }

    private void simpleBlockWithItem(DeferredHolder<Block, ? extends Block> block) {
        simpleBlockWithItem(block.get(), new ModelFile.UncheckedModelFile(TheurgyConstants.modLoc("block/" + block.getId().getPath())));
    }

    private void simpleHorizontalBlock(DeferredHolder<Block, ? extends Block> block) {
        horizontalBlock(block.get(), new ModelFile.UncheckedModelFile(TheurgyConstants.modLoc("block/" + block.getId().getPath())));
    }

    private void simpleHorizontalBlockWithItem(DeferredHolder<Block, ? extends Block> block) {
        ModelFile.UncheckedModelFile model = new ModelFile.UncheckedModelFile(TheurgyConstants.modLoc("block/" + block.getId().getPath()));

        horizontalBlock(block.get(), model);
        simpleBlockItem(block);
    }

    private void simpleBlockWithItem(DeferredHolder<Block, ? extends Block> block, String name) {
        simpleBlockWithItem(block.get(), new ModelFile.UncheckedModelFile(TheurgyConstants.modLoc("block/" + name)));
    }

    private void simpleBlockItem(DeferredHolder<Block, ? extends Block> block) {
        simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(TheurgyConstants.modLoc("block/" + block.getId().getPath())));
    }

    private void simpleCubeAllWithItem(DeferredHolder<Block, ? extends Block> block, String name) {
        simpleBlockWithItem(block.get(), models().cubeAll(name(block.get()), TheurgyConstants.modLoc("block/" + name)));
    }

    private void simpleCubeAllWithItem(DeferredHolder<Block, ? extends Block> block) {
        simpleCubeAllWithItem(block, block.getId().getPath());
    }

    private void directionalBlockWithItem(DeferredHolder<Block, ? extends Block> block) {
        ModelFile model = model(block);

        this.getVariantBuilder(block.get())
            .partialState()
            .with(DirectionalBlock.FACING, Direction.UP)
            .modelForState()
            .modelFile(model)
            .addModel()

            .partialState()
            .with(DirectionalBlock.FACING, Direction.DOWN)
            .modelForState()
            .modelFile(model)
            .rotationX(180)
            .addModel()

            .partialState()
            .with(DirectionalBlock.FACING, Direction.NORTH)
            .modelForState()
            .modelFile(model)
            .rotationY(0)
            .rotationX(90)
            .addModel()

            .partialState()
            .with(DirectionalBlock.FACING, Direction.EAST)
            .modelForState()
            .modelFile(model)
            .rotationY(90)
            .rotationX(90)
            .addModel()

            .partialState()
            .with(DirectionalBlock.FACING, Direction.SOUTH)
            .modelForState()
            .modelFile(model)
            .rotationY(180)
            .rotationX(90)
            .addModel()

            .partialState()
            .with(DirectionalBlock.FACING, Direction.WEST)
            .modelForState()
            .modelFile(model)
            .rotationY(270)
            .rotationX(90)
            .addModel();

        simpleBlockItem(block.get(), model);
    }

    private static ModelFile model(ResourceLocation model) {
        return new ModelFile.UncheckedModelFile(model);
    }

    private static ResourceLocation texture(DeferredHolder<Block, ? extends Block> block) {
        return texture(block.getId().getPath());
    }

    private static ResourceLocation texture(String name) {
        return TheurgyConstants.modLoc("block/" + name);
    }

    private static ModelFile model(DeferredHolder<Block, ? extends Block> block) {
        return model(texture(block));
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    private ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    private static ResourceLocation stripSetName(ResourceLocation name) {
        int index = name.getPath().indexOf('_');

        if (index == -1) {
            return name;
        }

        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), name.getPath().substring(index + 1));
    }
}
