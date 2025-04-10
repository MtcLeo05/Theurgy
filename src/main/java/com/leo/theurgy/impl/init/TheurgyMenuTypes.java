package com.leo.theurgy.impl.init;

import com.leo.theurgy.api.guidebook.GuideBookMenu;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.block.menu.TheurgistsBenchMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TheurgyMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, TheurgyConstants.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<TheurgistsBenchMenu>> THEURGISTS_BENCH_MENU = MENUS.register(
        "theurgists_bench",
        () ->  IMenuTypeExtension.create(TheurgistsBenchMenu::new)
    );

    public static final DeferredHolder<MenuType<?>, MenuType<GuideBookMenu>> GUIDE_BOOK_MENU = MENUS.register(
        "guide_book",
        () ->  IMenuTypeExtension.create(GuideBookMenu::new)
    );

}
