package com.leo.theurgy.impl.block.entity;

import com.leo.theurgy.api.block.entity.BaseMionHandlerMenuProviderBE;
import com.leo.theurgy.api.data.player.PlayerData;
import com.leo.theurgy.api.recipe.BaseTheurgistsBenchRecipe;
import com.leo.theurgy.api.recipe.TheurgistsBenchRecipeInput;
import com.leo.theurgy.api.research.ResearchGatedRecipeHandler;
import com.leo.theurgy.impl.block.menu.TheurgistsBenchMenu;
import com.leo.theurgy.impl.init.TheurgyBlockEntities;
import com.leo.theurgy.impl.init.TheurgyRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TheurgistsBenchBE extends BaseMionHandlerMenuProviderBE implements ResearchGatedRecipeHandler {

    public static int[] CRYSTAL_SLOTS = new int[]{0, 1, 2, 3, 4, 5};
    public static int[] INPUT_SLOTS = new int[]{6, 7, 8, 9, 10, 11, 12, 13, 14};
    public static int OUTPUT_SLOT = 15;

    private ResourceLocation benchRecipeId;
    private ResourceLocation craftingRecipeId;

    public TheurgistsBenchBE(BlockPos pos, BlockState blockState) {
        super(TheurgyBlockEntities.THEURGISTS_BENCH_BE.get(), pos, blockState);

        itemHandler = new ItemStackHandler(16) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                TheurgistsBenchBE.this.sync();
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.theurgy.theurgists_bench.title");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new TheurgistsBenchMenu(containerId, playerInventory, this, data);
    }

    public void tryCreateResult(ServerPlayer player) {
        if (!(getLevel() instanceof ServerLevel level)) return;

        if (tryMakeCraftingResult(level)) {
            sync();
            return;
        }

        tryMakeTheurgistsBenchResult(level, player);
        sync();
    }

    private boolean tryMakeCraftingResult(ServerLevel level) {
        craftingRecipeId = null;
        benchRecipeId = null;
        CraftingInput.Positioned positioned = CraftingInput.ofPositioned(3, 3, inputs());

        Optional<RecipeHolder<CraftingRecipe>> optional = level.getServer()
            .getRecipeManager()
            .getRecipeFor(RecipeType.CRAFTING, positioned.input(), level);

        if (optional.isEmpty()) {
            itemHandler.setStackInSlot(OUTPUT_SLOT, ItemStack.EMPTY);
            return false;
        }

        craftingRecipeId = optional.get().id();
        itemHandler.setStackInSlot(OUTPUT_SLOT, optional.get().value().getResultItem(level.registryAccess()).copy());

        return true;
    }

    private void tryMakeTheurgistsBenchResult(ServerLevel level, ServerPlayer player) {
        craftingRecipeId = null;
        benchRecipeId = null;
        TheurgistsBenchRecipeInput input = new TheurgistsBenchRecipeInput(inputs());

        Optional<RecipeHolder<BaseTheurgistsBenchRecipe>> optional = level.getServer()
            .getRecipeManager()
            .getRecipeFor(TheurgyRecipes.THEURGISTS_BENCH_TYPE.get(), input, level);

        if(optional.isEmpty() || !optional.get().value().checkAspectuses(aspectuses())) {
            itemHandler.setStackInSlot(OUTPUT_SLOT, ItemStack.EMPTY);
            return;
        }

        benchRecipeId = optional.get().id();

        if(!hasResearch(player)){
            itemHandler.setStackInSlot(OUTPUT_SLOT, ItemStack.EMPTY);
            return;
        }

        itemHandler.setStackInSlot(OUTPUT_SLOT, optional.get().value().result().copy());
    }

    public boolean tryPickupItem(ServerPlayer player, boolean simulate) {
        if(!(getLevel() instanceof ServerLevel sLevel)) return true;

        if(craftingRecipeId != null) {
            return tryPickupCraftingItem(sLevel, simulate);
        }

        if(benchRecipeId != null) {
            return tryPickupBenchItem(sLevel, player, simulate);
        }

        return false;
    }

    private boolean tryPickupBenchItem(ServerLevel level, ServerPlayer player, boolean simulate) {
        RecipeHolder<?> recipeHolder = level.getRecipeManager().byKey(benchRecipeId).orElseThrow();

        if(!(recipeHolder.value() instanceof BaseTheurgistsBenchRecipe recipe)) return false;

        if(!doesChunkHaveMion(recipe.mion())) return false;
        if(!recipe.checkAspectuses(aspectuses())) return false;
        if(!hasResearch(player)) return false;

        if(simulate) return true;

        List<Ingredient> inputs = recipe.inputs();
        for (int i = 0; i < inputs.size(); i++) {
            ItemStack input = itemHandler.getStackInSlot(INPUT_SLOTS[i]);
            input.shrink(1);
            itemHandler.setStackInSlot(INPUT_SLOTS[i], input);
        }

        for (int i : CRYSTAL_SLOTS) {
            ItemStack slot = itemHandler.getStackInSlot(i);
            slot.shrink(recipe.orderedAspectuses()[i]);
            itemHandler.setStackInSlot(i, slot);
        }

        //If somehow the mion disappears, to avoid going negative
        if(doesChunkHaveMion(recipe.mion())) {
            removeMionToChunk(recipe.mion());
        }

        sync();
        return true;
    }

    private boolean tryPickupCraftingItem(ServerLevel level, boolean simulate) {
        RecipeHolder<?> recipeHolder = level.getRecipeManager().byKey(craftingRecipeId).orElseThrow();

        if(!(recipeHolder.value() instanceof CraftingRecipe)) return false;

        if(simulate) return true;

        for (int i : INPUT_SLOTS) {
            ItemStack slot = itemHandler.getStackInSlot(i);
            slot.shrink(1);
            itemHandler.setStackInSlot(i, slot);
        }

        sync();
        return true;
    }

    protected List<ItemStack> aspectuses() {
        List<ItemStack> toRet = new ArrayList<>();

        for (int slot : CRYSTAL_SLOTS) {
            toRet.add(itemHandler.getStackInSlot(slot));
        }

        return toRet;
    }

    protected List<ItemStack> inputs() {
        List<ItemStack> toRet = new ArrayList<>();

        for (int slot : INPUT_SLOTS) {
            toRet.add(itemHandler.getStackInSlot(slot));
        }

        return toRet;
    }

    protected ItemStack output() {
        return itemHandler.getStackInSlot(OUTPUT_SLOT);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if(tag.contains("craftingRecipe")) {
            craftingRecipeId = ResourceLocation.parse(tag.getString("craftingRecipe"));
        }

        if(tag.contains("benchRecipe")) {
            benchRecipeId = ResourceLocation.parse(tag.getString("benchRecipe"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        if(craftingRecipeId != null) tag.putString("craftingRecipe", craftingRecipeId.toString());
        if(benchRecipeId != null) tag.putString("benchRecipe", benchRecipeId.toString());
    }

    @Override
    public boolean hasResearch(ServerPlayer player) {
        if(craftingRecipeId != null) return true;
        if(benchRecipeId == null) return true;

        RecipeHolder<?> recipeHolder = level.getRecipeManager().byKey(benchRecipeId).orElseThrow();
        if(!(recipeHolder.value() instanceof BaseTheurgistsBenchRecipe recipe)) return true;

        PlayerData playerData = PlayerData.getOrCreate(player);

        List<ResourceLocation> neededResearch = recipe.neededResearch();
        if(neededResearch.isEmpty()) return true;

        return new HashSet<>(playerData.researchProgress().completedResearch()).containsAll(neededResearch);
    }

    @Override
    @Nullable
    public ServerPlayer owner() {
        return null;
    }
}
