package com.leo.theurgy.impl.client.gui.overlay;

import com.leo.theurgy.api.data.mion.ChunkMion;
import com.leo.theurgy.api.data.player.PlayerData;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.init.TheurgyItems;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;

public class MionMeterHUD implements LayeredDraw.Layer {
    public static final ResourceLocation BACKGROUND = TheurgyConstants.modLoc("textures/gui/overlay/mion_overlay.png");

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        LocalPlayer player = Minecraft.getInstance().player;

        if(!player.getMainHandItem().is(TheurgyItems.CRYSTAL_SCANNER)) return;

        RenderSystem.enableBlend();

        int mainX = 20, mainY = 20;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.5f, 0.5f, 0.5f);
        guiGraphics.blit(
            BACKGROUND,
            mainX,
            mainY,
            0,
            0,
            55,
            153
        );

        ChunkMion mion = ChunkMion.getOrCreateMion(player.blockPosition(), player.level());


        int effectiveMaxMion = mion.maxMion() - mion.corruptMion();
        int scaledCorruptMion = mion.corruptMion() > 0 && mion.maxMion() > 0 ? (mion.currentMion() * 133) / mion.maxMion() : 0;
        int scaledMion = mion.currentMion() - mion.corruptMion() > 0 && effectiveMaxMion > 0 ? ((mion.currentMion() - mion.corruptMion()) * 133) / effectiveMaxMion : 0;

        if(scaledMion > 0) {
            guiGraphics.blit(
                BACKGROUND,
                mainX + 23,
                mainY + 10 + 133 - scaledMion,
                55,
                10,
                9,
                scaledMion
            );
        }

        if(scaledCorruptMion > 0) {
            guiGraphics.blit(
                BACKGROUND,
                mainX + 23,
                mainY + 10 + 133 - scaledMion,
                64,
                10,
                9,
                scaledMion
            );
        }

        RenderSystem.disableBlend();

        guiGraphics.pose().popPose();

        if (!Screen.hasShiftDown()) return;
        if (Minecraft.getInstance().screen != null) return;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.5f, 0.5f, 0.5f);
        guiGraphics.drawCenteredString(
            Minecraft.getInstance().font,
            mion.currentMion() + "/" + mion.maxMion() + "/" + mion.corruptMion(),
            mainX + 28,
            mainY + 170,
            0xFF000000 | TheurgyConstants.MION_COLOR
        );
        guiGraphics.pose().popPose();
    }
}
