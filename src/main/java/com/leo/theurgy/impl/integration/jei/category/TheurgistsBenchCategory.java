package com.leo.theurgy.impl.integration.jei.category;

import com.leo.theurgy.api.data.aspectus.AspectusHolder;
import com.leo.theurgy.api.data.player.PlayerData;
import com.leo.theurgy.api.recipe.BaseTheurgistsBenchRecipe;
import com.leo.theurgy.api.util.ScreenUtils;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.init.TheurgyBlocks;
import com.leo.theurgy.impl.init.TheurgyDataComponents;
import com.leo.theurgy.impl.init.TheurgyItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TheurgistsBenchCategory implements IRecipeCategory<BaseTheurgistsBenchRecipe> {
    public static final ResourceLocation UID = TheurgyConstants.modLoc("theurgists_bench");

    public static final ResourceLocation BACKGROUND = TheurgyConstants.modLoc("textures/gui/container/jei/theurgists_bench.png");
    public static final ResourceLocation MISSING_RESEARCH = TheurgyConstants.modLoc("textures/gui/icons/missing_research.png");
    public static final ResourceLocation SHAPELESS = TheurgyConstants.modLoc("textures/gui/icons/bench_shapeless.png");

    public static final RecipeType<BaseTheurgistsBenchRecipe> RECIPE_TYPE = new RecipeType<>(UID, BaseTheurgistsBenchRecipe.class);

    private final IDrawable icon;

    public TheurgistsBenchCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(TheurgyBlocks.THEURGISTS_BENCH.get()));
    }

    @Override
    public RecipeType<BaseTheurgistsBenchRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public int getWidth() {
        return 148;
    }

    @Override
    public int getHeight() {
        return 118;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("gui.theurgy.theurgists_bench.title");
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BaseTheurgistsBenchRecipe recipe, IFocusGroup iFocusGroup) {
        PlayerData data = PlayerData.getOrCreateClient();

        boolean hasResearch = recipe.neededResearch().stream().allMatch(data::hasResearch);

        builder.addSlot(
            RecipeIngredientRole.OUTPUT,
            123,
            51
        ).addItemStack(recipe.result());

        addCrystals(builder, recipe, hasResearch);

        if(!hasResearch) return;

        for (int i = 0; i < recipe.inputs().size(); i++) {
            builder.addSlot(
                RecipeIngredientRole.INPUT,
                26 + (18 * (i % 3)),
                33 + (18 * (i / 3))
            ).addIngredients(recipe.inputs().get(i));
        }
    }

    private void addCrystals(IRecipeLayoutBuilder builder, BaseTheurgistsBenchRecipe recipe, boolean hasResearch) {
        builder.addSlot(
            RecipeIngredientRole.INPUT,
            44,
            7
        ).addItemStack(crystal("theurgy:ordo", hasResearch? recipe.ordo(): 1));

        builder.addSlot(
            RecipeIngredientRole.INPUT,
            83,
            26
        ).addItemStack(crystal("theurgy:aqua", hasResearch? recipe.aqua(): 1));

        builder.addSlot(
            RecipeIngredientRole.INPUT,
            83,
            75
        ).addItemStack(crystal("theurgy:aer", hasResearch? recipe.aer(): 1));

        builder.addSlot(
            RecipeIngredientRole.INPUT,
            44,
            95
        ).addItemStack(crystal("theurgy:perditio", hasResearch? recipe.perditio(): 1));

        builder.addSlot(
            RecipeIngredientRole.INPUT,
            5,
            75
        ).addItemStack(crystal("theurgy:ignis", hasResearch? recipe.ignis(): 1));

        builder.addSlot(
            RecipeIngredientRole.INPUT,
            5,
            26
        ).addItemStack(crystal("theurgy:terra", hasResearch? recipe.terra(): 1));
    }

    private ItemStack crystal(String type, int amount) {
        ItemStack stack = new ItemStack(TheurgyItems.MION_CRYSTAL, amount);
        stack.set(TheurgyDataComponents.ASPECTUS_HOLDER, new AspectusHolder(ResourceLocation.parse(type), amount));
        return stack;
    }

    @Override
    public void draw(BaseTheurgistsBenchRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(
            BACKGROUND,
            0,
            0,
            0,
            0,
            getWidth(),
            getHeight(),
            256,
            256
        );

        if(recipe.isShapeless()) {
            guiGraphics.blit(
                SHAPELESS,
                getWidth() - 12,
                4,
                8,
                8,
                0,
                0,
                36,
                36,
                36,
                36
            );
        }



        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(recipe.mion()), 131, 38, TheurgyConstants.MION_COLOR);

        PlayerData data = PlayerData.getOrCreateClient();
        boolean hasResearch = recipe.neededResearch().stream().allMatch(data::hasResearch);

        if(hasResearch) return;

        guiGraphics.blit(
            MISSING_RESEARCH,
            25,
            32,
            54,
            54,
            0,
            0,
            128,
            128,
            128,
            128
        );

        guiGraphics.drawString(Minecraft.getInstance().font, "?", 44 + 12, 7 + 12, 0xFFFFFFFF);
        guiGraphics.drawString(Minecraft.getInstance().font, "?", 83 + 12, 26 + 12, 0xFFFFFFFF);
        guiGraphics.drawString(Minecraft.getInstance().font, "?", 83 + 12, 75 + 12, 0xFFFFFFFF);
        guiGraphics.drawString(Minecraft.getInstance().font, "?", 44 + 12, 95 + 12, 0xFFFFFFFF);
        guiGraphics.drawString(Minecraft.getInstance().font, "?", 5 + 12, 75 + 12, 0xFFFFFFFF);
        guiGraphics.drawString(Minecraft.getInstance().font, "?", 5 + 12, 26 + 12, 0xFFFFFFFF);

        if(!ScreenUtils.isMouseHovering(25, 32, 54, 54, mouseX, mouseY)) return;

        guiGraphics.renderTooltip(
            Minecraft.getInstance().font,
            Component.translatable("gui." + TheurgyConstants.MODID + ".jei.missing_research", recipe.neededResearch().getFirst().toString()),
            (int) mouseX,
            (int) mouseY
        );
    }
}
