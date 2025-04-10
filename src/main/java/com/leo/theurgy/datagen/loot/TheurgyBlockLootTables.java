package com.leo.theurgy.datagen.loot;

import com.leo.theurgy.api.data.aspectus.AspectusHolder;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.init.TheurgyBlocks;
import com.leo.theurgy.impl.init.TheurgyDataComponents;
import com.leo.theurgy.impl.init.TheurgyItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class TheurgyBlockLootTables extends BlockLootSubProvider {
    public TheurgyBlockLootTables(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    static List<Block> toIgnore = List.of(
        TheurgyBlocks.IGNIS_GEODE.get(),
        TheurgyBlocks.AER_GEODE.get(),
        TheurgyBlocks.AQUA_GEODE.get(),
        TheurgyBlocks.TERRA_GEODE.get(),
        TheurgyBlocks.PERDITIO_GEODE.get(),
        TheurgyBlocks.ORDO_GEODE.get(),
        TheurgyBlocks.GREATWOOD_LEAVES.get()
    );

    static List<Block> brokenBlocks = List.of(
        TheurgyBlocks.GREATWOOD_LEAVES.get()
    );

    @Override
    protected void generate() {
        TheurgyBlocks.BLOCKS.getEntries().stream()
            .map(DeferredHolder::get)
            .filter(b -> !toIgnore.contains(b))
            .forEach(this::dropSelf);

        this.add(TheurgyBlocks.IGNIS_GEODE.get(), mionCrystal("ignis"));
        this.add(TheurgyBlocks.AER_GEODE.get(), mionCrystal("aer"));
        this.add(TheurgyBlocks.AQUA_GEODE.get(), mionCrystal("aqua"));
        this.add(TheurgyBlocks.TERRA_GEODE.get(), mionCrystal("terra"));
        this.add(TheurgyBlocks.PERDITIO_GEODE.get(), mionCrystal("perditio"));
        this.add(TheurgyBlocks.ORDO_GEODE.get(), mionCrystal("ordo"));

        this.add(TheurgyBlocks.IGNIS_ORE.get(), aspectusOre("ignis"));
        this.add(TheurgyBlocks.DEEPSLATE_IGNIS_ORE.get(), aspectusOre("ignis"));
        this.add(TheurgyBlocks.AQUA_ORE.get(), aspectusOre("aqua"));
        this.add(TheurgyBlocks.DEEPSLATE_AQUA_ORE.get(), aspectusOre("aqua"));
        this.add(TheurgyBlocks.TERRA_ORE.get(), aspectusOre("terra"));
        this.add(TheurgyBlocks.DEEPSLATE_TERRA_ORE.get(), aspectusOre("terra"));
        this.add(TheurgyBlocks.AER_ORE.get(), aspectusOre("aer"));
        this.add(TheurgyBlocks.DEEPSLATE_AER_ORE.get(), aspectusOre("aer"));
        this.add(TheurgyBlocks.ORDO_ORE.get(), aspectusOre("ordo"));
        this.add(TheurgyBlocks.DEEPSLATE_ORDO_ORE.get(), aspectusOre("ordo"));
        this.add(TheurgyBlocks.PERDITIO_ORE.get(), aspectusOre("perditio"));
        this.add(TheurgyBlocks.DEEPSLATE_PERDITIO_ORE.get(), aspectusOre("perditio"));

    }


    private LootTable.Builder mionCrystal(String aspectus) {
        return LootTable.lootTable()
            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
            .apply(SetComponentsFunction.setComponent(TheurgyDataComponents.ASPECTUS_HOLDER.get(), new AspectusHolder(TheurgyConstants.modLoc(aspectus), 1)))
            .withPool(
                LootPool.lootPool()
                    .when(ExplosionCondition.survivesExplosion())
                    .setRolls(UniformGenerator.between(1, 2))
                    .add(LootItem.lootTableItem(TheurgyItems.MION_CRYSTAL.get()))
            );
    }

    private LootTable.Builder aspectusOre(String aspectus) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        return LootTable.lootTable()
            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
            .apply(SetComponentsFunction.setComponent(TheurgyDataComponents.ASPECTUS_HOLDER.get(), new AspectusHolder(TheurgyConstants.modLoc(aspectus), 1)))
            .withPool(
                LootPool.lootPool()
                    .when(ExplosionCondition.survivesExplosion())
                    .setRolls(UniformGenerator.between(3, 7))
                    .add(LootItem.lootTableItem(TheurgyItems.MION_CRYSTAL.get()))
                    .apply(ApplyBonusCount.addUniformBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))
            );
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        Stream<Block> blocks = TheurgyBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get);
        return blocks.filter(b -> !brokenBlocks.contains(b)).toList();
    }

}