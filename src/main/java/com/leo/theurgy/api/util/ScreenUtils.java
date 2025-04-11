package com.leo.theurgy.api.util;

import com.leo.theurgy.api.data.aspectus.Aspectus;
import com.leo.theurgy.api.data.aspectus.AspectusHolder;
import com.leo.theurgy.impl.Theurgy;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.init.TheurgyDataComponents;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.Optional;

public class ScreenUtils {

    public static boolean isMouseHovering(double x, double y, double width, double height, double mouseX, double mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    public static void renderSlantedLine(VertexConsumer vc, Matrix4f matrix, float x1, float y1, float x2, float y2, float z, float thickness, int color) {
        vc.addVertex(matrix, x1, y1, z).setColor(color);
        vc.addVertex(matrix, x1 + thickness, y1, z).setColor(color);
        vc.addVertex(matrix, x2, y2, z).setColor(color);
        vc.addVertex(matrix, x2 + thickness, y2, z).setColor(color);
    }

    public static boolean isOutsideRenderArea(int renderAreaStartX, int renderAreaStartY, int renderAreaWidth, int renderAreaHeight, double entryStartX, double entryStartY, int entryWidth, int entryHeight) {
        int renderAreaEndX = renderAreaStartX + renderAreaWidth;
        int renderAreaEndY = renderAreaStartY + renderAreaHeight;

        double entryEndX = entryStartX + entryWidth;
        double entryEndY = entryStartY + entryHeight;

        return renderAreaStartX > entryStartX || entryEndX > renderAreaEndX ||
            renderAreaStartY > entryStartY || entryEndY > renderAreaEndY;
    }

    public static void renderAspectusDecorator(ItemStack stack, GuiGraphics guiGraphics, int x, int y) {
        AspectusHolder holder = stack.get(TheurgyDataComponents.ASPECTUS_HOLDER);

        if(holder == null) {
            return;
        }

        ClientPacketListener connection = Minecraft.getInstance().getConnection();

        if(connection == null) {
            return;
        }

        RegistryAccess.Frozen registry = connection.registryAccess();
        Optional<Aspectus> aspectus = holder.aspectus(registry);

        if(aspectus.isEmpty()) {
            Theurgy.LOGGER.error("Aspectus {} does not exist anymore!", holder.aspectusId());
            return;
        }

        PoseStack pose = guiGraphics.pose();

        int intColor = aspectus.get().color();

        Color color = new Color(intColor >> 16 & 0xFF, intColor >> 8 & 0xFF, intColor & 0xFF);
        color = color.brighter();

        int r, g, b;
        r = color.getRed();
        g = color.getGreen();
        b = color.getBlue();

        guiGraphics.setColor(r / 255f, g / 255f, b / 255f, 1);

        ResourceLocation id = holder.aspectusId();
        ResourceLocation icon = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "textures/aspectus/" + id.getPath() + ".png");

        pose.pushPose();
        pose.translate(0, 0, 300);

        guiGraphics.blit(
            icon,
            x,
            y,
            10,
            10,
            0,
            0,
            16,
            16,
            16,
            16
        );

        pose.popPose();

        guiGraphics.setColor(1, 1, 1, 1);
    }

    public static void renderAspectusTooltip(Aspectus aspectus, int amount, GuiGraphics guiGraphics, int x, int y) {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();

        if(connection == null) {
            return;
        }

        RegistryAccess.Frozen registry = connection.registryAccess();

        PoseStack pose = guiGraphics.pose();

        int intColor = aspectus.color();

        Color color = new Color(intColor >> 16 & 0xFF, intColor >> 8 & 0xFF, intColor & 0xFF);
        color = color.brighter();

        int r, g, b;
        r = color.getRed();
        g = color.getGreen();
        b = color.getBlue();

        guiGraphics.setColor(r / 255f, g / 255f, b / 255f, 1);

        ResourceLocation id = registry.registryOrThrow(TheurgyConstants.ASPECTUS_REGISTRY_KEY).getKey(aspectus);
        ResourceLocation icon = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "textures/aspectus/" + id.getPath() + ".png");

        pose.pushPose();
        pose.translate(0, 0, 2000);

        guiGraphics.blit(
            icon,
            x,
            y,
            10,
            10,
            0,
            0,
            16,
            16,
            16,
            16
        );

        guiGraphics.setColor(1, 1, 1, 1);

        pose.pushPose();

        float scale = 0.7f;
        float compensation = 1 / scale;

        pose.scale(scale, scale, 1);
        guiGraphics.drawString(Minecraft.getInstance().font, String.valueOf(amount), (x + 7) * compensation, (y + 6) * compensation, 0xFFFFFFFF, false);
        pose.popPose();

        pose.popPose();
    }
}
