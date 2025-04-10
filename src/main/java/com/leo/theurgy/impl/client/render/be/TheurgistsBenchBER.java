package com.leo.theurgy.impl.client.render.be;

import com.leo.theurgy.impl.block.TheurgistsBenchBlock;
import com.leo.theurgy.impl.block.entity.TheurgistsBenchBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class TheurgistsBenchBER implements BlockEntityRenderer<TheurgistsBenchBE> {

    private static final Map<Integer, float[]> CRYSTAL_POSITION = Map.of(
        0, new float[]{8 / 16f, 13.5f / 16},
        1, new float[]{2.8f / 16f, 11.7f / 16},
        2, new float[]{2.8f / 16f, 5.4f / 16},
        3, new float[]{8 / 16f, 2.5f / 16},
        4, new float[]{13.2f / 16f, 5.4f / 16},
        5, new float[]{13.2f / 16f, 11.7f / 16}
    );

    public TheurgistsBenchBER(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(TheurgistsBenchBE be, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        Direction direction = be.getBlockState().getValue(TheurgistsBenchBlock.FACING);
        float yRot = direction.toYRot();

        pose.pushPose();
        pose.translate(0.5f, 0, 0.5f);
        pose.mulPose(Axis.YP.rotationDegrees(yRot));
        for (int slot : TheurgistsBenchBE.INPUT_SLOTS) {
            ItemStack item = be.getInventory().getStackInSlot(slot);
            pose.pushPose();

            float x, y, z;

            x = ((11 - 3 * (slot % 3)) / 16f) - 0.5f;
            y = 1.05f;
            z = ((17 - 3 * (slot / 3)) / 16f) - 0.5f;

            pose.translate(x, y, z);
            pose.scale(0.125f, 0.125f, 0.125f);
            itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, packedLight, packedOverlay, pose, buffer, be.getLevel(), 0);
            pose.popPose();
        }
        pose.popPose();
    }
}
