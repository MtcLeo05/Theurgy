package com.leo.theurgy.api.data.aspectus;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record Aspectus(String key, int color) {

    public static Codec<Aspectus> CODEC = RecordCodecBuilder.create(
        inst -> inst.group(
            Codec.STRING.fieldOf("key").forGetter(Aspectus::key),
            Codec.INT.fieldOf("color").forGetter(Aspectus::color)
        ).apply(inst, Aspectus::new)
    );

    public static StreamCodec<FriendlyByteBuf, Aspectus> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8,
        Aspectus::key,
        ByteBufCodecs.INT,
        Aspectus::color,
        Aspectus::new
    );

    public String translationKey() {
        return "aspectus." + key;
    }

}
