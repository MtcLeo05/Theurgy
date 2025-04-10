package com.leo.theurgy.impl.init;

import com.leo.theurgy.api.research.ResearchType;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.research.ItemResearchType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Function;

public class TheurgyResearchTypes {

    public static final Registry<MapCodec<? extends ResearchType>> RESEARCH_TYPES_REGISTRY = new RegistryBuilder<>(TheurgyConstants.RESEARCH_TYPES_REGISTRY_KEY)
        .sync(true)
        .defaultKey(TheurgyConstants.modLoc("null_research"))
        .create();

    public static final DeferredRegister<MapCodec<? extends ResearchType>> RESEARCH_TYPES_REGISTER = DeferredRegister.create(RESEARCH_TYPES_REGISTRY, TheurgyConstants.MODID);

    public static final Codec<ResearchType> RESEARCH_TYPE_CODEC = RESEARCH_TYPES_REGISTRY.byNameCodec().dispatch(
        ResearchType::getCodec,
        Function.identity()
    );

    public static final DeferredHolder<MapCodec<? extends ResearchType>, MapCodec<ItemResearchType>> ITEM = RESEARCH_TYPES_REGISTER.register("item",
        () -> ItemResearchType.CODEC
    );
}
