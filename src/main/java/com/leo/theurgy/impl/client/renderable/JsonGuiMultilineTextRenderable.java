package com.leo.theurgy.impl.client.renderable;

import com.leo.theurgy.api.client.renderable.JsonGuiRenderable;
import com.leo.theurgy.api.guidebook.GuideBookScreen;
import com.leo.theurgy.api.guidebook.data.GuideBookCategory;
import com.leo.theurgy.api.guidebook.data.GuideBookEntry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.Optional;
import java.util.function.BiConsumer;

public record JsonGuiMultilineTextRenderable(int xPos, int yPos, int maxWidth, int maxRows, Component text,
                                             boolean centered) implements JsonGuiRenderable {

    public static final MapCodec<JsonGuiMultilineTextRenderable> CODEC = RecordCodecBuilder.mapCodec(
        inst -> inst.group(
            Codec.INT.fieldOf("xPos").forGetter(JsonGuiMultilineTextRenderable::xPos),
            Codec.INT.fieldOf("yPos").forGetter(JsonGuiMultilineTextRenderable::yPos),
            Codec.INT.fieldOf("maxWidth").forGetter(JsonGuiMultilineTextRenderable::maxWidth),
            Codec.INT.fieldOf("maxRows").forGetter(JsonGuiMultilineTextRenderable::maxRows),
            ComponentSerialization.CODEC.fieldOf("text").forGetter(JsonGuiMultilineTextRenderable::text),
            Codec.BOOL.optionalFieldOf("centered", false).forGetter(JsonGuiMultilineTextRenderable::centered)
        ).apply(inst, JsonGuiMultilineTextRenderable::new)
    );


    @Override
    public void render(GuiGraphics graphics, double leftPos, double topPos, double mouseX, double mouseY, float partialTick) {
        MultiLineTextWidget widget = new MultiLineTextWidget(text, Minecraft.getInstance().font);
        widget.setPosition((int) leftPos + xPos, (int) topPos + yPos);
        widget.setCentered(centered);
        widget.setMaxWidth(maxWidth);
        widget.setMaxRows(maxRows);
        widget.renderWidget(graphics, (int) mouseX, (int) mouseY, partialTick);
    }

    @Override
    public MapCodec<? extends JsonGuiRenderable> getCodec() {
        return CODEC;
    }

    @Override
    public int[] size() {
        return new int[]{
            maxWidth(),
            Minecraft.getInstance().font.lineHeight
        };
    }

    @Override
    public <T extends GuiEventListener & NarratableEntry> Optional<T> addButton(GuideBookScreen screen, GuideBookCategory category, GuideBookEntry entry) {
        return Optional.empty();
    }

}
