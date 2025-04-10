package com.leo.theurgy.impl.init;

import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.block.entity.TheurgistsBenchBE;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TheurgyBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TheurgyConstants.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TheurgistsBenchBE>> THEURGISTS_BENCH_BE = BLOCK_ENTITIES.register("theurgists_bench",
        () -> BlockEntityType.Builder.of(
            TheurgistsBenchBE::new,
            TheurgyBlocks.THEURGISTS_BENCH.get()
        ).build(null)
    );

}
