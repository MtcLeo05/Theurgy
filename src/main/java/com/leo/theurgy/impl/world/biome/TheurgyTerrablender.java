package com.leo.theurgy.impl.world.biome;

import com.leo.theurgy.impl.TheurgyConstants;
import terrablender.api.Regions;

public class TheurgyTerrablender {

    public static void registerBiomes(){
        Regions.register(
            new TheurgyOverworldRegion(
                TheurgyConstants.modLoc("magical_forest"),
                10
            )
        );
    }

}
