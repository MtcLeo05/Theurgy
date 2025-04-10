package com.leo.theurgy.impl.client.renderable.recipe;

import com.leo.theurgy.api.client.renderable.JsonGuiRenderable;
import com.leo.theurgy.api.client.renderable.recipe.JsonGuiRecipeRenderable;
import com.leo.theurgy.api.guidebook.GuideBookScreen;
import com.leo.theurgy.api.guidebook.data.GuideBookCategory;
import com.leo.theurgy.api.guidebook.data.GuideBookEntry;
import com.leo.theurgy.impl.TheurgyConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.Optional;

public record JsonGuiCraftingRecipeRenderable(int xPos, int yPos,
                                              ResourceLocation recipeId) implements JsonGuiRecipeRenderable {

    public static final MapCodec<JsonGuiCraftingRecipeRenderable> CODEC = RecordCodecBuilder.mapCodec(
        inst -> inst.group(
            Codec.INT.fieldOf("xPos").forGetter(JsonGuiCraftingRecipeRenderable::xPos),
            Codec.INT.fieldOf("yPos").forGetter(JsonGuiCraftingRecipeRenderable::yPos),
            ResourceLocation.CODEC.fieldOf("recipeId").forGetter(JsonGuiCraftingRecipeRenderable::recipeId)
        ).apply(inst, JsonGuiCraftingRecipeRenderable::new)
    );

    @Override
    public RecipeHolder<?> recipe() {
        return Minecraft.getInstance().level.getRecipeManager().byKey(recipeId).orElseThrow();
    }

    @Override
    public void render(GuiGraphics graphics, double leftPos, double topPos, double mouseX, double mouseY, float partialTick) {
        Recipe<?> recipe = recipe().value();

        if(!(recipe instanceof CraftingRecipe r)) return;

        boolean isShapeless = r instanceof ShapelessRecipe;

        NonNullList<Ingredient> inputs = recipe.getIngredients();
        ItemStack output = recipe.getResultItem(Minecraft.getInstance().level.registryAccess());

        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 500);

        graphics.blit(
            TheurgyConstants.modLoc("textures/gui/guide_book/crafting_recipe.png"),
            (int) (leftPos + xPos),
            (int) (topPos + yPos),
            0,
            0,
            54,
            94,
            54,
            94
        );

        graphics.renderItem(output, (int) (leftPos + xPos + 19), (int) (topPos + yPos + 77));

        for (int i = 0; i < inputs.size(); i++) {
            Ingredient input = inputs.get(i);
            if(input.isEmpty()) continue;

            ItemStack item = input.getItems()[0];

            graphics.renderItem(item, (int) (leftPos + xPos + 1 + (18 * (i % 3))), (int) (topPos + yPos + 1 + (18 * (i / 3))));
        }

        if (isShapeless) {
            graphics.blit(
                TheurgyConstants.modLoc("textures/gui/book_shapeless.png"),
                (int) (leftPos + xPos - 6),
                (int) (topPos + yPos - 6),
                8,
                8,
                0,
                0,
                12,
                12,
                12,
                12
            );
        }

        graphics.pose().popPose();
    }

    @Override
    public MapCodec<? extends JsonGuiRenderable> getCodec() {
        return CODEC;
    }

    @Override
    public int[] size() {
        return new int[]{
            46,
            82
        };
    }

    @Override
    public <T extends GuiEventListener & NarratableEntry> Optional<T> addButton(GuideBookScreen screen, GuideBookCategory category, GuideBookEntry entry) {
        return Optional.empty();
    }
}
