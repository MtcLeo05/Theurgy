package com.leo.theurgy.api.data.player;

import com.leo.theurgy.api.aspectus.IAspectusHolderContext;
import com.leo.theurgy.api.aspectus.mapper.holder.IAspectusMappingHolder;
import com.leo.theurgy.api.data.aspectus.Aspectus;
import com.leo.theurgy.api.util.ListUtil;
import com.leo.theurgy.api.util.MapUtil;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.aspectus.AspectusHolderItemContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record PlayerKnowledge(Map<ResourceLocation, Integer> knowledge, List<ResourceLocation> scannedEntries) {
    public static Codec<PlayerKnowledge> CODEC = RecordCodecBuilder.create(
        inst -> inst.group(
            Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("knowledge").forGetter(PlayerKnowledge::knowledge),
            ResourceLocation.CODEC.listOf().fieldOf("scanned_entries").forGetter(PlayerKnowledge::scannedEntries)
        ).apply(inst, PlayerKnowledge::new)
    );

    public static StreamCodec<FriendlyByteBuf, PlayerKnowledge> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.map(
            HashMap::new,
            ResourceLocation.STREAM_CODEC,
            ByteBufCodecs.INT
        ),
        PlayerKnowledge::knowledge,
        ByteBufCodecs.collection(ArrayList::new, ResourceLocation.STREAM_CODEC),
        PlayerKnowledge::scannedEntries,
        PlayerKnowledge::new
    );

    public PlayerKnowledge() {
        this(new HashMap<>(), new ArrayList<>());
    }

    public boolean knowsItem(ItemStack item) {
        return scannedEntries.contains(itemKey(item));
    }

    public boolean knowsEntity(IAspectusHolderContext context) {
        return scannedEntries.contains(entityKeyFromContext(context));
    }

    public PlayerKnowledge scanItem(IAspectusHolderContext context) {
        RegistryAccess registryAccess = context.level().registryAccess();
        Registry<IAspectusMappingHolder> mappings = registryAccess.registryOrThrow(TheurgyConstants.ASPECTUS_MAPPING_REGISTRY_KEY);

        Registry<Aspectus> aspectuses = registryAccess.registryOrThrow(TheurgyConstants.ASPECTUS_REGISTRY_KEY);

        Map<ResourceLocation, Integer> map = MapUtil.mutable(Map.copyOf(knowledge));
        List<ResourceLocation> list = ListUtil.mutable(scannedEntries);

        for (IAspectusMappingHolder mapping : mappings) {
            if(mapping.aspectuses(context).isEmpty()) continue;

            mapping.aspectuses(context).forEach((aspectus, integer) -> {
                ResourceLocation key = aspectuses.getKey(aspectus);
                if(!map.containsKey(key)) {
                    map.put(key, integer);
                    return;
                }

                map.put(key, map.get(key) + integer);
            });

            ResourceLocation blockId = itemKey(context.stack());
            list.add(blockId);
            break;
        }
        
        return new PlayerKnowledge(map, list);
    }

    private ResourceLocation itemKey(ItemStack item) {
        return BuiltInRegistries.ITEM.getKey(item.getItem());
    }

    private ResourceLocation entityKeyFromContext(IAspectusHolderContext context) {
        return BuiltInRegistries.ENTITY_TYPE.getKey(context.entity().getType());
    }

    public Map<Aspectus, Integer> getAspectus(Level level, ItemStack item) {
        RegistryAccess registryAccess = level.registryAccess();
        Registry<IAspectusMappingHolder> mappings = registryAccess.registryOrThrow(TheurgyConstants.ASPECTUS_MAPPING_REGISTRY_KEY);

        Map<Aspectus, Integer> map = new HashMap<>();

        AspectusHolderItemContext context = AspectusHolderItemContext.create(item, Minecraft.getInstance().player);

        for (IAspectusMappingHolder mapping : mappings) {
            if(mapping.aspectuses(context).isEmpty()) continue;

            mapping.aspectuses(context).forEach((aspectus, integer) -> {
                if(!map.containsKey(aspectus)) {
                    map.put(aspectus, integer);
                    return;
                }

                map.put(aspectus, map.get(aspectus) + integer);
            });

            break;
        }

        return map;
    }

    public PlayerKnowledge clear() {
        return new PlayerKnowledge();
    }
}
