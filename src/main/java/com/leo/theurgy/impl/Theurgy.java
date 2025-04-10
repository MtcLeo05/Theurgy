package com.leo.theurgy.impl;

import com.leo.theurgy.impl.config.ConfigLoader;
import com.leo.theurgy.impl.init.*;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(TheurgyConstants.MODID)
public class Theurgy {
    public static final Logger LOGGER = LogUtils.getLogger();

    public Theurgy(IEventBus modEventBus, ModContainer modContainer) {
        ConfigLoader.getInstance().load();

        TheurgyResearchTypes.RESEARCH_TYPES_REGISTER.register(modEventBus);
        TheurgyJsonGuiRenderableTypes.JSON_GUI_RENDERABLE_TYPES_REGISTER.register(modEventBus);
        TheurgyGuideBookPageTypes.GUIDEBOOK_PAGE_TYPES_REGISTER.register(modEventBus);

        TheurgyAttachmentTypes.ATTACHMENT_TYPES.register(modEventBus);
        TheurgyDataComponents.DATA_COMPONENTS.register(modEventBus);
        TheurgyItems.ITEMS.register(modEventBus);
        TheurgyBlocks.BLOCKS.register(modEventBus);
        TheurgyBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        TheurgyMenuTypes.MENUS.register(modEventBus);
        TheurgyCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        TheurgyRecipes.RECIPE_TYPES.register(modEventBus);
        TheurgyRecipes.RECIPE_SERIALIZERS.register(modEventBus);
    }
}
