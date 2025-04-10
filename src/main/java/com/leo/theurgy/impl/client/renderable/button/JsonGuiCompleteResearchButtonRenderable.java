package com.leo.theurgy.impl.client.renderable.button;

import com.leo.theurgy.api.client.renderable.JsonGuiRenderable;
import com.leo.theurgy.api.client.renderable.button.JsonGuiButtonRenderable;
import com.leo.theurgy.api.guidebook.GuideBookScreen;
import com.leo.theurgy.api.guidebook.data.GuideBookCategory;
import com.leo.theurgy.api.guidebook.data.GuideBookEntry;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.init.TheurgyJsonGuiRenderableTypes;
import com.leo.theurgy.impl.network.payloads.ServerResearchComplete;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Optional;

public class JsonGuiCompleteResearchButtonRenderable extends JsonGuiButtonRenderable {
    public static final MapCodec<JsonGuiCompleteResearchButtonRenderable> CODEC = RecordCodecBuilder.mapCodec(
        inst -> inst.group(
            Codec.INT.fieldOf("xPos").forGetter(JsonGuiCompleteResearchButtonRenderable::xPos),
            Codec.INT.fieldOf("yPos").forGetter(JsonGuiCompleteResearchButtonRenderable::yPos),
            Codec.INT.fieldOf("width").forGetter(JsonGuiCompleteResearchButtonRenderable::width),
            Codec.INT.fieldOf("height").forGetter(JsonGuiCompleteResearchButtonRenderable::height),
            TheurgyJsonGuiRenderableTypes.JSON_GUI_RENDERABLE_TYPE_CODEC.listOf().fieldOf("layers").forGetter(JsonGuiCompleteResearchButtonRenderable::layers),
            ResourceLocation.CODEC.listOf().fieldOf("research").forGetter(JsonGuiCompleteResearchButtonRenderable::research)
        ).apply(inst, JsonGuiCompleteResearchButtonRenderable::new)
    );

    private final List<ResourceLocation> research;

    public JsonGuiCompleteResearchButtonRenderable(int xPos, int yPos, int width, int height, List<JsonGuiRenderable> layers, List<ResourceLocation> research) {
        super(xPos, yPos, width, height, layers);
        this.research = research;
    }

    public List<ResourceLocation> research() {
        return research;
    }

    @Override
    public MapCodec<? extends JsonGuiRenderable> getCodec() {
        return CODEC;
    }

    @Override
    public <T extends GuiEventListener & NarratableEntry> Optional<T> addButton(GuideBookScreen screen, GuideBookCategory category, GuideBookEntry entry) {
        int[] pageStart = screen.getPageStart();

        if(pageStart == null) return Optional.empty();

        Button button = Button.builder(Component.empty(), (b) -> {
                PacketDistributor.sendToServer(new ServerResearchComplete(research(), Optional.of(TheurgyConstants.makeGuideBookEntry(category, entry))));
            })
            .pos(pageStart[0] + xPos, pageStart[1] + yPos)
            .size(width, height)
            .build();

        return Optional.of((T) button);
    }
}
