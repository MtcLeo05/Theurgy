package com.leo.theurgy.impl.client.renderable.conditional;

import com.leo.theurgy.api.client.renderable.JsonGuiRenderable;
import com.leo.theurgy.api.client.renderable.conditional.JsonGuiConditional;
import com.leo.theurgy.api.data.player.PlayerData;
import com.leo.theurgy.api.guidebook.GuideBookScreen;
import com.leo.theurgy.api.guidebook.data.GuideBookCategory;
import com.leo.theurgy.api.guidebook.data.GuideBookEntry;
import com.leo.theurgy.impl.init.TheurgyJsonGuiRenderableTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record JsonGuiResearchCompleteConditional(int xPos, int yPos, List<ResourceLocation> research, List<JsonGuiRenderable> incomplete, List<JsonGuiRenderable> complete) implements JsonGuiConditional {
    public static final MapCodec<JsonGuiResearchCompleteConditional> CODEC = RecordCodecBuilder.mapCodec(
        inst -> inst.group(
            Codec.INT.fieldOf("xPos").forGetter(JsonGuiResearchCompleteConditional::xPos),
            Codec.INT.fieldOf("yPos").forGetter(JsonGuiResearchCompleteConditional::yPos),
            ResourceLocation.CODEC.listOf().fieldOf("research").forGetter(JsonGuiResearchCompleteConditional::research),
            TheurgyJsonGuiRenderableTypes.JSON_GUI_RENDERABLE_TYPE_CODEC.listOf().optionalFieldOf("incomplete", new ArrayList<>()).forGetter(JsonGuiResearchCompleteConditional::incomplete),
            TheurgyJsonGuiRenderableTypes.JSON_GUI_RENDERABLE_TYPE_CODEC.listOf().fieldOf("complete").forGetter(JsonGuiResearchCompleteConditional::complete)
        ).apply(inst, JsonGuiResearchCompleteConditional::new)
    );

    @Override
    public boolean isConditionComplete() {
        return PlayerData.getOrCreateClient().hasResearches(research);
    }

    @Override
    public void render(GuiGraphics graphics, double leftPos, double topPos, double mouseX, double mouseY, float partialTick) {
        if(isConditionComplete()) {
            complete.forEach(c -> c.render(graphics, leftPos + xPos, topPos + yPos, mouseX, mouseY, partialTick));
            return;
        }

        if(incomplete.isEmpty()) return;

        incomplete.forEach(i -> i.render(graphics, leftPos + xPos, topPos + yPos, mouseX, mouseY, partialTick));
    }

    @Override
    public MapCodec<? extends JsonGuiRenderable> getCodec() {
        return CODEC;
    }

    @Override
    public int[] size() {
        return new int[]{
            0,
            0
        };
    }

    @Override
    public <T extends GuiEventListener & NarratableEntry> Optional<T> addButton(GuideBookScreen screen, GuideBookCategory category, GuideBookEntry entry) {
        return Optional.empty();
    }
}
