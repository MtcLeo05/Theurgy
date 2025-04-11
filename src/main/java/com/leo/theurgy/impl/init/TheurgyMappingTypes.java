package com.leo.theurgy.impl.init;

import com.leo.theurgy.api.aspectus.mapper.holder.EntityAspectusMapping;
import com.leo.theurgy.api.aspectus.mapper.holder.IAspectusMappingHolder;
import com.leo.theurgy.api.aspectus.mapper.holder.ItemAspectusMapping;
import com.leo.theurgy.impl.TheurgyConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Function;

public class TheurgyMappingTypes {

    public static final Registry<MapCodec<? extends IAspectusMappingHolder>> MAPPING_TYPES_REGISTRY = new RegistryBuilder<>(TheurgyConstants.MAPPING_TYPE_REGISTRY_KEY)
        .sync(true)
        .defaultKey(TheurgyConstants.modLoc("null_mapping"))
        .create();

    public static final DeferredRegister<MapCodec<? extends IAspectusMappingHolder>> MAPPING_TYPES_REGISTER = DeferredRegister.create(MAPPING_TYPES_REGISTRY, TheurgyConstants.MODID);

    public static final Codec<IAspectusMappingHolder> MAPPING_TYPE_CODEC = MAPPING_TYPES_REGISTRY.byNameCodec().dispatch(
        IAspectusMappingHolder::getCodec,
        Function.identity()
    );

    public static final DeferredHolder<MapCodec<? extends IAspectusMappingHolder>, MapCodec<ItemAspectusMapping>> ITEM = MAPPING_TYPES_REGISTER.register("item",
        () -> ItemAspectusMapping.CODEC
    );

    public static final DeferredHolder<MapCodec<? extends IAspectusMappingHolder>, MapCodec<EntityAspectusMapping>> ENTITY = MAPPING_TYPES_REGISTER.register("entity",
        () -> EntityAspectusMapping.CODEC
    );
}
