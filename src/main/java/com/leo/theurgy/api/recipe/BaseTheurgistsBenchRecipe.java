package com.leo.theurgy.api.recipe;

import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.recipe.ShapelessTheurgistsBenchRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.*;

public abstract class BaseTheurgistsBenchRecipe implements Recipe<TheurgistsBenchRecipeInput>, BaseResearchGatedRecipe {
    protected final ItemStack result;
    protected final List<Ingredient> inputs;
    protected final int aer, terra, aqua, ignis, ordo, perditio;
    protected final int mion;
    protected final List<ResourceLocation> research;

    protected BaseTheurgistsBenchRecipe(ItemStack result, List<Ingredient> inputs, int aer, int terra, int aqua, int ignis, int ordo, int perditio, int mion, List<ResourceLocation> research) {
        this.result = result;
        this.inputs = inputs;
        this.aer = aer;
        this.terra = terra;
        this.aqua = aqua;
        this.ignis = ignis;
        this.ordo = ordo;
        this.perditio = perditio;
        this.mion = mion;
        this.research = research;
    }

    public abstract boolean isShapeless();

    public boolean checkAspectuses(List<ItemStack> aspectuses) {
        if((aspectuses.get(0).isEmpty() && ordo > 0) || aspectuses.get(0).getCount() < ordo) return false;
        if((aspectuses.get(1).isEmpty() && aqua > 0) || aspectuses.get(1).getCount() < aqua) return false;
        if((aspectuses.get(2).isEmpty() && aer > 0) || aspectuses.get(2).getCount() < aer) return false;
        if((aspectuses.get(3).isEmpty() && perditio > 0) || aspectuses.get(3).getCount() < perditio) return false;
        if((aspectuses.get(4).isEmpty() && ignis > 0) || aspectuses.get(4).getCount() < ignis) return false;
        return (!aspectuses.get(5).isEmpty() || terra <= 0) && aspectuses.get(5).getCount() >= terra;
    }

    public int[] orderedAspectuses() {
        return new int[] {
            ordo, aqua, aer, perditio, ignis, terra
        };
    }

    public ItemStack result() {
        return result;
    }

    public List<Ingredient> inputs() {
        return inputs;
    }

    public int aer() {
        return aer;
    }

    public int terra() {
        return terra;
    }

    public int aqua() {
        return aqua;
    }

    public int ignis() {
        return ignis;
    }

    public int ordo() {
        return ordo;
    }

    public int perditio() {
        return perditio;
    }

    public int mion() {
        return mion;
    }

    @Override
    public RecipeType<?> getType() {
        return TheurgyConstants.THEURGISTS_BENCH_RECIPE_TYPE;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public ItemStack assemble(TheurgistsBenchRecipeInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    public static boolean isSameItemSameComponentsBiggerCount(ItemStack test, ItemStack item) {
        if (!ItemStack.isSameItemSameComponents(test, item)) return false;

        return test.getCount() >= item.getCount();
    }
}
