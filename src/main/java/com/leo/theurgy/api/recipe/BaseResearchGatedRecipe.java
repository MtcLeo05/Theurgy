package com.leo.theurgy.api.recipe;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface BaseResearchGatedRecipe {

    List<ResourceLocation> neededResearch();

}
