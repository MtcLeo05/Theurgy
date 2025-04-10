package com.leo.theurgy.api.config;

import com.google.gson.annotations.Expose;

public record ChunkMionConfig(@Expose int maxMion, @Expose float variance, @Expose int mionRegenCD) {
}
