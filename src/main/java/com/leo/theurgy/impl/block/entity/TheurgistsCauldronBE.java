package com.leo.theurgy.impl.block.entity;

import com.leo.theurgy.api.block.entity.BaseResearchRecipeHandler;
import com.leo.theurgy.api.data.player.PlayerData;
import com.leo.theurgy.api.recipe.TheurgistsCauldronHeatRecipeInput;
import com.leo.theurgy.api.recipe.TheurgistsCauldronRecipeInput;
import com.leo.theurgy.impl.block.TheurgistsCauldronBlock;
import com.leo.theurgy.impl.capability.aspectus.CompoundAspectusStorage;
import com.leo.theurgy.impl.init.TheurgyBlockEntities;
import com.leo.theurgy.impl.init.TheurgyMappingTypes;
import com.leo.theurgy.impl.init.TheurgyRecipes;
import com.leo.theurgy.impl.recipe.cauldron.TheurgistsCauldronHeatRecipe;
import com.leo.theurgy.impl.recipe.cauldron.TheurgistsCauldronRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TheurgistsCauldronBE extends BaseResearchRecipeHandler {

    private final CompoundAspectusStorage aspectusStorage;
    private final FluidTank fluidTank;
    private UUID playerUUID;
    private ServerPlayer player;

    public TheurgistsCauldronBE(BlockPos pos, BlockState blockState) {
        super(TheurgyBlockEntities.THEURGISTS_CAULDRON_BE.get(), pos, blockState);

        aspectusStorage = new CompoundAspectusStorage(-1) {
            @Override
            public boolean addAspectus(ResourceLocation key, int amount, boolean simulate) {
                boolean b = super.addAspectus(key, amount, simulate);
                TheurgistsCauldronBE.this.sync();
                return b;
            }

            @Override
            public boolean removeAspectus(ResourceLocation key, int amount, boolean simulate) {
                boolean b = super.removeAspectus(key, amount, simulate);
                TheurgistsCauldronBE.this.sync();
                return b;
            }
        };

        fluidTank = new FluidTank(1000) {
            @Override
            public FluidStack drain(int maxDrain, FluidAction action) {
                FluidStack drain = super.drain(maxDrain, action);
                TheurgistsCauldronBE.this.updateLight();
                return drain;
            }

            @Override
            public FluidStack drain(FluidStack resource, FluidAction action) {
                FluidStack drain = super.drain(resource, action);
                TheurgistsCauldronBE.this.updateLight();
                return drain;
            }

            @Override
            public int fill(FluidStack resource, FluidAction action) {
                int fill = super.fill(resource, action);
                TheurgistsCauldronBE.this.updateLight();
                return fill;
            }

        };
    }

    public void onPlace(ServerPlayer player) {
        this.playerUUID = player.getUUID();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.contains("aspectusStorage")) {
            aspectusStorage.deserializeNBT(registries, tag.getCompound("aspectusStorage"));
        }

        if (tag.contains("tank")) {
            fluidTank.readFromNBT(registries, tag.getCompound("tank"));
        }

        if (tag.contains("playerUUID")) {
            playerUUID = tag.getUUID("playerUUID");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        if (aspectusStorage != null) {
            tag.put("aspectusStorage", aspectusStorage.serializeNBT(registries));
        }

        if (fluidTank != null && !fluidTank.isEmpty()) {
            CompoundTag fluidTag = new CompoundTag();
            fluidTag.put("Fluid", fluidTank.getFluid().save(registries));
            tag.put("tank", fluidTag);
        }

        if (playerUUID != null) {
            tag.putUUID("playerUUID", playerUUID);
        }
    }

    @Override
    public boolean hasResearch(ServerPlayer player) {
        throw new RuntimeException("Use the hasResearch with Item!");
    }

    public FluidTank getFluidTank() {
        return fluidTank;
    }

    public boolean hasResearch(ServerLevel level, ItemStack item) {
        RecipeHolder<TheurgistsCauldronRecipe> recipe = getRecipe(item, level);

        if (recipe == null) return false;

        PlayerData playerData = PlayerData.getOrCreate(player);

        List<ResourceLocation> neededResearch = recipe.value().neededResearch();
        if (neededResearch.isEmpty()) return true;

        return new HashSet<>(playerData.researchProgress().completedResearch()).containsAll(neededResearch);
    }

    private @Nullable RecipeHolder<TheurgistsCauldronRecipe> getRecipe(ItemStack stack, Level level) {
        RecipeHolder<TheurgistsCauldronHeatRecipe> heatRecipe = getHeatRecipe(level);

        int heat = heatRecipe != null ? heatRecipe.value().heat() : 0;

        TheurgistsCauldronRecipeInput input = new TheurgistsCauldronRecipeInput(stack, fluidTank.getFluidInTank(0), heat, aspectusStorage.allAspectuses());

        Optional<RecipeHolder<TheurgistsCauldronRecipe>> optional = level.getServer()
            .getRecipeManager()
            .getRecipeFor(TheurgyRecipes.THEURGISTS_CAULDRON_TYPE.get(), input, level);

        return optional.orElse(null);
    }

    private @Nullable RecipeHolder<TheurgistsCauldronHeatRecipe> getHeatRecipe(Level level) {
        TheurgistsCauldronHeatRecipeInput input = new TheurgistsCauldronHeatRecipeInput(level.getBlockState(this.getBlockPos().below()));

        Optional<RecipeHolder<TheurgistsCauldronHeatRecipe>> optional = level.getServer()
            .getRecipeManager()
            .getRecipeFor(TheurgyRecipes.THEURGISTS_CAULDRON_HEAT_TYPE.get(), input, level);

        return optional.orElse(null);
    }

    public List<ItemStack> handleItem(ItemStack item, ServerLevel level) {
        if (player == null) player = (ServerPlayer) level.getPlayerByUUID(playerUUID);

        IFluidHandlerItem capability = item.getCapability(Capabilities.FluidHandler.ITEM);
        if (capability != null) {
            FluidStack fluid = capability.getFluidInTank(0);

            if (fluidTank.isEmpty() && !capability.getFluidInTank(0).isEmpty()) {
                fluidTank.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
                capability.drain(fluid, IFluidHandler.FluidAction.EXECUTE);
                item.shrink(1);

                sync();
                return List.of(capability.getContainer(), item);
            }
        }

        if(this.fluidTank.isEmpty()) return List.of(item);

        RecipeHolder<TheurgistsCauldronRecipe> recipe = getRecipe(item, level);

        if (recipe != null && hasResearch(level, item)) {
            TheurgistsCauldronRecipe r = recipe.value();

            item.shrink(r.input().getCount());
            r.aspectuses().forEach((key, value) -> {
                this.aspectusStorage.removeAspectus(key, value, false);
            });

            fluidTank.drain(r.fluid(), IFluidHandler.FluidAction.EXECUTE);

            sync();
            return List.of(r.result().copy());
        }

        Map<ResourceLocation, Integer> aspectuses = TheurgyMappingTypes.getAspectusFromItem(item, level);
        if (aspectuses.isEmpty()) return List.of(item);

        aspectuses.forEach((key, value) -> {
            aspectusStorage.addAspectus(key, value, false);
        });

        sync();
        return List.of();
    }

    public CompoundAspectusStorage getAspectusStorage() {
        return aspectusStorage;
    }

    @Override
    @Nullable
    public ServerPlayer owner() {
        return player;
    }
}
