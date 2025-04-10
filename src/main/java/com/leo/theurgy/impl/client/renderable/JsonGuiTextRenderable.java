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
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public record JsonGuiTextRenderable(int xPos, int yPos, boolean shadow, Component text,
                                    boolean centered) implements JsonGuiRenderable {

    public static final MapCodec<JsonGuiTextRenderable> CODEC = RecordCodecBuilder.mapCodec(
        inst -> inst.group(
            Codec.INT.fieldOf("xPos").forGetter(JsonGuiTextRenderable::xPos),
            Codec.INT.fieldOf("yPos").forGetter(JsonGuiTextRenderable::yPos),
            Codec.BOOL.optionalFieldOf("shadow", false).forGetter(JsonGuiTextRenderable::shadow),
            ComponentSerialization.CODEC.fieldOf("text").forGetter(JsonGuiTextRenderable::text),
            Codec.BOOL.optionalFieldOf("centered", false).forGetter(JsonGuiTextRenderable::centered)
        ).apply(inst, JsonGuiTextRenderable::new)
    );

    @Override
    public void render(GuiGraphics graphics, double leftPos, double topPos, double mouseX, double mouseY, float partialTick) {
        if (centered) {
            graphics.drawCenteredString(Minecraft.getInstance().font, text, (int) leftPos + xPos, (int) topPos + yPos, 0xFFFFFFFF);
            return;
        }

        graphics.drawString(Minecraft.getInstance().font, text, (int) leftPos + xPos, (int) topPos + yPos, 0xFFFFFFFF, shadow);
    }

    @Override
    public MapCodec<? extends JsonGuiRenderable> getCodec() {
        return CODEC;
    }

    @Override
    public int[] size() {
        return new int[]{
            Minecraft.getInstance().font.width(text),
            Minecraft.getInstance().font.lineHeight
        };
    }

    @Override
    public <T extends GuiEventListener & NarratableEntry> Optional<T> addButton(GuideBookScreen screen, GuideBookCategory category, GuideBookEntry entry) {
        return Optional.empty();
    }
}
