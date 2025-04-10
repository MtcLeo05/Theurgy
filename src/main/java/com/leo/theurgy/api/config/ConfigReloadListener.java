package com.leo.theurgy.api.config;

import com.leo.theurgy.impl.config.ConfigLoader;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.jetbrains.annotations.NotNull;

public class ConfigReloadListener implements ResourceManagerReloadListener {

    @SuppressWarnings("null")
    @Override
    public void onResourceManagerReload(@NotNull ResourceManager pResourceManager) {
        ConfigLoader.getInstance().load();
    }
}
