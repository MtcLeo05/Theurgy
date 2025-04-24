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

public class TheurgistsBenchBER implements BlockEntityRenderer<TheurgistsBenchBE> {
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
