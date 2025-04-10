package com.leo.theurgy.impl.client.renderable.button;

import com.leo.theurgy.api.client.renderable.JsonGuiRenderable;
import com.leo.theurgy.api.client.renderable.button.JsonGuiButtonRenderable;
import com.leo.theurgy.api.data.player.PlayerData;
import com.leo.theurgy.api.guidebook.GuideBookScreen;
import com.leo.theurgy.api.guidebook.data.GuideBookCategory;
import com.leo.theurgy.api.guidebook.data.GuideBookEntry;
import com.leo.theurgy.impl.init.TheurgyAttachmentTypes;
import com.leo.theurgy.impl.init.TheurgyJsonGuiRenderableTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Optional;

public class JsonGuiCompleteEntryButtonRenderable extends JsonGuiButtonRenderable {
    public static final MapCodec<JsonGuiCompleteEntryButtonRenderable> CODEC = RecordCodecBuilder.mapCodec(
        inst -> inst.group(
            Codec.INT.fieldOf("xPos").forGetter(JsonGuiCompleteEntryButtonRenderable::xPos),
            Codec.INT.fieldOf("yPos").forGetter(JsonGuiCompleteEntryButtonRenderable::yPos),
            Codec.INT.fieldOf("width").forGetter(JsonGuiCompleteEntryButtonRenderable::width),
            Codec.INT.fieldOf("height").forGetter(JsonGuiCompleteEntryButtonRenderable::height),
            TheurgyJsonGuiRenderableTypes.JSON_GUI_RENDERABLE_TYPE_CODEC.listOf().fieldOf("layers").forGetter(JsonGuiCompleteEntryButtonRenderable::layers)
        ).apply(inst, JsonGuiCompleteEntryButtonRenderable::new)
    );

    public JsonGuiCompleteEntryButtonRenderable(int xPos, int yPos, int width, int height, List<JsonGuiRenderable> layers) {
        super(xPos, yPos, width, height, layers);
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
                PlayerData data = PlayerData.getOrCreateClient();

                data = data.addCompletedBookEntry(category, entry);

                Minecraft.getInstance().player.setData(TheurgyAttachmentTypes.PLAYER_DATA_ATTACHMENT, data);
                PacketDistributor.sendToServer(data);
            })
            .pos(pageStart[0] + xPos, pageStart[1] + yPos)
            .size(width, height)
            .build();

        return Optional.of((T) button);
    }
}
