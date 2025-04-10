package com.leo.theurgy.impl.research;

import com.leo.theurgy.api.recipe.BaseTheurgistsBenchRecipe;
import com.leo.theurgy.api.research.ResearchType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemResearchType extends ResearchType {
    public static final MapCodec<ItemResearchType> CODEC = RecordCodecBuilder.mapCodec(
        inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("research").forGetter(ItemResearchType::researchToUnlock),
            ItemStack.CODEC.listOf().fieldOf("items").forGetter(ItemResearchType::items),
            Codec.BOOL.fieldOf("consume").forGetter(ItemResearchType::consume)
        ).apply(inst, ItemResearchType::new)
    );

    private final ResourceLocation researchToUnlock;
    private final List<ItemStack> items;
    private final boolean consume;

    public List<ItemStack> items() {
        return items;
    }

    public boolean consume() {
        return consume;
    }

    public ItemResearchType(ResourceLocation researchToUnlock, List<ItemStack> items, boolean consume) {
        this.items = items;
        this.researchToUnlock = researchToUnlock;
        this.consume = consume;
    }

    @Override
    public ResourceLocation researchToUnlock() {
        return researchToUnlock;
    }

    @Override
    public boolean tryComplete(ServerPlayer player) {
        List<ItemStack> inputItems = items().stream().filter(i -> !i.isEmpty()).toList();
        List<ItemStack> testInputs = new ArrayList<>(player.getInventory().items);

        List<ItemStack> mutableTests = new ArrayList<>(testInputs);

        return inputItems.stream().allMatch(iItem -> {
            Iterator<ItemStack> iterator = mutableTests.iterator();
            while (iterator.hasNext()) {
                ItemStack test = iterator.next();
                if(BaseTheurgistsBenchRecipe.isSameItemSameComponentsBiggerCount(test, iItem)) {
                    if(consume()) test.shrink(iItem.getCount());
                    iterator.remove();
                    return true;
                }
            }
            return false;
        });
    }

    @Override
    public MapCodec<? extends ResearchType> getCodec() {
        return CODEC;
    }
}
