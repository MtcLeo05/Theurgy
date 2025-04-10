package com.leo.theurgy.impl.guidebook.data;

import com.leo.theurgy.api.client.renderable.JsonGuiRenderable;
import com.leo.theurgy.api.guidebook.GuideBookScreen;
import com.leo.theurgy.api.guidebook.data.GuideBookCategory;
import com.leo.theurgy.api.guidebook.data.GuideBookEntry;
import com.leo.theurgy.api.guidebook.data.GuideBookPage;
import com.leo.theurgy.impl.init.TheurgyJsonGuiRenderableTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

import java.util.List;

public class CompositeGuideBookPage extends GuideBookPage {
    public static final MapCodec<CompositeGuideBookPage> CODEC = RecordCodecBuilder.mapCodec(
        inst -> inst.group(
            TheurgyJsonGuiRenderableTypes.JSON_GUI_RENDERABLE_TYPE_CODEC.listOf().fieldOf("renderables").forGetter(CompositeGuideBookPage::renderables)
        ).apply(inst, CompositeGuideBookPage::new)
    );

    private final List<JsonGuiRenderable> renderables;

    public CompositeGuideBookPage(List<JsonGuiRenderable> renderables) {
        this.renderables = renderables;
    }

    public List<JsonGuiRenderable> renderables() {
        return renderables;
    }

    @Override
    public void render(GuiGraphics graphics, int leftPos, int topPos, double mouseX, double mouseY, float partialTick) {
        renderables.forEach(r -> r.render(graphics, leftPos, topPos, mouseX, mouseY, partialTick));
    }

    @Override
    public MapCodec<? extends GuideBookPage> getCodec() {
        return CODEC;
    }

    @Override
    public int[] size() {
        int w = 0, h = 0;

        for (JsonGuiRenderable layer : renderables) {
            if(layer.size()[0] > w) w = layer.size()[0];
            if(layer.size()[1] > h) h = layer.size()[1];
        }

        return new int[]{w, h};
    }

    @Override
    public <T extends GuiEventListener & NarratableEntry> List<T> buttons(GuideBookScreen screen, GuideBookCategory category, GuideBookEntry entry) {
        return renderables
            .stream()
            .filter(r -> r.addButton(screen, category, entry).isPresent())
            .map(r -> (T) (r.addButton(screen, category, entry).get()))
            .toList();
    }
}
