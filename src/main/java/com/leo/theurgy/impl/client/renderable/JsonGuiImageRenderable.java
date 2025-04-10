package com.leo.theurgy.impl.client.renderable;

import com.leo.theurgy.api.client.renderable.JsonGuiRenderable;
import com.leo.theurgy.api.guidebook.GuideBookScreen;
import com.leo.theurgy.api.guidebook.data.GuideBookCategory;
import com.leo.theurgy.api.guidebook.data.GuideBookEntry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record JsonGuiImageRenderable(int xPos, int yPos, int width, int height, int imageWidth, int imageHeight,
                                     int vOffset, int uOffset, ResourceLocation image) implements JsonGuiRenderable {

    public static final MapCodec<JsonGuiImageRenderable> CODEC = RecordCodecBuilder.mapCodec(
        inst -> inst.group(
            Codec.INT.fieldOf("xPos").forGetter(JsonGuiImageRenderable::xPos),
            Codec.INT.fieldOf("yPos").forGetter(JsonGuiImageRenderable::yPos),
            Codec.INT.fieldOf("width").forGetter(JsonGuiImageRenderable::width),
            Codec.INT.fieldOf("height").forGetter(JsonGuiImageRenderable::height),
            Codec.INT.optionalFieldOf("imageWidth", 256).forGetter(JsonGuiImageRenderable::imageWidth),
            Codec.INT.optionalFieldOf("imageHeight", 256).forGetter(JsonGuiImageRenderable::imageHeight),
            Codec.INT.optionalFieldOf("vOffset", 0).forGetter(JsonGuiImageRenderable::vOffset),
            Codec.INT.optionalFieldOf("uOffset", 0).forGetter(JsonGuiImageRenderable::uOffset),
            ResourceLocation.CODEC.fieldOf("image").forGetter(JsonGuiImageRenderable::image)
        ).apply(inst, JsonGuiImageRenderable::new)
    );

    @Override
    public void render(GuiGraphics graphics, double leftPos, double topPos, double mouseX, double mouseY, float partialTick) {
        graphics.blit(
            image,
            (int) leftPos + xPos,
            (int) topPos + yPos,
            uOffset,
            vOffset,
            width,
            height,
            imageWidth,
            imageHeight
        );
    }

    @Override
    public MapCodec<? extends JsonGuiRenderable> getCodec() {
        return CODEC;
    }

    @Override
    public int[] size() {
        return new int[]{
            width,
            height
        };
    }

    @Override
    public <T extends GuiEventListener & NarratableEntry> Optional<T> addButton(GuideBookScreen screen, GuideBookCategory category, GuideBookEntry entry) {
        return Optional.empty();
    }

}
