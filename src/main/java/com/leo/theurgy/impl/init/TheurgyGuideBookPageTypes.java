package com.leo.theurgy.impl.init;

import com.leo.theurgy.api.guidebook.data.GuideBookPage;
import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.guidebook.data.CompositeGuideBookPage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Function;

public class TheurgyGuideBookPageTypes {

    public static final Registry<MapCodec<? extends GuideBookPage>> GUIDEBOOK_PAGE_TYPES_REGISTRY = new RegistryBuilder<>(TheurgyConstants.GUIDEBOOK_PAGE_RENDERABLE_TYPES_REGISTRY_KEY)
        .sync(true)
        .defaultKey(TheurgyConstants.modLoc("null_guide_book_page"))
        .create();

    public static final DeferredRegister<MapCodec<? extends GuideBookPage>> GUIDEBOOK_PAGE_TYPES_REGISTER = DeferredRegister.create(GUIDEBOOK_PAGE_TYPES_REGISTRY, TheurgyConstants.MODID);

    public static final Codec<GuideBookPage> GUIDEBOOK_RENDERABLE_TYPE_CODEC = GUIDEBOOK_PAGE_TYPES_REGISTRY.byNameCodec().dispatch(
        GuideBookPage::getCodec,
        Function.identity()
    );

    public static final DeferredHolder<MapCodec<? extends GuideBookPage>, MapCodec<CompositeGuideBookPage>> ITEM = GUIDEBOOK_PAGE_TYPES_REGISTER.register("composite",
        () -> CompositeGuideBookPage.CODEC
    );
}
