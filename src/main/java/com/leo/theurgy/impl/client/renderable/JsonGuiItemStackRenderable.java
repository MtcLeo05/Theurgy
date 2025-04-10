package com.leo.theurgy.impl.client.renderable;

import com.leo.theurgy.api.client.renderable.JsonGuiRenderable;
import com.leo.theurgy.api.guidebook.GuideBookScreen;
import com.leo.theurgy.api.guidebook.data.GuideBookCategory;
import com.leo.theurgy.api.guidebook.data.GuideBookEntry;
import com.leo.theurgy.api.util.ScreenUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.function.BiConsumer;

public record JsonGuiItemStackRenderable(int xPos, int yPos, ItemStack item) implements JsonGuiRenderable {

    public static final MapCodec<JsonGuiItemStackRenderable> CODEC = RecordCodecBuilder.mapCodec(
        inst -> inst.group(
            Codec.INT.fieldOf("xPos").forGetter(JsonGuiItemStackRenderable::xPos),
            Codec.INT.fieldOf("yPos").forGetter(JsonGuiItemStackRenderable::yPos),
            ItemStack.CODEC.fieldOf("item").forGetter(JsonGuiItemStackRenderable::item)
        ).apply(inst, JsonGuiItemStackRenderable::new)
    );

    @Override
    public void render(GuiGraphics graphics, double leftPos, double topPos, double mouseX, double mouseY, float partialTick) {
        graphics.renderFakeItem(item, (int) leftPos + xPos, (int) topPos + yPos);
    }

    @Override
    public MapCodec<? extends JsonGuiRenderable> getCodec() {
        return CODEC;
    }

    @Override
    public int[] size() {
        return new int[]{
            16,
            16
        };
    }

    @Override
    public <T extends GuiEventListener & NarratableEntry> Optional<T> addButton(GuideBookScreen screen, GuideBookCategory category, GuideBookEntry entry) {
        return Optional.empty();
    }
}
