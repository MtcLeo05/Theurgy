package com.leo.theurgy.api.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShapeUtil {
    public static VoxelShape shapeFromDimension(float x1, float y1, float z1, float x2, float y2, float z2) {
        return Block.box(x1, y1, z1, x1 + x2, y1 + y2, z1 + z2);
    }

    public static VoxelShape multiRotation(final VoxelShape shape, int pitch, int yaw) {
        VoxelShape newShape = shape;

        for (int i = 0; i < pitch; i++) {
            newShape = ShapeUtil.clockwisePitch(newShape);
        }

        for (int i = 0; i < yaw; i++) {
            newShape = ShapeUtil.clockwiseYaw(newShape);
        }

        return newShape;
    }

    private static VoxelShape clockwisePitch(VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};
        buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(minX, 1 - maxZ, minY, maxX, 1 - minZ, maxY)));
        return buffer[1];
    }

    private static VoxelShape clockwiseYaw(VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};
        buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
        return buffer[1];
    }
}
