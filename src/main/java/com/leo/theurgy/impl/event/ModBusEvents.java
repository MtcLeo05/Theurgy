package com.leo.theurgy.impl.event;

import com.leo.theurgy.api.capability.TheurgyCapabilities;
import com.leo.theurgy.api.data.aspectus.Aspectus;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.init.*;
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

        event.dataPackRegistry(
            TheurgyConstants.ASPECTUS_MAPPING_REGISTRY_KEY,
            TheurgyMappingTypes.MAPPING_TYPE_CODEC,
            TheurgyMappingTypes.MAPPING_TYPE_CODEC
        );
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event){
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            TheurgyBlockEntities.THEURGISTS_BENCH_BE.get(),
            ((o, direction) -> o.getInventory())
        );

        event.registerBlockEntity(
            TheurgyCapabilities.ASPECTUS_BLOCK,
            TheurgyBlockEntities.THEURGISTS_CAULDRON_BE.get(),
            ((o, direction) -> o.getAspectusStorage())
        );

        event.registerBlockEntity(
            Capabilities.FluidHandler.BLOCK,
            TheurgyBlockEntities.THEURGISTS_CAULDRON_BE.get(),
            ((o, direction) -> o.getFluidTank())
        );
    }

    @SubscribeEvent
    public static void registerRegistry(NewRegistryEvent event) {
        event.register(TheurgyResearchTypes.RESEARCH_TYPES_REGISTRY);
        event.register(TheurgyJsonGuiRenderableTypes.JSON_GUI_RENDERABLE_TYPES_REGISTRY);
        event.register(TheurgyGuideBookPageTypes.GUIDEBOOK_PAGE_TYPES_REGISTRY);
        event.register(TheurgyMappingTypes.MAPPING_TYPES_REGISTRY);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        TheurgyTerrablender.registerBiomes();
    }
}
