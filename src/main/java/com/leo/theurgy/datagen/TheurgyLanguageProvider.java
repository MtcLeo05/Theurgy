package com.leo.theurgy.datagen;

import com.leo.theurgy.impl.TheurgyConstants;
import com.leo.theurgy.impl.init.TheurgyBlocks;
import com.leo.theurgy.impl.init.TheurgyItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.stream.Stream;

public class TheurgyLanguageProvider extends LanguageProvider {

    public TheurgyLanguageProvider(PackOutput output, String locale) {
        super(output, TheurgyConstants.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        this.add(TheurgyConstants.MODID + ".itemGroup.items", "Items");
        this.add("theurgy.item.mion_crystal.empty", " Crystal");

        this.add("aspectus.theurgy.ignis", "Ignis");
        this.add("aspectus.theurgy.terra", "Terra");
        this.add("aspectus.theurgy.aqua", "Aqua");
        this.add("aspectus.theurgy.aer", "Aer");
        this.add("aspectus.theurgy.perditio", "Perditio");
        this.add("aspectus.theurgy.ordo", "Ordo");

        List<Item> toIgnore = List.of(
            TheurgyBlocks.THEURGISTS_BENCH.get().asItem(),
            TheurgyItems.MION_CRYSTAL.get()
        );

        Stream<Item> itemList = TheurgyItems.ITEMS.getEntries().stream().map(DeferredHolder::get);

        itemList.forEach((item) -> {
            if (toIgnore.contains(item)) {
                return;
            }

            this.add(item, cFL(TheurgyConstants.itemName(item).getPath()));
        });

        this.add(TheurgyItems.MION_CRYSTAL.get(), "Mion Crystal");
        this.add(TheurgyBlocks.THEURGISTS_BENCH.get(), "Theurgist's Bench");


        this.add("command." + TheurgyConstants.MODID + ".research.clear_all.success", "Successfully cleared research");
        this.add("command." + TheurgyConstants.MODID + ".guidebook.clear_all.success", "Successfully cleared guidebook progress");
        this.add("command." + TheurgyConstants.MODID + ".guidebook.unlock_all.success", "Successfully unlocked all guidebook progress");
        this.add("command." + TheurgyConstants.MODID + ".knowledge.clear_all.success", "Successfully cleared all aspectus knowledge");

        this.add("gui." + TheurgyConstants.MODID + ".theurgists_bench.title", "Theurgist's Bench");
        this.add("gui." + TheurgyConstants.MODID + ".jei.missing_research", "Missing Research: %d");
        this.add("gui." + TheurgyConstants.MODID + ".gui.guidebook", "Guide Book");
    }


    /**
     * Capitalizes first letter of a string
     *
     * @param input the string to capitalize e.g. "alpha_beta"
     * @return the string capitalized e.g. "Alpha Beta"
     */
    public static String cFL(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        String[] words = input.split("_");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase())
                    .append(" ");
            }
        }

        return result.toString().trim();
    }
}