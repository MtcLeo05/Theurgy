package com.leo.theurgy.impl.init;

import com.leo.theurgy.api.client.renderable.JsonGuiRenderable;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.client.renderable.JsonGuiImageRenderable;
import com.leo.theurgy.impl.client.renderable.JsonGuiItemStackRenderable;
import com.leo.theurgy.impl.client.renderable.JsonGuiMultilineTextRenderable;
import com.leo.theurgy.impl.client.renderable.JsonGuiTextRenderable;
import com.leo.theurgy.impl.client.renderable.button.JsonGuiCompleteEntryButtonRenderable;
import com.leo.theurgy.impl.client.renderable.button.JsonGuiCompleteResearchButtonRenderable;
import com.leo.theurgy.impl.client.renderable.button.JsonGuiChangePageButtonRenderable;
import com.leo.theurgy.impl.client.renderable.conditional.JsonGuiResearchCompleteConditional;
import com.leo.theurgy.impl.client.renderable.recipe.JsonGuiCraftingRecipeRenderable;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Function;

public class TheurgyJsonGuiRenderableTypes {

    public static final Registry<MapCodec<? extends JsonGuiRenderable>> JSON_GUI_RENDERABLE_TYPES_REGISTRY = new RegistryBuilder<>(TheurgyConstants.JSON_GUI_RENDERABLE_TYPES_REGISTRY_KEY)
        .sync(true)
        .defaultKey(TheurgyConstants.modLoc("null_json_gui_renderable"))
        .create();

    public static final DeferredRegister<MapCodec<? extends JsonGuiRenderable>> JSON_GUI_RENDERABLE_TYPES_REGISTER = DeferredRegister.create(JSON_GUI_RENDERABLE_TYPES_REGISTRY, TheurgyConstants.MODID);

    public static final Codec<JsonGuiRenderable> JSON_GUI_RENDERABLE_TYPE_CODEC = JSON_GUI_RENDERABLE_TYPES_REGISTRY.byNameCodec().dispatch(
        JsonGuiRenderable::getCodec,
        Function.identity()
    );

    public static final DeferredHolder<MapCodec<? extends JsonGuiRenderable>, MapCodec<JsonGuiImageRenderable>> IMAGE = JSON_GUI_RENDERABLE_TYPES_REGISTER.register("image",
        () -> JsonGuiImageRenderable.CODEC
    );

    public static final DeferredHolder<MapCodec<? extends JsonGuiRenderable>, MapCodec<JsonGuiItemStackRenderable>> ITEMSTACK = JSON_GUI_RENDERABLE_TYPES_REGISTER.register("itemstack",
        () -> JsonGuiItemStackRenderable.CODEC
    );

    public static final DeferredHolder<MapCodec<? extends JsonGuiRenderable>, MapCodec<JsonGuiTextRenderable>> TEXT = JSON_GUI_RENDERABLE_TYPES_REGISTER.register("text",
        () -> JsonGuiTextRenderable.CODEC
    );

    public static final DeferredHolder<MapCodec<? extends JsonGuiRenderable>, MapCodec<JsonGuiMultilineTextRenderable>> MULTILINE_TEXT = JSON_GUI_RENDERABLE_TYPES_REGISTER.register("multiline_text",
        () -> JsonGuiMultilineTextRenderable.CODEC
    );

    public static final DeferredHolder<MapCodec<? extends JsonGuiRenderable>, MapCodec<JsonGuiCompleteEntryButtonRenderable>> RENDERABLE_BUTTON = JSON_GUI_RENDERABLE_TYPES_REGISTER.register("complete_entry_button",
        () -> JsonGuiCompleteEntryButtonRenderable.CODEC
    );

    public static final DeferredHolder<MapCodec<? extends JsonGuiRenderable>, MapCodec<JsonGuiCompleteResearchButtonRenderable>> RESEARCH_BUTTON = JSON_GUI_RENDERABLE_TYPES_REGISTER.register("complete_research_button",
        () -> JsonGuiCompleteResearchButtonRenderable.CODEC
    );

    public static final DeferredHolder<MapCodec<? extends JsonGuiRenderable>, MapCodec<JsonGuiChangePageButtonRenderable>> PAGE_BUTTON = JSON_GUI_RENDERABLE_TYPES_REGISTER.register("change_page_button",
        () -> JsonGuiChangePageButtonRenderable.CODEC
    );

    public static final DeferredHolder<MapCodec<? extends JsonGuiRenderable>, MapCodec<JsonGuiResearchCompleteConditional>> RESEARCH_CONDITIONAL = JSON_GUI_RENDERABLE_TYPES_REGISTER.register("research_conditional",
        () -> JsonGuiResearchCompleteConditional.CODEC
    );

    public static final DeferredHolder<MapCodec<? extends JsonGuiRenderable>, MapCodec<JsonGuiCraftingRecipeRenderable>> CRAFTING_RECIPE = JSON_GUI_RENDERABLE_TYPES_REGISTER.register("crafting_recipe",
        () -> JsonGuiCraftingRecipeRenderable.CODEC
    );
}
