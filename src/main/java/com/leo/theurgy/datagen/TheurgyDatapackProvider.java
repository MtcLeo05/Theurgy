package com.leo.theurgy.datagen;

import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.world.TheurgyBiomeModifiers;
import com.leo.theurgy.impl.world.TheurgyConfiguredFeatures;
import com.leo.theurgy.impl.world.TheurgyPlacedFeatures;
import com.leo.theurgy.impl.world.biome.TheurgyBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class TheurgyDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(Registries.CONFIGURED_FEATURE, TheurgyConfiguredFeatures::bootstrap)
        .add(Registries.PLACED_FEATURE, TheurgyPlacedFeatures::bootstrap)
        .add(Registries.BIOME, TheurgyBiomes::bootstrap)
        .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, TheurgyBiomeModifiers::bootstrap);


    public TheurgyDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of(TheurgyConstants.MODID));
    }
}
