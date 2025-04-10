package com.leo.theurgy.api.client.renderable.recipe;

import com.leo.theurgy.api.client.renderable.JsonGuiRenderable;
import net.minecraft.world.item.crafting.RecipeHolder;

public interface JsonGuiRecipeRenderable extends JsonGuiRenderable {

    RecipeHolder<?> recipe();

}
