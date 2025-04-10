package com.leo.theurgy.impl.init;

import com.leo.theurgy.impl.TheurgyConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.stream.Stream;

public class TheurgyCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TheurgyConstants.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ITEMS = CREATIVE_MODE_TABS.register("items", () ->
        CreativeModeTab.builder()
            .title(Component.translatable(TheurgyConstants.MODID + ".itemGroup.items"))
            .icon(() -> TheurgyItems.MION_CRYSTAL.get().getDefaultInstance())
            .displayItems((idp, output) -> {
                Stream<? extends Item> items = TheurgyItems.ITEMS.getEntries().stream().map(DeferredHolder::get);
                items.forEach(i -> output.accept(new ItemStack(i)));
            })
            .build()
    );
}
