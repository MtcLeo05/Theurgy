package com.leo.theurgy.api.guidebook.data;

import com.leo.theurgy.api.client.renderable.JsonGuiRenderable;
import com.leo.theurgy.api.util.ScreenUtils;
import com.leo.theurgy.impl.init.TheurgyGuideBookPageTypes;
import com.leo.theurgy.impl.init.TheurgyJsonGuiRenderableTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record GuideBookEntry(ResourceLocation id, List<ResourceLocation> dependencies, List<JsonGuiRenderable> lockedEntryLayers,
                             List<JsonGuiRenderable> unlockedEntryLayers, List<JsonGuiRenderable> completedEntryLayers,
                             List<GuideBookPage> pages, int xPos, int yPos, List<JsonGuiRenderable> entryTitle) {
    public static final Codec<GuideBookEntry> CODEC = RecordCodecBuilder.create(
        inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(GuideBookEntry::id),
            ResourceLocation.CODEC.listOf().optionalFieldOf("dependencies", new ArrayList<>()).forGetter(GuideBookEntry::dependencies),
            TheurgyJsonGuiRenderableTypes.JSON_GUI_RENDERABLE_TYPE_CODEC.listOf().optionalFieldOf("lockedEntryLayers", new ArrayList<>()).forGetter(GuideBookEntry::lockedEntryLayers),
            TheurgyJsonGuiRenderableTypes.JSON_GUI_RENDERABLE_TYPE_CODEC.listOf().fieldOf("unlockedEntryLayers").forGetter(GuideBookEntry::unlockedEntryLayers),
            TheurgyJsonGuiRenderableTypes.JSON_GUI_RENDERABLE_TYPE_CODEC.listOf().optionalFieldOf("completedEntryLayers", new ArrayList<>()).forGetter(GuideBookEntry::completedEntryLayers),
            TheurgyGuideBookPageTypes.GUIDEBOOK_RENDERABLE_TYPE_CODEC.listOf().fieldOf("pages").forGetter(GuideBookEntry::pages),
            Codec.INT.fieldOf("xPos").forGetter(GuideBookEntry::xPos),
            Codec.INT.fieldOf("yPos").forGetter(GuideBookEntry::yPos),
            TheurgyJsonGuiRenderableTypes.JSON_GUI_RENDERABLE_TYPE_CODEC.listOf().fieldOf("entryTitle").forGetter(GuideBookEntry::entryTitle)
        ).apply(inst, GuideBookEntry::new)
    );

    public void renderDependencyLine(GuiGraphics guiGraphics, GuideBookEntry e, double startX, double startY) {
        ScreenUtils.renderSlantedLine(
            guiGraphics.bufferSource().getBuffer(RenderType.gui()),
            guiGraphics.pose().last().pose(),
            (int) (startX + xPos()),
            (int) (startY + yPos()),
            (int) (startX + e.xPos()),
            (int) (startY + e.yPos()),
            0,
            1f,
            0xFFFFFFFF
        );
    }



    public void renderEntryButton(boolean isLocked, boolean isCompleted, GuiGraphics graphics, double leftPos, double topPos, double mouseX, double mouseY, float partialTick) {
        if(isLocked && !lockedEntryLayers.isEmpty()) {
            lockedEntryLayers.forEach(l -> l.render(graphics, leftPos, topPos, mouseX, mouseY, partialTick));
            return;
        }

        if(isCompleted && !completedEntryLayers.isEmpty()) {
            completedEntryLayers.forEach(l -> l.render(graphics, leftPos, topPos, mouseX, mouseY, partialTick));
            return;
        }

        unlockedEntryLayers().forEach(l -> l.render(graphics, leftPos, topPos, mouseX, mouseY, partialTick));
    }

    public int[] entryButtonSize() {
        int w = 0, h = 0;

        for (JsonGuiRenderable layer : unlockedEntryLayers) {
            if(layer.size()[0] > w) w = layer.size()[0];
            if(layer.size()[1] > h) h = layer.size()[1];
        }

        return new int[]{w, h};
    }

    public void renderTitle(GuiGraphics graphics, int leftPos, int topPos, int mouseX, int mouseY, float partialTick) {
        entryTitle.forEach(l -> l.render(graphics, leftPos, topPos, mouseX, mouseY, partialTick));
    }
}
