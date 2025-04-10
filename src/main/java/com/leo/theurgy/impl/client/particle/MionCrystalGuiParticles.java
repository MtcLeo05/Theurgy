package com.leo.theurgy.impl.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.util.RandomSource;
import team.lodestar.lodestone.helpers.RandomHelper;
import team.lodestar.lodestone.registry.common.particle.LodestoneScreenParticleTypes;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.ScreenParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;

public class MionCrystalGuiParticles {

    public static void spawnParticles(ScreenParticleHolder target, int color) {
        RandomSource rand = Minecraft.getInstance().level.getRandom();

        int  r, g, b;
        r = color >> 16 & 0xFF;
        g = color >> 8 & 0xFF;
        b = color & 0xFF;

        ColorParticleData colorData = ColorParticleData.create(r / 255f, g / 255f, b / 255f).build();

        ScreenParticleBuilder.create(LodestoneScreenParticleTypes.SPARKLE, target)
            .setTransparencyData(GenericParticleData.create(0.25f, 0f).setEasing(Easing.SINE_IN_OUT).build())
            .setScaleData(GenericParticleData.create(0.5f + rand.nextFloat() * 0.1f, 0).setEasing(Easing.SINE_IN_OUT, Easing.BOUNCE_IN_OUT).build())
            .setColorData(colorData)
            .setLifetime(RandomHelper.randomBetween(rand, 20, 30))
            .setRandomOffset(0.5f)
            .setRandomMotion(1f, 1f)
            .spawnOnStack(3, -1);
    }

}
