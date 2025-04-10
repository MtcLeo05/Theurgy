package com.leo.theurgy.impl.init;

import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.item.GuideBookItem;
import com.leo.theurgy.impl.item.MionCrystalItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TheurgyItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(TheurgyConstants.MODID);

    public static final DeferredHolder<Item, Item> MION_CRYSTAL = ITEMS.register("mion_crystal",
        () -> new MionCrystalItem(
            new Item.Properties()
        )
    );

    public static final DeferredHolder<Item, Item> GUIDE_BOOK = ITEMS.register("guide_book",
        () -> new GuideBookItem(
            new Item.Properties()
        )
    );
}
