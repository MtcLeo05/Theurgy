package com.leo.theurgy.impl.init;

import com.leo.theurgy.api.block.RotatedStrippableBlock;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.block.AspectusOreBlock;
import com.leo.theurgy.impl.block.MionGeodeBlock;
import com.leo.theurgy.impl.block.TheurgistsBenchBlock;
import com.leo.theurgy.impl.block.TheurgistsCauldronBlock;
import com.leo.theurgy.impl.client.render.be.TheurgistsCauldronBER;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TheurgyBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, TheurgyConstants.MODID);

    public static DeferredHolder<Block, TheurgistsBenchBlock> THEURGISTS_BENCH = registerBlock("theurgists_bench",
        () -> new TheurgistsBenchBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .sound(SoundType.WOOD)
                .strength(3f)
                .requiresCorrectToolForDrops()
        )
    );

    public static DeferredHolder<Block, TheurgistsCauldronBlock> THEURGISTS_CAULDRON = registerBlock("theurgists_cauldron",
        () -> new TheurgistsCauldronBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .sound(SoundType.METAL)
                .strength(2f)
                .requiresCorrectToolForDrops()
        )
    );

    // region Wood Stuff

    public static DeferredHolder<Block, RotatedPillarBlock> STRIPPED_GREATWOOD_LOG = registerBlock("stripped_greatwood_log", () ->
        new RotatedPillarBlock(
            BlockBehaviour.Properties.of()
                .mapColor(state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.WOOD : MapColor.PODZOL)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava()
        )
    );

    public static DeferredHolder<Block, RotatedPillarBlock> STRIPPED_GREATWOOD_WOOD = registerBlock("stripped_greatwood_wood", () ->
        new RotatedPillarBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava()
        )
    );

    public static DeferredHolder<Block, RotatedStrippableBlock> GREATWOOD_LOG = registerBlock("greatwood_log", () ->
        new RotatedStrippableBlock(
            BlockBehaviour.Properties.of()
                .mapColor(state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.WOOD : MapColor.PODZOL)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava(),
            TheurgyBlocks.STRIPPED_GREATWOOD_LOG.get()
        )
    );

    public static DeferredHolder<Block, RotatedStrippableBlock> GREATWOOD_WOOD = registerBlock("greatwood_wood", () ->
        new RotatedStrippableBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava(),
            TheurgyBlocks.STRIPPED_GREATWOOD_WOOD.get()
        )
    );

    public static DeferredHolder<Block, LeavesBlock> GREATWOOD_LEAVES = registerBlock("greatwood_leaves", () ->
        new LeavesBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .strength(0.2F)
                .randomTicks()
                .sound(SoundType.GRASS)
                .noOcclusion()
                .isValidSpawn((state, level, pos, entity) -> entity == EntityType.OCELOT || entity == EntityType.PARROT)
                .isSuffocating((state, level, pos) -> false)
                .isViewBlocking((state, level, pos) -> false)
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY)
                .isRedstoneConductor((state, level, pos) -> false)
        )
    );

    public static DeferredHolder<Block, SaplingBlock> GREATWOOD_SAPLING = registerBlock("greatwood_sapling", () ->
        new SaplingBlock(
            TheurgyTrees.GREATWOOD_TREE_GROWER,
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY)
        )
    );

    public static DeferredHolder<Block, Block> GREATWOOD_PLANKS = registerBlock("greatwood_planks", () ->
        new Block(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_BROWN)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F, 3.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava()
        )
    );

    public static DeferredHolder<Block, StairBlock> GREATWOOD_STAIRS = registerBlock("greatwood_stairs", () ->
        new StairBlock(
            GREATWOOD_PLANKS.get().defaultBlockState(),
            BlockBehaviour.Properties.ofFullCopy(GREATWOOD_PLANKS.get())
        )
    );

    public static DeferredHolder<Block, SlabBlock> GREATWOOD_SLAB = registerBlock("greatwood_slab", () ->
        new SlabBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F, 3.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava()
        )
    );

    // endregion

    // region Worldgen Stuff
    public static DeferredHolder<Block, MionGeodeBlock> IGNIS_GEODE = makeGeode("ignis");
    public static DeferredHolder<Block, MionGeodeBlock> AQUA_GEODE = makeGeode("aqua");
    public static DeferredHolder<Block, MionGeodeBlock> TERRA_GEODE = makeGeode("terra");
    public static DeferredHolder<Block, MionGeodeBlock> AER_GEODE = makeGeode("aer");
    public static DeferredHolder<Block, MionGeodeBlock> ORDO_GEODE = makeGeode("ordo");
    public static DeferredHolder<Block, MionGeodeBlock> PERDITIO_GEODE = makeGeode("perditio");

    public static DeferredHolder<Block, AspectusOreBlock> IGNIS_ORE = makeOre("ignis", IGNIS_GEODE, false);
    public static DeferredHolder<Block, AspectusOreBlock> DEEPSLATE_IGNIS_ORE = makeOre("ignis", IGNIS_GEODE, true);
    public static DeferredHolder<Block, AspectusOreBlock> AQUA_ORE = makeOre("aqua", AQUA_GEODE, false);
    public static DeferredHolder<Block, AspectusOreBlock> DEEPSLATE_AQUA_ORE = makeOre("aqua", AQUA_GEODE, true);
    public static DeferredHolder<Block, AspectusOreBlock> TERRA_ORE = makeOre("terra", TERRA_GEODE, false);
    public static DeferredHolder<Block, AspectusOreBlock> DEEPSLATE_TERRA_ORE = makeOre("terra", TERRA_GEODE, true);
    public static DeferredHolder<Block, AspectusOreBlock> AER_ORE = makeOre("aer", AER_GEODE, false);
    public static DeferredHolder<Block, AspectusOreBlock> DEEPSLATE_AER_ORE = makeOre("aer", AER_GEODE, true);
    public static DeferredHolder<Block, AspectusOreBlock> ORDO_ORE = makeOre("ordo", ORDO_GEODE, false);
    public static DeferredHolder<Block, AspectusOreBlock> DEEPSLATE_ORDO_ORE = makeOre("ordo", ORDO_GEODE, true);
    public static DeferredHolder<Block, AspectusOreBlock> PERDITIO_ORE = makeOre("perditio", PERDITIO_GEODE, false);
    public static DeferredHolder<Block, AspectusOreBlock> DEEPSLATE_PERDITIO_ORE = makeOre("perditio", PERDITIO_GEODE, true);

    // endregion

    // region Methods

    public static DeferredHolder<Block, MionGeodeBlock> makeGeode(String aspectus) {
        return registerBlock(aspectus + "_geode", () ->
            new MionGeodeBlock(
                BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)
                    .sound(SoundType.AMETHYST)
                    .strength(0.25f)
                    .lightLevel((s) -> 1)
                    .requiresCorrectToolForDrops()
            )
        );
    }

    public static DeferredHolder<Block, AspectusOreBlock> makeOre(String aspectus, DeferredHolder<Block, MionGeodeBlock> crystal, boolean isDeepslate) {
        return registerBlock((isDeepslate? "deepslate_": "") + aspectus + "_ore",
            () -> new AspectusOreBlock(
                BlockBehaviour.Properties.ofFullCopy(isDeepslate? Blocks.DEEPSLATE: Blocks.STONE),
                crystal
            )
        );
    }

    public static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Supplier<T> block) {
        DeferredHolder<Block, T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    public static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Supplier<T> block, Rarity rarity) {
        DeferredHolder<Block, T> toReturn = registerBlock(name, block);
        registerBlockItem(name, toReturn, rarity);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredHolder<Block, T> block) {
        TheurgyItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredHolder<Block, T> block, Rarity rarity) {
        TheurgyItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().rarity(rarity)));
    }

    // endregion
}
