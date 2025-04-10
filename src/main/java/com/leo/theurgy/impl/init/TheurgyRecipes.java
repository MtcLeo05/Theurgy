package com.leo.theurgy.impl.init;

import com.leo.theurgy.api.recipe.BaseTheurgistsBenchRecipe;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.recipe.ShapelessTheurgistsBenchRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TheurgyRecipes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, TheurgyConstants.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, TheurgyConstants.MODID);

    public static final Supplier<RecipeType<BaseTheurgistsBenchRecipe>> THEURGISTS_BENCH_TYPE = RECIPE_TYPES.register(
        "theurgists_bench",
        () -> TheurgyConstants.THEURGISTS_BENCH_RECIPE_TYPE
    );

    public static final Supplier<RecipeSerializer<ShapelessTheurgistsBenchRecipe>> SHAPELESS_THEURGISTS_BENCH_SERIALIZER = RECIPE_SERIALIZERS.register(
        "shapeless_theurgists_bench",
        ShapelessTheurgistsBenchRecipe.Serializer::new
    );

}
