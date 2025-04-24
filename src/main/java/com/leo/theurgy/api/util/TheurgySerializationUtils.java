package com.leo.theurgy.api.util;

import com.mojang.datafixers.util.Function10;
import com.mojang.datafixers.util.Function7;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public class TheurgySerializationUtils {

    public static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> composite(
        StreamCodec<? super B, T1> codec1,
        Function<C, T1> getter1,
        StreamCodec<? super B, T2> codec2,
        Function<C, T2> getter2,
        StreamCodec<? super B, T3> codec3,
        Function<C, T3> getter3,
        StreamCodec<? super B, T4> codec4,
        Function<C, T4> getter4,
        StreamCodec<? super B, T5> codec5,
        Function<C, T5> getter5,
        StreamCodec<? super B, T6> codec6,
        Function<C, T6> getter6,
        StreamCodec<? super B, T7> codec7,
        Function<C, T7> getter7,
        Function7<T1, T2, T3, T4, T5, T6, T7, C> factory
    ) {
        return new StreamCodec<>() {
            @Override
            public C decode(B buf) {
                T1 t1 = codec1.decode(buf);
                T2 t2 = codec2.decode(buf);
                T3 t3 = codec3.decode(buf);
                T4 t4 = codec4.decode(buf);
                T5 t5 = codec5.decode(buf);
                T6 t6 = codec6.decode(buf);
                T7 t7 = codec7.decode(buf);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7);
            }

            @Override
            public void encode(B buf, C object) {
                codec1.encode(buf, getter1.apply(object));
                codec2.encode(buf, getter2.apply(object));
                codec3.encode(buf, getter3.apply(object));
                codec4.encode(buf, getter4.apply(object));
                codec5.encode(buf, getter5.apply(object));
                codec6.encode(buf, getter6.apply(object));
                codec7.encode(buf, getter7.apply(object));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> StreamCodec<B, C> composite(
        StreamCodec<? super B, T1> codec1,
        Function<C, T1> getter1,
        StreamCodec<? super B, T2> codec2,
        Function<C, T2> getter2,
        StreamCodec<? super B, T3> codec3,
        Function<C, T3> getter3,
        StreamCodec<? super B, T4> codec4,
        Function<C, T4> getter4,
        StreamCodec<? super B, T5> codec5,
        Function<C, T5> getter5,
        StreamCodec<? super B, T6> codec6,
        Function<C, T6> getter6,
        StreamCodec<? super B, T7> codec7,
        Function<C, T7> getter7,
        StreamCodec<? super B, T8> codec8,
        Function<C, T8> getter8,
        StreamCodec<? super B, T9> codec9,
        Function<C, T9> getter9,
        StreamCodec<? super B, T10> codec10,
        Function<C, T10> getter10,
        Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, C> factory
    ) {
        return new StreamCodec<>() {
            @Override
            public C decode(B buf) {
                T1 t1 = codec1.decode(buf);
                T2 t2 = codec2.decode(buf);
                T3 t3 = codec3.decode(buf);
                T4 t4 = codec4.decode(buf);
                T5 t5 = codec5.decode(buf);
                T6 t6 = codec6.decode(buf);
                T7 t7 = codec7.decode(buf);
                T8 t8 = codec8.decode(buf);
                T9 t9 = codec9.decode(buf);
                T10 t10 = codec10.decode(buf);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
            }

            @Override
            public void encode(B buf, C object) {
                codec1.encode(buf, getter1.apply(object));
                codec2.encode(buf, getter2.apply(object));
                codec3.encode(buf, getter3.apply(object));
                codec4.encode(buf, getter4.apply(object));
                codec5.encode(buf, getter5.apply(object));
                codec6.encode(buf, getter6.apply(object));
                codec7.encode(buf, getter7.apply(object));
                codec8.encode(buf, getter8.apply(object));
                codec9.encode(buf, getter9.apply(object));
                codec10.encode(buf, getter10.apply(object));
            }
        };
    }
}
