package com.leo.theurgy.datagen;

import com.leo.theurgy.impl.TheurgyConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = TheurgyConstants.MODID, bus = EventBusSubscriber.Bus.MOD)
public class TheurgyDataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new TheurgyBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new TheurgyItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new TheurgyRecipeProvider(packOutput, provider));

        generator.addProvider(event.includeClient(), TheurgyLootTableProvider.create(packOutput, provider));

        TheurgyBlockTagsProvider blockTagGenerator = new TheurgyBlockTagsProvider(packOutput, provider, existingFileHelper);
        generator.addProvider(event.includeClient(), blockTagGenerator);

        generator.addProvider(event.includeClient(), new TheurgyItemTagsProvider(packOutput, provider, blockTagGenerator.contentsGetter(), existingFileHelper));

        generator.addProvider(event.includeClient(), new TheurgyLanguageProvider(packOutput, "en_us"));

        generator.addProvider(event.includeClient(), new TheurgyDatapackProvider(packOutput, provider));
    }
}
