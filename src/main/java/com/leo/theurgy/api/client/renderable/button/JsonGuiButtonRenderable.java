package com.leo.theurgy.api.client.renderable.button;

import com.leo.theurgy.api.client.renderable.JsonGuiRenderable;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

public abstract class JsonGuiButtonRenderable implements JsonGuiRenderable {
    protected final int xPos, yPos, width, height;
    private final List<JsonGuiRenderable> layers;

    public JsonGuiButtonRenderable(int xPos, int yPos, int width, int height, List<JsonGuiRenderable> layers) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.layers = layers;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public List<JsonGuiRenderable> layers() {
        return layers;
    }

    public int xPos() {
        return xPos;
    }

    public int yPos() {
        return yPos;
    }

    @Override
    public void render(GuiGraphics graphics, double leftPos, double topPos, double mouseX, double mouseY, float partialTick) {
        layers.forEach(l -> l.render(graphics, leftPos + xPos, topPos + yPos, mouseX, mouseY, partialTick));
    }

    @Override
    public int[] size() {
        return new int[] {
            width(),
            height()
        };
    }
}
