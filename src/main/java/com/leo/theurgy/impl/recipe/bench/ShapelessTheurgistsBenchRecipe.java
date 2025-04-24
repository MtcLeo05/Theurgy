package com.leo.theurgy.impl.recipe.bench;

import com.leo.theurgy.api.recipe.BaseTheurgistsBenchRecipe;
import com.leo.theurgy.api.recipe.TheurgistsBenchRecipeInput;
import com.leo.theurgy.api.util.TheurgySerializationUtils;
import com.leo.theurgy.impl.init.TheurgyRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ShapelessTheurgistsBenchRecipe extends BaseTheurgistsBenchRecipe {
    public ShapelessTheurgistsBenchRecipe(ItemStack result, List<Ingredient> inputs, int aer, int terra, int aqua, int ignis, int ordo, int perditio, int mion, List<ResourceLocation> research) {
        super(result, NonNullList.copyOf(inputs), aer, terra, aqua, ignis, ordo, perditio, mion, research);
    }

    @Override
    public boolean isShapeless() {
        return true;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= inputs.size();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TheurgyRecipes.SHAPELESS_THEURGISTS_BENCH_SERIALIZER.get();
    }

    public List<ResourceLocation> getResearch() {
        return this.neededResearch();
    }

    @Override
    public boolean matches(TheurgistsBenchRecipeInput input, Level level) {
        List<ItemStack> inputItems = input.items().stream().filter(i -> !i.isEmpty()).toList();
        List<Ingredient> testInputs = new ArrayList<>(this.inputs());

        if(inputItems.size() != testInputs.size()) return false;

        List<Ingredient> mutableTests = new ArrayList<>(testInputs);

        return inputItems.stream().allMatch(iItem -> {
            Iterator<Ingredient> iterator = mutableTests.iterator();
            while (iterator.hasNext()) {
                Ingredient test = iterator.next();
                if(test.test(iItem)) {
                    iterator.remove();
                    return true;
                }
            }
            return false;
        });
    }

    public static class Serializer implements RecipeSerializer<ShapelessTheurgistsBenchRecipe> {
        public static final MapCodec<ShapelessTheurgistsBenchRecipe> CODEC = RecordCodecBuilder.mapCodec(
            inst -> inst.group(
                ItemStack.CODEC.fieldOf("result").forGetter(ShapelessTheurgistsBenchRecipe::result),
                Ingredient.CODEC.listOf().fieldOf("inputs").forGetter(ShapelessTheurgistsBenchRecipe::inputs),
                Codec.INT.fieldOf("aer").forGetter(ShapelessTheurgistsBenchRecipe::aer),
                Codec.INT.fieldOf("terra").forGetter(ShapelessTheurgistsBenchRecipe::terra),
                Codec.INT.fieldOf("aqua").forGetter(ShapelessTheurgistsBenchRecipe::aqua),
                Codec.INT.fieldOf("ignis").forGetter(ShapelessTheurgistsBenchRecipe::ignis),
                Codec.INT.fieldOf("ordo").forGetter(ShapelessTheurgistsBenchRecipe::ordo),
                Codec.INT.fieldOf("perditio").forGetter(ShapelessTheurgistsBenchRecipe::perditio),
                Codec.INT.fieldOf("mion").forGetter(ShapelessTheurgistsBenchRecipe::mion),
                ResourceLocation.CODEC.listOf().fieldOf("research").forGetter((ShapelessTheurgistsBenchRecipe::neededResearch))
            ).apply(inst, ShapelessTheurgistsBenchRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessTheurgistsBenchRecipe> STREAM_CODEC = TheurgySerializationUtils.composite(
            ItemStack.STREAM_CODEC,
            ShapelessTheurgistsBenchRecipe::result,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)),
            ShapelessTheurgistsBenchRecipe::inputs,
            ByteBufCodecs.INT,
            ShapelessTheurgistsBenchRecipe::aer,
            ByteBufCodecs.INT,
            ShapelessTheurgistsBenchRecipe::terra,
            ByteBufCodecs.INT,
            ShapelessTheurgistsBenchRecipe::aqua,
            ByteBufCodecs.INT,
            ShapelessTheurgistsBenchRecipe::ignis,
            ByteBufCodecs.INT,
            ShapelessTheurgistsBenchRecipe::ordo,
            ByteBufCodecs.INT,
            ShapelessTheurgistsBenchRecipe::perditio,
            ByteBufCodecs.INT,
            ShapelessTheurgistsBenchRecipe::mion,
            ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)),
            ShapelessTheurgistsBenchRecipe::getResearch,
            ShapelessTheurgistsBenchRecipe::new
        );

        @Override
        public MapCodec<ShapelessTheurgistsBenchRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShapelessTheurgistsBenchRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public static class Builder implements RecipeBuilder {
        protected String group;
        protected final RecipeCategory category;
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

        protected ItemStack result;
        protected List<Ingredient> inputs;
        protected int aer, terra, aqua, ignis, ordo, perditio;
        protected int mion;
        protected List<ResourceLocation> research;

        public Builder(String group, RecipeCategory category) {
            this.group = group;
            this.category = category;
            inputs = NonNullList.withSize(9, Ingredient.EMPTY);
            research = new ArrayList<>();
        }

        public Builder withResult(ItemStack result) {
            this.result = result;
            return this;
        }

        public Builder addInput(Ingredient input) {
            int j = -1;

            for (int i = 0; i < inputs.size(); i++) {
                if (inputs.get(i).isEmpty()) j = i;
            }

            if (j == -1) return this;

            inputs.set(j, input);
            return this;
        }

        public Builder withAspectus(int aer, int terra, int aqua, int ignis, int ordo, int perditio) {
            this.aer = aer;
            this.terra = terra;
            this.aqua = aqua;
            this.ignis = ignis;
            this.ordo = ordo;
            this.perditio = perditio;

            return this;
        }

        public Builder withMion(int mion) {
            this.mion = mion;
            return this;
        }

        public Builder unlockedBy(String name, Criterion<?> criterion) {
            this.criteria.put(name, criterion);
            return this;
        }

        public Builder addResearch(ResourceLocation research) {
            this.research.add(research);
            return this;
        }

        public Builder group(@Nullable String groupName) {
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

            this.inputs = this.inputs.stream().filter(s -> !s.isEmpty()).toList();

            ShapelessTheurgistsBenchRecipe shapelessTheurgistsBenchRecipe = new ShapelessTheurgistsBenchRecipe(
                this.result,
                this.inputs,
                aer,
                terra,
                aqua,
                ignis,
                ordo,
                perditio,
                mion,
                this.research
            );

            recipeOutput.accept(id, shapelessTheurgistsBenchRecipe, advancement$builder.build(id.withPrefix("recipes/theurgists_bench/shapeless/" + this.category.getFolderName() + "/")));
        }

        private void ensureValid(ResourceLocation id) {
            if (this.criteria.isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + id);
            }
        }
    }
}
