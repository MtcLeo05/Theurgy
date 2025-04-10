package com.leo.theurgy.impl.event;

import com.leo.theurgy.api.data.aspectus.Aspectus;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.init.TheurgyBlockEntities;
import com.leo.theurgy.impl.init.TheurgyGuideBookPageTypes;
import com.leo.theurgy.impl.init.TheurgyJsonGuiRenderableTypes;
import com.leo.theurgy.impl.init.TheurgyResearchTypes;
import com.leo.theurgy.impl.world.biome.TheurgyTerrablender;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(modid = TheurgyConstants.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusEvents {

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
            TheurgyConstants.ASPECTUS_REGISTRY_KEY,
            Aspectus.CODEC,
            Aspectus.CODEC
        );

        event.dataPackRegistry(
            TheurgyConstants.RESEARCH_REGISTRY_KEY,
            TheurgyResearchTypes.RESEARCH_TYPE_CODEC,
            TheurgyResearchTypes.RESEARCH_TYPE_CODEC
        );
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event){
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            TheurgyBlockEntities.THEURGISTS_BENCH_BE.get(),
            ((o, direction) -> o.getInventory())
        );
    }

    @SubscribeEvent
    public static void registerRegistry(NewRegistryEvent event) {
        event.register(TheurgyResearchTypes.RESEARCH_TYPES_REGISTRY);
        event.register(TheurgyJsonGuiRenderableTypes.JSON_GUI_RENDERABLE_TYPES_REGISTRY);
        event.register(TheurgyGuideBookPageTypes.GUIDEBOOK_PAGE_TYPES_REGISTRY);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        TheurgyTerrablender.registerBiomes();
    }
}
