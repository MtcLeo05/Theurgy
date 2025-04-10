package com.leo.theurgy.impl.event;


import com.leo.theurgy.api.data.aspectus.Aspectus;
import com.leo.theurgy.api.data.aspectus.AspectusHolder;
import com.leo.theurgy.api.guidebook.GuideBookScreen;
import com.leo.theurgy.api.guidebook.data.GuideBookReloadListener;
import com.leo.theurgy.api.item.AspectusContainerRenderer;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.client.gui.screen.TheurgistsBenchScreen;
import com.leo.theurgy.impl.client.render.be.TheurgistsBenchBER;
import com.leo.theurgy.impl.init.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import java.util.Optional;

@EventBusSubscriber(modid = TheurgyConstants.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModBusClientEvents {

    @SubscribeEvent
    public static void registerItemColorHandlersEvent(RegisterColorHandlersEvent.Item event) {
        event.register(((stack, tintIndex) -> {
            if (tintIndex > 1) return -1;

            AspectusHolder holder = stack.get(TheurgyDataComponents.ASPECTUS_HOLDER);
            if (holder == null) return -1;

            ClientPacketListener connection = Minecraft.getInstance().getConnection();
            if (connection == null) return -1;

            RegistryAccess.Frozen registry = connection.registryAccess();
            Optional<Aspectus> aspectus = holder.aspectus(registry);

            return aspectus.map(Aspectus::color).orElse(-1);

        }), TheurgyItems.MION_CRYSTAL.get());

        event.register(
            ((stack, tintIndex) -> {
                Level level = Minecraft.getInstance().level;
                if(level == null || Minecraft.getInstance().player == null) return -1;

                BlockPos pos = Minecraft.getInstance().player.blockPosition();

                return BiomeColors.getAverageFoliageColor(level, pos);
            }),
            TheurgyBlocks.GREATWOOD_LEAVES.get()
        );
    }

    @SubscribeEvent
    public static void registerBlockColorHandlersEvent(RegisterColorHandlersEvent.Block event){
        event.register(
            ((state, level, pos, tintIndex) -> {
                if(pos == null || level == null) return -1;

                return BiomeColors.getAverageFoliageColor(level, pos);
            }),
            TheurgyBlocks.GREATWOOD_LEAVES.get()
        );
    }

    @SubscribeEvent
    public static void registerItemDecoratorsEvent(RegisterItemDecorationsEvent event) {
        AspectusContainerRenderer aspectusContainerRenderer = new AspectusContainerRenderer();

        event.register(TheurgyItems.MION_CRYSTAL.get(), aspectusContainerRenderer);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(TheurgyMenuTypes.THEURGISTS_BENCH_MENU.get(), TheurgistsBenchScreen::new);
        event.register(TheurgyMenuTypes.GUIDE_BOOK_MENU.get(), GuideBookScreen::new);
    }

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection == null) return;

        if (event.getTabKey() != TheurgyCreativeTabs.ITEMS.getKey()) return;

        Registry<Aspectus> registry = connection.registryAccess().registry(TheurgyConstants.ASPECTUS_REGISTRY_KEY).orElseThrow();

        for (ResourceLocation key : registry.keySet()) {
            ItemStack stack = new ItemStack(TheurgyItems.MION_CRYSTAL);
            stack.set(TheurgyDataComponents.ASPECTUS_HOLDER, new AspectusHolder(key, 1));

            event.accept(stack);
        }
    }

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(TheurgyBlockEntities.THEURGISTS_BENCH_BE.get(), TheurgistsBenchBER::new);
    }

    @SubscribeEvent
    public static void registerClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(GuideBookReloadListener.getInstance());
    }
}
