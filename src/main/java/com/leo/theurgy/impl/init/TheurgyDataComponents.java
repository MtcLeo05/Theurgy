package com.leo.theurgy.impl.init;

import com.leo.theurgy.api.data.aspectus.AspectusHolder;
import com.leo.theurgy.impl.TheurgyConstants;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TheurgyDataComponents {

    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, TheurgyConstants.MODID);

    public static final Supplier<DataComponentType<AspectusHolder>> ASPECTUS_HOLDER = DATA_COMPONENTS.registerComponentType(
        "aspectus_holder",
        builder -> builder
            // The codec to read/write the data to disk
            .persistent(AspectusHolder.CODEC)
            // The codec to read/write the data across the network
            .networkSynchronized(AspectusHolder.STREAM_CODEC)
    );

}
