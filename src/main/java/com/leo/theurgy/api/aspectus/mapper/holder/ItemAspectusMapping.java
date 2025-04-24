package com.leo.theurgy.api.aspectus.mapper.holder;

import com.leo.theurgy.api.aspectus.IAspectusHolderContext;
import com.leo.theurgy.api.data.aspectus.Aspectus;
import com.leo.theurgy.impl.TheurgyConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public record ItemAspectusMapping(Map<ResourceLocation, Integer> aspectuses, ItemStack item) implements IAspectusMappingHolder {
    public static MapCodec<ItemAspectusMapping> CODEC = RecordCodecBuilder.mapCodec(
        inst -> inst.group(
            Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("aspectuses").forGetter(ItemAspectusMapping::aspectuses),
            ItemStack.CODEC.fieldOf("item").forGetter(ItemAspectusMapping::item)
        ).apply(inst, ItemAspectusMapping::new)
    );

    public static StreamCodec<RegistryFriendlyByteBuf, ItemAspectusMapping> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.map(
            HashMap::new,
            ResourceLocation.STREAM_CODEC,
            ByteBufCodecs.INT
        ),
        ItemAspectusMapping::aspectuses,
        ItemStack.STREAM_CODEC,
        ItemAspectusMapping::item,
        ItemAspectusMapping::new
    );

    @Override
    public MapCodec<? extends IAspectusMappingHolder> getCodec() {
        return CODEC;
    }

    @Override
    public Map<Aspectus, Integer> aspectuses(IAspectusHolderContext context) {
        Map<Aspectus, Integer> toRet = new HashMap<>();

        ItemStack item = context.stack();

        if(item == null) return toRet;
        if(!ItemStack.isSameItemSameComponents(item, this.item)) return toRet;
        if(context.level() == null) return toRet;

        Registry<Aspectus> aspectusRegistry = context.level().registryAccess().registryOrThrow(TheurgyConstants.ASPECTUS_REGISTRY_KEY);

        aspectuses.forEach((aspectusId, amount) -> {
            Aspectus aspectus = aspectusRegistry.get(aspectusId);
            toRet.put(aspectus, amount * item.getCount());
        });

        return toRet;
    }
}
