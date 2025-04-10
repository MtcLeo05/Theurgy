package com.leo.theurgy.impl.recipe;

import com.leo.theurgy.api.recipe.BaseTheurgistsBenchRecipe;
import com.leo.theurgy.api.recipe.TheurgistsBenchRecipeInput;
import com.leo.theurgy.api.util.TheurgySerializationUtils;
import com.leo.theurgy.impl.init.TheurgyRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    @Override
    public List<ResourceLocation> neededResearch() {
        return this.research;
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
}
