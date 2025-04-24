package com.leo.theurgy.api.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.state.BlockState;

public record TheurgistsCauldronHeatRecipeInput(BlockState block) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return block.getBlock().asItem().getDefaultInstance();
    }

    @Override
    public int size() {
        return 1;
    }
}
