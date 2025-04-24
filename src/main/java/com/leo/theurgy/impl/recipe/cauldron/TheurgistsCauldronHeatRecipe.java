package com.leo.theurgy.impl.recipe.cauldron;

import com.leo.theurgy.api.recipe.TheurgistsCauldronHeatRecipeInput;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.init.TheurgyRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record TheurgistsCauldronHeatRecipe(BlockPredicate block, int heat) implements Recipe<TheurgistsCauldronHeatRecipeInput> {
    @Override
    public boolean matches(TheurgistsCauldronHeatRecipeInput recipeInput, Level level) {
        BlockState currentBlock = recipeInput.block();

        return this.block.matchesState(currentBlock);
    }

    public static Builder builder(String group, RecipeCategory category) {
        return new Builder(group, category);
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return null;
    }

    @Override
    public ItemStack assemble(TheurgistsCauldronHeatRecipeInput input, HolderLookup.Provider registries) {
        return input.block().getBlock().asItem().getDefaultInstance();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TheurgyRecipes.THEURGISTS_CAULDRON_HEAT_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return TheurgyConstants.THEURGISTS_CAULDRON_HEAT_RECIPE_TYPE;
    }

    public static class Serializer implements RecipeSerializer<TheurgistsCauldronHeatRecipe> {
        public static final MapCodec<TheurgistsCauldronHeatRecipe> CODEC = RecordCodecBuilder.mapCodec(
            inst -> inst.group(
                BlockPredicate.CODEC.fieldOf("block").forGetter(TheurgistsCauldronHeatRecipe::block),
                Codec.INT.fieldOf("heat").forGetter(TheurgistsCauldronHeatRecipe::heat)
            ).apply(inst, TheurgistsCauldronHeatRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, TheurgistsCauldronHeatRecipe> STREAM_CODEC = StreamCodec.composite(
            BlockPredicate.STREAM_CODEC,
            TheurgistsCauldronHeatRecipe::block,
            ByteBufCodecs.INT,
            TheurgistsCauldronHeatRecipe::heat,
            TheurgistsCauldronHeatRecipe::new
        );

        @Override
        public MapCodec<TheurgistsCauldronHeatRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, TheurgistsCauldronHeatRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public static class Builder implements RecipeBuilder {
        protected String group;
        protected final RecipeCategory category;
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

        protected BlockPredicate block;
        protected int heat;

        private Builder(String group, RecipeCategory category) {
            this.group = group;
            this.category = category;
        }

        public TheurgistsCauldronHeatRecipe.Builder withHeat(int heat) {
            this.heat = heat;
            return this;
        }

        public TheurgistsCauldronHeatRecipe.Builder withBlock(BlockPredicate block) {
            this.block = block;
            return this;
        }

        public TheurgistsCauldronHeatRecipe.Builder unlockedBy(String name, Criterion<?> criterion) {
            this.criteria.put(name, criterion);
            return this;
        }

        public TheurgistsCauldronHeatRecipe.Builder group(@Nullable String groupName) {
            this.group = groupName;
            return this;
        }

        @Override
        public Item getResult() {
            return block.blocks().orElseThrow().stream().findFirst().orElseThrow().value().asItem();
        }

        @Override
        public void save(RecipeOutput recipeOutput, ResourceLocation id) {
            this.ensureValid(id);
            Advancement.Builder advancement$builder = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);

            this.criteria.forEach(advancement$builder::addCriterion);


            TheurgistsCauldronHeatRecipe recipe = new TheurgistsCauldronHeatRecipe(
                this.block,
                this.heat
            );

            recipeOutput.accept(id, recipe, advancement$builder.build(id.withPrefix("recipes/theurgists_cauldron/heat/" + this.category.getFolderName() + "/")));
        }

        private void ensureValid(ResourceLocation id) {
            if (this.criteria.isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + id);
            }
        }
    }
}
