package com.leo.theurgy.api.data.aspectus;

import com.leo.theurgy.impl.TheurgyConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record AspectusHolder(ResourceLocation aspectusId, int amount) {

    public static Codec<AspectusHolder> CODEC = RecordCodecBuilder.create(
        inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("aspectus").forGetter(AspectusHolder::aspectusId),
            Codec.INT.fieldOf("amount").forGetter(AspectusHolder::amount)
        ).apply(inst, AspectusHolder::new)
    );

    public static StreamCodec<FriendlyByteBuf, AspectusHolder> STREAM_CODEC = StreamCodec.composite(
        ResourceLocation.STREAM_CODEC,
        AspectusHolder::aspectusId,
        ByteBufCodecs.INT,
        AspectusHolder::amount,
        AspectusHolder::new
    );

    public Optional<Aspectus> aspectus(RegistryAccess registry) {
        Registry<Aspectus> aspectusRegistry = registry.registryOrThrow(TheurgyConstants.ASPECTUS_REGISTRY_KEY);

        return aspectusRegistry.getOptional(ResourceKey.create(TheurgyConstants.ASPECTUS_REGISTRY_KEY, aspectusId()));
    }
}
