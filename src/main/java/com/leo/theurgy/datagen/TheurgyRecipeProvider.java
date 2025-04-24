package com.leo.theurgy.datagen;

import com.leo.theurgy.api.data.aspectus.AspectusHolder;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.init.TheurgyBlocks;
import com.leo.theurgy.impl.init.TheurgyDataComponents;
import com.leo.theurgy.impl.init.TheurgyItems;
import com.leo.theurgy.impl.recipe.cauldron.TheurgistsCauldronHeatRecipe;
import com.leo.theurgy.impl.recipe.cauldron.TheurgistsCauldronRecipe;
import com.leo.theurgy.impl.recipe.bench.ShapedTheurgistsBenchRecipe;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.concurrent.CompletableFuture;

public class TheurgyRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public TheurgyRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> provider) {
        super(pOutput, provider);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput, HolderLookup.Provider holderLookup) {
        ItemStack aer = new ItemStack(TheurgyItems.MION_CRYSTAL);
        aer.set(TheurgyDataComponents.ASPECTUS_HOLDER, new AspectusHolder(TheurgyConstants.modLoc("aer"), 1));
        Ingredient aerIng = DataComponentIngredient.of(false, aer);

        ItemStack aqua = new ItemStack(TheurgyItems.MION_CRYSTAL);
        aqua.set(TheurgyDataComponents.ASPECTUS_HOLDER, new AspectusHolder(TheurgyConstants.modLoc("aqua"), 1));
        Ingredient aquaIng = DataComponentIngredient.of(false, aqua);

        ItemStack ignis = new ItemStack(TheurgyItems.MION_CRYSTAL);
        ignis.set(TheurgyDataComponents.ASPECTUS_HOLDER, new AspectusHolder(TheurgyConstants.modLoc("ignis"), 1));
        Ingredient ignisIng = DataComponentIngredient.of(false, ignis);

        ItemStack ordo = new ItemStack(TheurgyItems.MION_CRYSTAL);
        ordo.set(TheurgyDataComponents.ASPECTUS_HOLDER, new AspectusHolder(TheurgyConstants.modLoc("ordo"), 1));
        Ingredient ordoIng = DataComponentIngredient.of(false, ordo);

        ItemStack perditio = new ItemStack(TheurgyItems.MION_CRYSTAL);
        perditio.set(TheurgyDataComponents.ASPECTUS_HOLDER, new AspectusHolder(TheurgyConstants.modLoc("perditio"), 1));
        Ingredient perditioIng = DataComponentIngredient.of(false, perditio);

        ItemStack terra = new ItemStack(TheurgyItems.MION_CRYSTAL);
        terra.set(TheurgyDataComponents.ASPECTUS_HOLDER, new AspectusHolder(TheurgyConstants.modLoc("terra"), 1));
        Ingredient terraIng = DataComponentIngredient.of(false, terra);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TheurgyBlocks.THEURGISTS_BENCH.get(), 1)
            .pattern("SCS")
            .pattern("LPL")
            .pattern("L L")
            .define('C', CompoundIngredient.of(aerIng, aquaIng, ignisIng, ordoIng, perditioIng, terraIng))
            .define('S', TheurgyBlocks.GREATWOOD_SLAB.get())
            .define('L', TheurgyItemTagsProvider.GREATWOOD_LOGS)
            .define('P', TheurgyBlocks.GREATWOOD_PLANKS.get())
            .unlockedBy("hasItem", has(TheurgyItems.MION_CRYSTAL.get()))
            .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TheurgyBlocks.GREATWOOD_WOOD.get(), 3)
            .pattern("LL")
            .pattern("LL")
            .define('L', TheurgyBlocks.GREATWOOD_LOG.get())
            .unlockedBy("hasItem", has(TheurgyBlocks.GREATWOOD_LOG.get()))
            .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TheurgyBlocks.GREATWOOD_STAIRS.get(), 4)
            .pattern("P  ")
            .pattern("PP ")
            .pattern("PPP")
            .define('P', TheurgyBlocks.GREATWOOD_PLANKS.get())
            .unlockedBy("hasItem", has(TheurgyBlocks.GREATWOOD_PLANKS.get()))
            .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TheurgyBlocks.GREATWOOD_SLAB.get(), 6)
            .pattern("PPP")
            .define('P', TheurgyBlocks.GREATWOOD_PLANKS.get())
            .unlockedBy("hasItem", has(TheurgyBlocks.GREATWOOD_PLANKS.get()))
            .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TheurgyBlocks.GREATWOOD_PLANKS.get(), 4)
            .requires(TheurgyItemTagsProvider.GREATWOOD_LOGS)
            .unlockedBy("hasItem", has(TheurgyItemTagsProvider.GREATWOOD_LOGS))
            .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TheurgyBlocks.STRIPPED_GREATWOOD_WOOD.get(), 3)
            .pattern("LL")
            .pattern("LL")
            .define('L', TheurgyBlocks.STRIPPED_GREATWOOD_LOG.get())
            .unlockedBy("hasItem", has(TheurgyBlocks.STRIPPED_GREATWOOD_LOG.get()))
            .save(recipeOutput);

        ShapedTheurgistsBenchRecipe.builder("misc", RecipeCategory.MISC)
            .pattern("NGN")
            .pattern("GTG")
            .pattern("NGN")
            .define('N', Items.GOLD_NUGGET)
            .define('G', Items.GOLD_INGOT)
            .define('T', Blocks.TINTED_GLASS)
            .unlockedBy("hasItem", has(Blocks.TINTED_GLASS))
            .withAspectus(1, 1, 1, 1, 1, 1)
            .withMion(20)
            .withResult(TheurgyItems.CRYSTAL_SCANNER.get().getDefaultInstance())
            .save(recipeOutput, TheurgyConstants.modLoc("theurgists_bench/shaped/crystal_scanner"));

        BlockPredicate cauldron = BlockPredicate.Builder.block()
            .of(Blocks.CAMPFIRE)
            .setProperties(StatePropertiesPredicate.Builder.properties()
                .hasProperty(CampfireBlock.LIT, true))
            .build();

        TheurgistsCauldronHeatRecipe.builder("misc", RecipeCategory.MISC)
            .withBlock(cauldron)
            .withHeat(5)
            .unlockedBy("hasItem", has(Blocks.CAMPFIRE))
            .save(recipeOutput, TheurgyConstants.modLoc("theurgists_cauldron/heat/campfire"));

        TheurgistsCauldronRecipe.builder("misc", RecipeCategory.MISC)
            .withInput(Items.IRON_INGOT.getDefaultInstance())
            .withResult(Items.DIAMOND.getDefaultInstance())
            .addAspectus(TheurgyConstants.modLoc("ignis"), 5)
            .withHeat(3)
            .withStrictHeat(false)
            .withFluid(new FluidStack(Fluids.WATER, 250))
            .unlockedBy("hasItem", has(Items.IRON_INGOT))
            .save(recipeOutput, TheurgyConstants.modLoc("theurgists_cauldron/diamond"));
    }
}
