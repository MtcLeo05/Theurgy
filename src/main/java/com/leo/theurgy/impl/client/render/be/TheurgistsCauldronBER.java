package com.leo.theurgy.impl.client.render.be;

import com.leo.theurgy.api.data.aspectus.Aspectus;
import com.leo.theurgy.api.data.aspectus.AspectusHolder;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.block.entity.TheurgistsCauldronBE;
import com.leo.theurgy.impl.event.ModBusClientEvents;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.Map;

import static net.minecraft.client.renderer.RenderStateShard.LIGHTMAP;
import static net.minecraft.client.renderer.RenderStateShard.RENDERTYPE_CUTOUT_SHADER;

public class TheurgistsCauldronBER implements BlockEntityRenderer<TheurgistsCauldronBE> {
    public static final RenderStateShard.TextureStateShard ASPECTUS_SHEET = new RenderStateShard.TextureStateShard(
        TheurgyConstants.modLoc("textures/atlas/aspectus.png"), false, false
    );

    public static final RenderType ASPECTUS = RenderType.create(
        "aspectus",
        DefaultVertexFormat.BLOCK,
        VertexFormat.Mode.QUADS,
        786432,
        true,
        false,
        RenderType.CompositeState.builder()
            .setLightmapState(LIGHTMAP)
            .setShaderState(RENDERTYPE_CUTOUT_SHADER)
            .setTextureState(ASPECTUS_SHEET)
            .createCompositeState(true)
    );

    private final Font font;

    public TheurgistsCauldronBER(BlockEntityRendererProvider.Context context) {
        this.font = context.getFont();
    }

    @Override
    public void render(TheurgistsCauldronBE be, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        List<Map.Entry<ResourceLocation, Integer>> list = be.getAspectusStorage().allAspectuses().entrySet().stream().toList();

        BlockPos pos = be.getBlockPos();

        Vec3 target = Minecraft.getInstance().player.position();
        Vec3 center = be.getBlockPos().getCenter();

        Vec3 dir = target.subtract(center);

        double angle = Math.toDegrees(Math.atan2(dir.z, dir.x));

        pose.pushPose();
        pose.translate(0.5f, 0, 0.5f);
        pose.mulPose(Axis.YN.rotationDegrees((float) angle - 90f));

        pose.pushPose();
        pose.translate(-1.55, 1.5, 0);
        pose.scale(0.025f, 0.025f, 1);
        for (int i = 0; i < list.size(); i++) {
            Map.Entry<ResourceLocation, Integer> entry = list.get(i);

            ResourceLocation aspectus = entry.getKey();
            int amount = entry.getValue();

            ResourceLocation atlasLocation = aspectus.withPrefix("aspectus/");
            TextureAtlasSprite asSprite = ModBusClientEvents.ASPECTUS_ATLAS.getSprite(atlasLocation);

            AspectusHolder holder = new AspectusHolder(aspectus, amount);
            Aspectus as = holder.aspectus(be.getLevel().registryAccess()).orElseThrow();

            int[] asPos = getAspectusPosition(i, list.size(), 7, 18, 18);

            int x = asPos[0];
            int y = asPos[1];

            VertexConsumer aspectBuilder = buffer.getBuffer(ASPECTUS);
            TheurgistsCauldronBER.drawFlippedQuad(aspectBuilder, pose, x, y, 0, x + 16, y + 16, 0, asSprite.getU1(), asSprite.getV1(), asSprite.getU0(), asSprite.getV0(), packedLight, as.color());

            pose.pushPose();
            pose.translate(10, 5f, 0.005f);
            pose.scale(1, -1, 1);
            this.font.drawInBatch(String.valueOf(amount), x, y, 0xFFFFFFFF, false, pose.last().pose(), buffer, Font.DisplayMode.NORMAL, 0x00000000, packedLight);
            pose.popPose();
        }
        pose.popPose();

        pose.popPose();


        FluidStack fluid = be.getFluidTank().getFluid();
        if (fluid.isEmpty()) return;

        IClientFluidTypeExtensions fluidExtension = IClientFluidTypeExtensions.of(fluid.getFluid());

        ResourceLocation still = fluidExtension.getStillTexture(fluid);

        if (still == null) return;

        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(still);
        FluidState fluidState = fluid.getFluid().defaultFluidState();

        int tintColor = fluidExtension.getTintColor(fluidState, be.getLevel(), pos);
        VertexConsumer builder = buffer.getBuffer(ItemBlockRenderTypes.getRenderLayer(fluidState));

        float fluidProgress = (fluid.getAmount() / 1000f) * 13f;

        pose.pushPose();
        TheurgistsCauldronBER.drawQuad(builder, pose, 1 / 16f, (fluidProgress + 2) / 16f, 1 / 16f, 15 / 16f, (fluidProgress + 2) / 16f, 15 / 16f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), packedLight, tintColor);
        pose.popPose();
    }

    public static int[] getAspectusPosition(int i, int totalElements, int maxPerRow, int spacingX, int spacingY) {
        int fullRows = totalElements / maxPerRow;
        int hasPartialRow = (totalElements % maxPerRow) != 0 ? 1 : 0;
        int totalRows = fullRows + hasPartialRow;

        int row = i / maxPerRow;
        int col = i % maxPerRow;

        int lastRowCount = totalElements % maxPerRow;
        int isLastRow = (row == totalRows - 1) ? 1 : 0;
        int elementsInThisRow = (isLastRow == 1 && lastRowCount != 0) ? lastRowCount : maxPerRow;
        int offsetX = (maxPerRow - elementsInThisRow) * spacingX / 2;

        int x = offsetX + (col * spacingX);
        int y = (totalRows - 1 - row) * spacingY;

        return new int[]{x, y};
    }


    private static void drawVertex(VertexConsumer builder, PoseStack poseStack, float x, float y, float z, float u, float v, int packedLight, int color) {
        builder.addVertex(poseStack.last().pose(), x, y, z)
            .setColor(color)
            .setUv(u, v)
            .setUv1(0, 0)
            .setUv2(packedLight, packedLight)
            .setNormal(1, 0, 0);
    }

    private static void drawQuad(VertexConsumer builder, PoseStack poseStack, float x0, float y0, float z0, float x1, float y1, float z1, float u0, float v0, float u1, float v1, int packedLight, int color) {
        RenderSystem.enableBlend();
        drawVertex(builder, poseStack, x0, y0, z0, u0, v0, packedLight, color);
        drawVertex(builder, poseStack, x0, y1, z1, u0, v1, packedLight, color);
        drawVertex(builder, poseStack, x1, y1, z1, u1, v1, packedLight, color);
        drawVertex(builder, poseStack, x1, y0, z0, u1, v0, packedLight, color);
        RenderSystem.disableBlend();
    }

    private static void drawFlippedQuad(VertexConsumer builder, PoseStack poseStack, float x0, float y0, float z0, float x1, float y1, float z1, float u0, float v0, float u1, float v1, int packedLight, int color) {
        RenderSystem.enableBlend();
        drawVertex(builder, poseStack, x1, y0, z0, u1, v0, packedLight, color);
        drawVertex(builder, poseStack, x1, y1, z1, u1, v1, packedLight, color);
        drawVertex(builder, poseStack, x0, y1, z1, u0, v1, packedLight, color);
        drawVertex(builder, poseStack, x0, y0, z0, u0, v0, packedLight, color);
        RenderSystem.disableBlend();
    }
}
