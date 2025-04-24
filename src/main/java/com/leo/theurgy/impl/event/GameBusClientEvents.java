package com.leo.theurgy.impl.event;

import com.leo.theurgy.api.data.aspectus.Aspectus;
import com.leo.theurgy.api.data.player.PlayerData;
import com.leo.theurgy.api.util.ScreenUtils;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.aspectus.AspectusHolderItemContext;
import com.leo.theurgy.impl.config.ConfigLoader;
import com.leo.theurgy.impl.init.TheurgyAttachmentTypes;
import com.leo.theurgy.impl.init.TheurgyDataComponents;
import com.leo.theurgy.impl.init.TheurgyItems;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = TheurgyConstants.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class GameBusClientEvents {

    private static int oldSlot = -1;
    @SubscribeEvent
    public static void handleScannerInventoryScanning(ClientTickEvent.Post event) {
        LocalPlayer player = Minecraft.getInstance().player;
        Screen screen = Minecraft.getInstance().screen;

        if(player == null || !(screen instanceof AbstractContainerScreen<?> s)) {
            oldSlot = -1;
            return;
        }

        ItemStack scanner = getScanner();

        if(scanner.isEmpty()) {
            oldSlot = -1;
            return;
        }

        Slot slot = s.getSlotUnderMouse();

        if(slot == null) {
            scanner.set(TheurgyDataComponents.SCAN_TIME, 0);
            scanner.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
            oldSlot = -1;
            return;
        }

        if (!slot.hasItem()) {
            oldSlot = -1;
            scanner.set(TheurgyDataComponents.SCAN_TIME, 0);
            scanner.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
            return;
        }

        ItemStack toScan = slot.getItem();

        if(PlayerData.getOrCreateClient().scannedItem(toScan)) return;

        if(oldSlot == -1) oldSlot = slot.index;

        if(oldSlot != slot.index) {
            scanner.set(TheurgyDataComponents.SCAN_TIME, 0);
            scanner.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
        }

        int i = scanner.get(TheurgyDataComponents.SCAN_TIME);
        scanner.set(TheurgyDataComponents.SCAN_TIME, Math.min(++i, ConfigLoader.getInstance().scanTime));
        scanner.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);

        if(i < ConfigLoader.getInstance().scanTime) return;

        scanner.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);

        PlayerData data = PlayerData.getOrCreateClient().scanItem(AspectusHolderItemContext.create(toScan, player));

        player.setData(TheurgyAttachmentTypes.PLAYER_DATA_ATTACHMENT, data);
        PacketDistributor.sendToServer(data);
    }

    private static ItemStack getScanner() {
        LocalPlayer player = Minecraft.getInstance().player;

        if(player == null || player.containerMenu == null) return ItemStack.EMPTY;

        AbstractContainerMenu menu = player.containerMenu;

        ItemStack scanner = menu.getCarried();

        if(scanner.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if(!scanner.is(TheurgyItems.CRYSTAL_SCANNER)) {
            return ItemStack.EMPTY;
        }

        return scanner;
    }

    @SubscribeEvent
    public static void prepareAspectusTooltip(RenderTooltipEvent.GatherComponents event) {
        ItemStack item = event.getItemStack();
        if(!PlayerData.getOrCreateClient().scannedItem(item)) return;

        event.getTooltipElements().add(Either.left(Component.empty()));
    }

    @SubscribeEvent
    public static void renderAspectuses(RenderTooltipEvent.Pre event) {
        if(Minecraft.getInstance().player == null) return;

        ItemStack item = event.getItemStack().copyWithCount(1);

        if(!PlayerData.getOrCreateClient().scannedItem(item)) return;

        Map<Aspectus, Integer> aspectuses = PlayerData.getOrCreateClient().getAspectus(Minecraft.getInstance().level, item);

        if(aspectuses.isEmpty()) return;

        int x = event.getX();
        int y = event.getY();

        int yOffset = 10 * (Math.max(event.getComponents().size() - 2, 0));

        GuiGraphics graphics = event.getGraphics();

        List<Map.Entry<Aspectus, Integer>> list = aspectuses.entrySet().stream().toList();

        for (int i = 0; i < list.size(); i++) {
            Map.Entry<Aspectus, Integer> entry = list.get(i);

            Aspectus aspectus = entry.getKey();
            Integer integer = entry.getValue();

            ScreenUtils.renderAspectusTooltip(aspectus, integer, graphics, x + 10 + (11 * i), y + yOffset);
        }
    }
}
