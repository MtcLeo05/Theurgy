package com.leo.theurgy.api.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.Map;

public record TheurgistsCauldronRecipeInput(ItemStack item, FluidStack fluid, int heat, Map<ResourceLocation, Integer> aspectuses) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return item;
    }

    @Override
    public int size() {
        return 1;
    }
}
