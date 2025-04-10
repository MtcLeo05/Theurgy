package com.leo.theurgy.impl.integration.jei;

import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.client.gui.screen.TheurgistsBenchScreen;
import com.leo.theurgy.impl.init.TheurgyBlocks;
import com.leo.theurgy.impl.init.TheurgyRecipes;
import com.leo.theurgy.impl.integration.jei.category.TheurgistsBenchCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class TheurgyJEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return TheurgyConstants.JEI_PLUGIN_ID;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(TheurgyBlocks.THEURGISTS_BENCH.get(), RecipeTypes.CRAFTING, TheurgistsBenchCategory.RECIPE_TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(TheurgistsBenchScreen.class, 106, 52, 22, 15, TheurgistsBenchCategory.RECIPE_TYPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();

        registration.addRecipes(TheurgistsBenchCategory.RECIPE_TYPE, gatherRecipes(manager.getAllRecipesFor(TheurgyRecipes.THEURGISTS_BENCH_TYPE.get())));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new TheurgistsBenchCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    private <T extends Recipe<?>> List<T> gatherRecipes(List<RecipeHolder<T>> recipeHolders) {
        List<T> recipes = new ArrayList<>();

        for (RecipeHolder<T> recipeHolder : recipeHolders) {
            recipes.add(recipeHolder.value());
        }

        return recipes;
    }
}
