package com.leo.theurgy.impl.recipe.bench;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ShapedTheurgistsBenchRecipe extends BaseTheurgistsBenchRecipe {

    private final ShapedRecipePattern pattern;

    public ShapedTheurgistsBenchRecipe(ItemStack result, ShapedRecipePattern pattern, int aer, int terra, int aqua, int ignis, int ordo, int perditio, int mion, List<ResourceLocation> research) {
        super(result, pattern.ingredients(), aer, terra, aqua, ignis, ordo, perditio, mion, research);
        this.pattern = pattern;
    }

    @Override
    public boolean isShapeless() {
        return false;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= inputs.size();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TheurgyRecipes.SHAPED_THEURGISTS_BENCH_SERIALIZER.get();
    }

    public List<ResourceLocation> getResearch() {
        return this.neededResearch();
    }

    @Override
    public boolean matches(TheurgistsBenchRecipeInput input, Level level) {
        List<ItemStack> inputItems = input.items().stream().filter(i -> !i.isEmpty()).toList();
        List<Ingredient> testInputs = new ArrayList<>(this.inputs());

        if(inputItems.size() != testInputs.size()) return false;

        boolean cont = true;

        for (int i = 0; i < testInputs.size(); i++) {
            Ingredient test = testInputs.get(i);
            if (!cont) return false;

            cont = test.test(inputItems.get(i));
        }

        return cont;
    }

    public ShapedRecipePattern pattern() {
        return pattern;
    }

    public static class Serializer implements RecipeSerializer<ShapedTheurgistsBenchRecipe> {
        public static final MapCodec<ShapedTheurgistsBenchRecipe> CODEC = RecordCodecBuilder.mapCodec(
            inst -> inst.group(
                ItemStack.CODEC.fieldOf("result").forGetter(ShapedTheurgistsBenchRecipe::result),
                ShapedRecipePattern.MAP_CODEC.fieldOf("pattern").forGetter(ShapedTheurgistsBenchRecipe::pattern),
                Codec.INT.optionalFieldOf("aer", 0).forGetter(ShapedTheurgistsBenchRecipe::aer),
                Codec.INT.optionalFieldOf("terra", 0).forGetter(ShapedTheurgistsBenchRecipe::terra),
                Codec.INT.optionalFieldOf("aqua", 0).forGetter(ShapedTheurgistsBenchRecipe::aqua),
                Codec.INT.optionalFieldOf("ignis", 0).forGetter(ShapedTheurgistsBenchRecipe::ignis),
                Codec.INT.optionalFieldOf("ordo", 0).forGetter(ShapedTheurgistsBenchRecipe::ordo),
                Codec.INT.optionalFieldOf("perditio", 0).forGetter(ShapedTheurgistsBenchRecipe::perditio),
                Codec.INT.optionalFieldOf("mion", 0).forGetter(ShapedTheurgistsBenchRecipe::mion),
                ResourceLocation.CODEC.listOf().fieldOf("research").forGetter((ShapedTheurgistsBenchRecipe::neededResearch))
            ).apply(inst, ShapedTheurgistsBenchRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, ShapedTheurgistsBenchRecipe> STREAM_CODEC = TheurgySerializationUtils.composite(
            ItemStack.STREAM_CODEC,
            ShapedTheurgistsBenchRecipe::result,
            ShapedRecipePattern.STREAM_CODEC,
            ShapedTheurgistsBenchRecipe::pattern,
            ByteBufCodecs.INT,
            ShapedTheurgistsBenchRecipe::aer,
            ByteBufCodecs.INT,
            ShapedTheurgistsBenchRecipe::terra,
            ByteBufCodecs.INT,
            ShapedTheurgistsBenchRecipe::aqua,
            ByteBufCodecs.INT,
            ShapedTheurgistsBenchRecipe::ignis,
            ByteBufCodecs.INT,
            ShapedTheurgistsBenchRecipe::ordo,
            ByteBufCodecs.INT,
            ShapedTheurgistsBenchRecipe::perditio,
            ByteBufCodecs.INT,
            ShapedTheurgistsBenchRecipe::mion,
            ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)),
            ShapedTheurgistsBenchRecipe::getResearch,
            ShapedTheurgistsBenchRecipe::new
        );

        @Override
        public MapCodec<ShapedTheurgistsBenchRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShapedTheurgistsBenchRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public static Builder builder(String group, RecipeCategory category) {
        return new Builder(group, category);
    }

    public static class Builder implements RecipeBuilder {
        protected String group;
        protected final RecipeCategory category;
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

        private final List<String> rows = Lists.newArrayList();
        private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();

        protected ItemStack result;
        protected List<Ingredient> inputs;
        protected int aer, terra, aqua, ignis, ordo, perditio;
        protected int mion;
        protected List<ResourceLocation> research;

        private Builder(String group, RecipeCategory category) {
            this.group = group;
            this.category = category;
            inputs = NonNullList.withSize(9, Ingredient.EMPTY);
            research = new ArrayList<>();
        }

        public Builder withResult(ItemStack result) {
            this.result = result;
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

        public Builder define(Character symbol, TagKey<Item> tag) {
            return this.define(symbol, Ingredient.of(tag));
        }

        public Builder define(Character symbol, ItemLike item) {
            return this.define(symbol, Ingredient.of(item));
        }

        public Builder define(Character symbol, Ingredient ingredient) {
            if (this.key.containsKey(symbol)) {
                throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
            } else if (symbol == ' ') {
                throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
            } else {
                this.key.put(symbol, ingredient);
                return this;
            }
        }

        public Builder pattern(String pattern) {
            if (!this.rows.isEmpty() && pattern.length() != this.rows.get(0).length()) {
                throw new IllegalArgumentException("Pattern must be the same width on every line!");
            } else {
                this.rows.add(pattern);
                return this;
            }
        }

        @Override
        public Item getResult() {
            return result.getItem();
        }

        @Override
        public void save(RecipeOutput recipeOutput, ResourceLocation id) {
            ShapedRecipePattern sRP = this.ensureValid(id);
            Advancement.Builder advancement$builder = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);

            this.criteria.forEach(advancement$builder::addCriterion);

            this.inputs = this.inputs.stream().filter(s -> !s.isEmpty()).toList();

            ShapedTheurgistsBenchRecipe shapedTheurgistsBenchRecipe = new ShapedTheurgistsBenchRecipe(
                this.result,
                sRP,
                aer,
                terra,
                aqua,
                ignis,
                ordo,
                perditio,
                mion,
                this.research
            );

            recipeOutput.accept(id, shapedTheurgistsBenchRecipe, advancement$builder.build(id.withPrefix("recipes/theurgists_bench/shaped/" + this.category.getFolderName() + "/")));
        }

        private ShapedRecipePattern ensureValid(ResourceLocation loaction) {
            if (this.criteria.isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + loaction);
            } else {
                return ShapedRecipePattern.of(this.key, this.rows);
            }
        }
    }
}
