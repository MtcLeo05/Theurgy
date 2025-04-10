package com.leo.theurgy.impl.client.gui.screen;

import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.block.menu.TheurgistsBenchMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TheurgistsBenchScreen extends AbstractContainerScreen<TheurgistsBenchMenu> {

    public static ResourceLocation BACKGROUND = TheurgyConstants.modLoc("textures/gui/container/theurgists_bench.png");

    public TheurgistsBenchScreen(TheurgistsBenchMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();

        inventoryLabelY = 10000;
        titleLabelY = 10000;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(
            BACKGROUND,
            x,
            y,
            0,
            0,
            184,
            197,
            256,
            256
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBg(guiGraphics, partialTick, mouseX, mouseX);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int mion = menu.getData().get(0);
        guiGraphics.drawCenteredString(this.font, String.valueOf(mion), this.leftPos + 143, this.topPos + 37, TheurgyConstants.MION_COLOR);

        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
