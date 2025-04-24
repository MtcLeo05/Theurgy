package com.leo.theurgy.impl.recipe.cauldron;

import com.leo.theurgy.api.recipe.BaseResearchGatedRecipe;
import com.leo.theurgy.api.recipe.BaseTheurgistsBenchRecipe;
import com.leo.theurgy.api.recipe.TheurgistsCauldronRecipeInput;
import com.leo.theurgy.api.util.TheurgySerializationUtils;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.init.TheurgyRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
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
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record TheurgistsCauldronRecipe(ItemStack result, ItemStack input, FluidStack fluid, int heat, Map<ResourceLocation, Integer> aspectuses, List<ResourceLocation> research, boolean isHeatStrict) implements Recipe<TheurgistsCauldronRecipeInput>, BaseResearchGatedRecipe {
    @Override
    public List<ResourceLocation> neededResearch() {
        return research;
    }

    @Override
    public boolean matches(TheurgistsCauldronRecipeInput recipeInput, Level level) {
        boolean cont = true;

        for (Map.Entry<ResourceLocation, Integer> recipeEntry : this.aspectuses.entrySet()) {
            if(!cont) return false;

            int recipeValue = recipeEntry.getValue();
            int toTest = recipeInput.aspectuses().getOrDefault(recipeEntry.getKey(), 0);

            if(toTest < recipeValue) cont = false;
        }

        if(!cont) return false;

        if (!fluid.is(recipeInput.fluid().getFluid()) || fluid.getAmount() > recipeInput.fluid().getAmount()) {
            return false;
        }

        if((isHeatStrict && heat != recipeInput.heat()) || (recipeInput.heat() < heat)) {
            return false;
        }

        ItemStack input = recipeInput.item();
        ItemStack test = this.input.copy();

        return BaseTheurgistsBenchRecipe.isSameItemSameComponentsBiggerCount(test, input);
    }

    public static Builder builder(String group, RecipeCategory category) {
        return new Builder(group, category);
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public ItemStack assemble(TheurgistsCauldronRecipeInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TheurgyRecipes.THEURGISTS_CAULDRON_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return TheurgyConstants.THEURGISTS_CAULDRON_RECIPE_TYPE;
    }

    public static class Serializer implements RecipeSerializer<TheurgistsCauldronRecipe> {
        public static final MapCodec<TheurgistsCauldronRecipe> CODEC = RecordCodecBuilder.mapCodec(
            inst -> inst.group(
                ItemStack.CODEC.fieldOf("result").forGetter(TheurgistsCauldronRecipe::result),
                ItemStack.CODEC.fieldOf("input").forGetter(TheurgistsCauldronRecipe::input),
                FluidStack.CODEC.fieldOf("fluid").forGetter(TheurgistsCauldronRecipe::fluid),
                Codec.INT.fieldOf("heat").forGetter(TheurgistsCauldronRecipe::heat),
                Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("aspectuses").forGetter(TheurgistsCauldronRecipe::aspectuses),
                ResourceLocation.CODEC.listOf().fieldOf("research").forGetter(TheurgistsCauldronRecipe::neededResearch),
                Codec.BOOL.optionalFieldOf("isHeatStrict", false).forGetter(TheurgistsCauldronRecipe::isHeatStrict)
            ).apply(inst, TheurgistsCauldronRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, TheurgistsCauldronRecipe> STREAM_CODEC = TheurgySerializationUtils.composite(
            ItemStack.STREAM_CODEC,
            TheurgistsCauldronRecipe::result,
            ItemStack.STREAM_CODEC,
            TheurgistsCauldronRecipe::input,
            FluidStack.STREAM_CODEC,
            TheurgistsCauldronRecipe::fluid,
            ByteBufCodecs.INT,
            TheurgistsCauldronRecipe::heat,
            ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, ByteBufCodecs.INT),
            TheurgistsCauldronRecipe::aspectuses,
            ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)),
            TheurgistsCauldronRecipe::research,
            ByteBufCodecs.BOOL,
            TheurgistsCauldronRecipe::isHeatStrict,
            TheurgistsCauldronRecipe::new
        );

        @Override
        public MapCodec<TheurgistsCauldronRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, TheurgistsCauldronRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public static class Builder implements RecipeBuilder {
        protected String group;
        protected final RecipeCategory category;
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

        protected ItemStack result;
        protected ItemStack input;
        protected Map<ResourceLocation, Integer> aspectuses;
        protected List<ResourceLocation> research;
        protected FluidStack fluid;
        protected int heat;
        protected boolean isHeatStrict = false;

        private Builder(String group, RecipeCategory category) {
            this.group = group;
            this.category = category;
            research = new ArrayList<>();
            aspectuses = new HashMap<>();
        }

        public TheurgistsCauldronRecipe.Builder withResult(ItemStack result) {
            this.result = result;
            return this;
        }

        public TheurgistsCauldronRecipe.Builder withInput(ItemStack input) {
            this.input = input.copy();
            return this;
        }

        public TheurgistsCauldronRecipe.Builder withFluid(FluidStack fluid) {
            this.fluid = fluid.copy();
            return this;
        }

        public TheurgistsCauldronRecipe.Builder withHeat(int heat) {
            this.heat = heat;
            return this;
        }

        public TheurgistsCauldronRecipe.Builder addAspectus(ResourceLocation aspectus, int amount) {
            if(aspectuses.containsKey(aspectus)) return this;

            aspectuses.put(aspectus, amount);
            return this;
        }

        public TheurgistsCauldronRecipe.Builder unlockedBy(String name, Criterion<?> criterion) {
            this.criteria.put(name, criterion);
            return this;
        }

        public TheurgistsCauldronRecipe.Builder addResearch(ResourceLocation research) {
            this.research.add(research);
            return this;
        }

        public Builder withStrictHeat(boolean isHeatStrict) {
            this.isHeatStrict = isHeatStrict;
            return this;
        }

        public TheurgistsCauldronRecipe.Builder group(@Nullable String groupName) {
            this.group = groupName;
            return this;
        }

        @Override
        public Item getResult() {
            return result.getItem();
        }

        @Override
        public void save(RecipeOutput recipeOutput, ResourceLocation id) {
            this.ensureValid(id);
            Advancement.Builder advancement$builder = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);

            this.criteria.forEach(advancement$builder::addCriterion);

            TheurgistsCauldronRecipe recipe = new TheurgistsCauldronRecipe(
                this.result,
                this.input,
                this.fluid,
                this.heat,
                this.aspectuses,
                this.research,
                this.isHeatStrict
            );

            recipeOutput.accept(id, recipe, advancement$builder.build(id.withPrefix("recipes/theurgists_cauldron/" + this.category.getFolderName() + "/")));
        }

        private void ensureValid(ResourceLocation id) {
            if (this.criteria.isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + id);
            }
        }
    }
}
