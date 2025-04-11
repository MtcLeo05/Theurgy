package com.leo.theurgy.api.aspectus.mapper.holder;

import com.leo.theurgy.api.aspectus.IAspectusHolderContext;
import com.leo.theurgy.api.data.aspectus.Aspectus;
import com.mojang.serialization.MapCodec;

import java.util.Map;

public interface IAspectusMappingHolder {

    MapCodec<? extends IAspectusMappingHolder> getCodec();

    Map<Aspectus, Integer> aspectuses(IAspectusHolderContext context);
}
