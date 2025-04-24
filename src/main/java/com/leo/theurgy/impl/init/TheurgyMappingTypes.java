package com.leo.theurgy.impl.init;

import com.leo.theurgy.api.aspectus.mapper.holder.EntityAspectusMapping;
import com.leo.theurgy.api.aspectus.mapper.holder.IAspectusMappingHolder;
import com.leo.theurgy.api.aspectus.mapper.holder.ItemAspectusMapping;
import com.leo.theurgy.api.data.aspectus.Aspectus;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.aspectus.AspectusHolderItemContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TheurgyMappingTypes {

    public static Map<ResourceLocation, Integer> getAspectusFromItem(ItemStack item, Level level) {
        Map<ResourceLocation, Integer> map = new HashMap<>();

        RegistryAccess registryAccess = level.registryAccess();
        Registry<IAspectusMappingHolder> mappings = registryAccess.registryOrThrow(TheurgyConstants.ASPECTUS_MAPPING_REGISTRY_KEY);
        Registry<Aspectus> aspectuses = registryAccess.registryOrThrow(TheurgyConstants.ASPECTUS_REGISTRY_KEY);

        for (IAspectusMappingHolder mapping : mappings) {
            AspectusHolderItemContext context = AspectusHolderItemContext.create(item, level);

            if(mapping.aspectuses(context).isEmpty()) continue;

            mapping.aspectuses(context).forEach((aspectus, integer) -> {
                ResourceLocation key = aspectuses.getKey(aspectus);
                if(!map.containsKey(key)) {
                    map.put(key, integer);
                    return;
                }

                map.put(key, map.get(key) + integer);
            });
            break;
        }

        return map;
    }

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
