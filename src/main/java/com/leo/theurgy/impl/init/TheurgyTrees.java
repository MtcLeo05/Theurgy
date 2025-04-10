package com.leo.theurgy.impl.init;

import com.leo.theurgy.impl.world.TheurgyConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class TheurgyTrees {

    public static final TreeGrower GREATWOOD_TREE_GROWER = new TreeGrower(
        "ebony_tree",
        Optional.empty(),
        Optional.of(TheurgyConfiguredFeatures.GREATWOOD_TREE),
        Optional.empty()
    );
}
