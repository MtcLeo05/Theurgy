package com.leo.theurgy.api.item;

import com.leo.theurgy.api.util.ScreenUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;

public class AspectusContainerRenderer implements IItemDecorator {

    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int x, int y) {
        ScreenUtils.renderAspectusDecorator(stack, guiGraphics, x, y);
        return false;
    }
}
