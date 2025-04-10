package com.leo.theurgy.impl.client.renderable.button;

import com.leo.theurgy.api.client.renderable.JsonGuiRenderable;
import com.leo.theurgy.api.client.renderable.button.JsonGuiButtonRenderable;
import com.leo.theurgy.api.guidebook.GuideBookScreen;
import com.leo.theurgy.api.guidebook.data.GuideBookCategory;
import com.leo.theurgy.api.guidebook.data.GuideBookEntry;
import com.leo.theurgy.impl.init.TheurgyJsonGuiRenderableTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Optional;

public class JsonGuiChangePageButtonRenderable extends JsonGuiButtonRenderable {
    public static final MapCodec<JsonGuiChangePageButtonRenderable> CODEC = RecordCodecBuilder.mapCodec(
        inst -> inst.group(
            Codec.INT.fieldOf("xPos").forGetter(JsonGuiChangePageButtonRenderable::xPos),
            Codec.INT.fieldOf("yPos").forGetter(JsonGuiChangePageButtonRenderable::yPos),
            Codec.INT.fieldOf("width").forGetter(JsonGuiChangePageButtonRenderable::width),
            Codec.INT.fieldOf("height").forGetter(JsonGuiChangePageButtonRenderable::height),
            TheurgyJsonGuiRenderableTypes.JSON_GUI_RENDERABLE_TYPE_CODEC.listOf().fieldOf("layers").forGetter(JsonGuiChangePageButtonRenderable::layers),
            Codec.BOOL.fieldOf("nextPage").forGetter(JsonGuiChangePageButtonRenderable::nextPage)
        ).apply(inst, JsonGuiChangePageButtonRenderable::new)
    );

    private final boolean nextPage;

    public JsonGuiChangePageButtonRenderable(int xPos, int yPos, int width, int height, List<JsonGuiRenderable> layers, boolean nextPage) {
        super(xPos, yPos, width, height, layers);
        this.nextPage = nextPage;
    }

    public boolean nextPage() {
        return nextPage;
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
                if(nextPage) {
                    screen.increasePage();
                    return;
                }
                screen.decreasePage();
            })
            .pos(pageStart[0] + xPos, pageStart[1] + yPos)
            .size(width, height)
            .build();

        return Optional.of((T) button);
    }
}
