package com.leo.theurgy.api.event;

import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.Event;

public class ChunkTickEvent extends Event {
    private final LevelChunk chunk;

    public ChunkTickEvent(LevelChunk chunk) {
        this.chunk = chunk;
    }

    public LevelChunk chunk() {
        return chunk;
    }
}
