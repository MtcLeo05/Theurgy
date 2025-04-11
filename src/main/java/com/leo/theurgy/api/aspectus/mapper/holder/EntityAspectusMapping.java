package com.leo.theurgy.api.aspectus.mapper.holder;

import com.leo.theurgy.api.aspectus.IAspectusHolderContext;
import com.leo.theurgy.api.data.aspectus.Aspectus;
import com.leo.theurgy.impl.TheurgyConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public record EntityAspectusMapping(Map<ResourceLocation, Integer> aspectuses, ResourceLocation entity) implements IAspectusMappingHolder {
    public static MapCodec<EntityAspectusMapping> CODEC = RecordCodecBuilder.mapCodec(
        inst -> inst.group(
            Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("aspectuses").forGetter(EntityAspectusMapping::aspectuses),
            ResourceLocation.CODEC.fieldOf("entity").forGetter(EntityAspectusMapping::entity)
        ).apply(inst, EntityAspectusMapping::new)
    );

    public static StreamCodec<RegistryFriendlyByteBuf, EntityAspectusMapping> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.map(
            HashMap::new,
            ResourceLocation.STREAM_CODEC,
            ByteBufCodecs.INT
        ),
        EntityAspectusMapping::aspectuses,
        ResourceLocation.STREAM_CODEC,
        EntityAspectusMapping::entity,
        EntityAspectusMapping::new
    );

    @Override
    public MapCodec<? extends IAspectusMappingHolder> getCodec() {
        return CODEC;
    }

    @Override
    public Map<Aspectus, Integer> aspectuses(IAspectusHolderContext context) {
        Map<Aspectus, Integer> toRet = new HashMap<>();

        if(context.entity() == null) return toRet;
        if(context.level() == null) return toRet;

        RegistryAccess registryAccess = context.level().registryAccess();
        Registry<Aspectus> aspectusRegistry = registryAccess.registryOrThrow(TheurgyConstants.ASPECTUS_REGISTRY_KEY);
        ResourceLocation entityId = BuiltInRegistries.ENTITY_TYPE.getKey(context.entity().getType());

        if(!entityId.equals(this.entity)) return toRet;

        aspectuses.forEach((aspectusId, amount) -> {
            Aspectus aspectus = aspectusRegistry.get(aspectusId);
            toRet.put(aspectus, amount);
        });

        return toRet;
    }
}
