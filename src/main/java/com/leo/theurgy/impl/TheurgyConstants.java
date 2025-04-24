package com.leo.theurgy.impl;

import com.leo.theurgy.api.aspectus.mapper.holder.IAspectusMappingHolder;
import com.leo.theurgy.api.data.aspectus.Aspectus;
import com.leo.theurgy.api.client.renderable.JsonGuiRenderable;
import com.leo.theurgy.api.guidebook.data.GuideBookCategory;
import com.leo.theurgy.api.guidebook.data.GuideBookEntry;
import com.leo.theurgy.api.guidebook.data.GuideBookPage;
import com.leo.theurgy.api.recipe.BaseTheurgistsBenchRecipe;
import com.leo.theurgy.api.research.ResearchType;
import com.leo.theurgy.api.util.ListUtil;
import com.leo.theurgy.impl.block.MionGeodeBlock;
import com.leo.theurgy.impl.init.TheurgyBlocks;
import com.leo.theurgy.impl.recipe.cauldron.TheurgistsCauldronHeatRecipe;
import com.leo.theurgy.impl.recipe.cauldron.TheurgistsCauldronRecipe;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

public class TheurgyConstants {

    public static final String MODID = "theurgy";

    public static final ResourceKey<Registry<Aspectus>> ASPECTUS_REGISTRY_KEY = ResourceKey.createRegistryKey(TheurgyConstants.modLoc("aspectus"));
    public static final ResourceKey<Registry<ResearchType>> RESEARCH_REGISTRY_KEY = ResourceKey.createRegistryKey(TheurgyConstants.modLoc("research"));
    public static final ResourceKey<Registry<IAspectusMappingHolder>> ASPECTUS_MAPPING_REGISTRY_KEY = ResourceKey.createRegistryKey(TheurgyConstants.modLoc("aspectus_mapping"));

    public static final ResourceKey<Registry<MapCodec<? extends IAspectusMappingHolder>>> MAPPING_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(TheurgyConstants.modLoc("mapping_types"));

    public static final ResourceKey<Registry<MapCodec<? extends ResearchType>>> RESEARCH_TYPES_REGISTRY_KEY = ResourceKey.createRegistryKey(TheurgyConstants.modLoc("research_types"));

    public static final ResourceKey<Registry<MapCodec<? extends JsonGuiRenderable>>> JSON_GUI_RENDERABLE_TYPES_REGISTRY_KEY = ResourceKey.createRegistryKey(TheurgyConstants.modLoc("json_gui_renderable"));
    public static final ResourceKey<Registry<MapCodec<? extends GuideBookPage>>> GUIDEBOOK_PAGE_RENDERABLE_TYPES_REGISTRY_KEY = ResourceKey.createRegistryKey(TheurgyConstants.modLoc("guidebook_page"));

    public static final RecipeType<BaseTheurgistsBenchRecipe> THEURGISTS_BENCH_RECIPE_TYPE = RecipeType.simple(TheurgyConstants.modLoc("theurgists_bench"));
    public static final RecipeType<TheurgistsCauldronRecipe> THEURGISTS_CAULDRON_RECIPE_TYPE = RecipeType.simple(TheurgyConstants.modLoc("theurgists_cauldron"));
    public static final RecipeType<TheurgistsCauldronHeatRecipe> THEURGISTS_CAULDRON_HEAT_RECIPE_TYPE = RecipeType.simple(TheurgyConstants.modLoc("theurgists_cauldron_heat"));

    public static final ResourceLocation JEI_PLUGIN_ID = modLoc("jei_plugin");
    
    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static ResourceLocation makeGuideBookEntry(ResourceLocation category, ResourceLocation entry) {
        return ResourceLocation.fromNamespaceAndPath(category.getNamespace(), category.getPath() + "/" + entry.getPath());
    }

    public static ResourceLocation makeGuideBookEntry(GuideBookCategory category, GuideBookEntry entry) {
        return TheurgyConstants.makeGuideBookEntry(category.id(), entry.id());
    }

    public static final int MION_COLOR = 0x99f8ff;

    public static ResourceLocation itemName(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }
}
