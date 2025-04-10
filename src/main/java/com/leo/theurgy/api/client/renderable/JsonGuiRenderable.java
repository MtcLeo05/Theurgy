package com.leo.theurgy.api.client.renderable;

import com.leo.theurgy.api.guidebook.GuideBookScreen;
import com.leo.theurgy.api.guidebook.data.GuideBookCategory;
import com.leo.theurgy.api.guidebook.data.GuideBookEntry;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

import java.util.Optional;

public interface JsonGuiRenderable {
    void render(GuiGraphics graphics, double leftPos, double topPos, double mouseX, double mouseY, float partialTick);
    MapCodec<? extends JsonGuiRenderable> getCodec();
    int[] size();
    <T extends GuiEventListener & NarratableEntry> Optional<T> addButton(GuideBookScreen screen, GuideBookCategory category, GuideBookEntry entry);
}
