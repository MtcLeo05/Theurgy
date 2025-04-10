package com.leo.theurgy.api.guidebook.data;

import com.leo.theurgy.api.client.renderable.JsonGuiRenderable;
import com.leo.theurgy.impl.init.TheurgyJsonGuiRenderableTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record GuideBookCategory(ResourceLocation id, int order, List<JsonGuiRenderable> iconLayers, List<JsonGuiRenderable> backgroundLayers, List<GuideBookEntry> entries) {

    public static final Codec<GuideBookCategory> CODEC = RecordCodecBuilder.create(
        inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(GuideBookCategory::id),
            Codec.INT.fieldOf("order").forGetter(GuideBookCategory::order),
            TheurgyJsonGuiRenderableTypes.JSON_GUI_RENDERABLE_TYPE_CODEC.listOf().fieldOf("unlockedEntryLayers").forGetter(GuideBookCategory::iconLayers),
            TheurgyJsonGuiRenderableTypes.JSON_GUI_RENDERABLE_TYPE_CODEC.listOf().fieldOf("backgroundLayers").forGetter(GuideBookCategory::backgroundLayers),
            GuideBookEntry.CODEC.listOf().fieldOf("entries").forGetter(GuideBookCategory::entries)
        ).apply(inst, GuideBookCategory::new)
    );

    public void renderBackground(GuiGraphics guiGraphics, int x, int y, double mouseX, double mouseY, float partialTick) {
        backgroundLayers().forEach(r -> r.render(guiGraphics, x, y, mouseX, mouseY, partialTick));
    }

    public void renderCategoryIcon(GuiGraphics guiGraphics, int x, int y, int mouseX, int mouseY, float partialTick) {
        iconLayers().forEach(l -> l.render(guiGraphics, x, y, mouseX, mouseY, partialTick));
    }

    public List<GuideBookEntry> dependencies(GuideBookEntry entry) {
        List<ResourceLocation> dependencies = entry.dependencies();

        return entries()
            .stream()
            .filter(e -> dependencies.contains(e.id()))
            .map(e -> entries
                .stream()
                .filter(d -> d.id().equals(e.id()))
                .findAny()
                .get()
            )
            .toList();
    }

    public int[] categoryButtonSize() {
        int w = 0, h = 0;

        for (JsonGuiRenderable layer : iconLayers) {
            if(layer.size()[0] > w) w = layer.size()[0];
            if(layer.size()[1] > h) h = layer.size()[1];
        }

        return new int[]{w, h};
    }

    public int[] categoryBackgroundSize() {
        int w = 0, h = 0;

        for (JsonGuiRenderable layer : backgroundLayers) {
            if(layer.size()[0] > w) w = layer.size()[0];
            if(layer.size()[1] > h) h = layer.size()[1];
        }

        return new int[]{w, h};
    }
}
