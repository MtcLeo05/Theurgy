package com.leo.theurgy.api.client;

import com.leo.theurgy.impl.TheurgyConstants;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;

public class AspectusAtlasHolder extends TextureAtlasHolder {
    public AspectusAtlasHolder(TextureManager textureManager) {
        super(textureManager, TheurgyConstants.modLoc("textures/atlas/aspectus.png"), TheurgyConstants.modLoc("aspectus"));
    }

    @Override
    public TextureAtlasSprite getSprite(ResourceLocation location) {
        return super.getSprite(location);
    }
}
