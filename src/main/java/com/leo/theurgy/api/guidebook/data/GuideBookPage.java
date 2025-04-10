package com.leo.theurgy.api.guidebook.data;

import com.leo.theurgy.api.guidebook.GuideBookScreen;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

import java.util.List;

public abstract class GuideBookPage {
    public abstract void render(GuiGraphics graphics, int leftPos, int topPos, double mouseX, double mouseY, float partialTick);

    public abstract MapCodec<? extends GuideBookPage> getCodec();

    public abstract int[] size();

    public abstract <T extends GuiEventListener & NarratableEntry> List<T> buttons(GuideBookScreen screen, GuideBookCategory category, GuideBookEntry entry);
}
