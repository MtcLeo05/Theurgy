package com.leo.theurgy.impl.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.leo.theurgy.api.config.ChunkMionConfig;
import com.leo.theurgy.api.util.MapUtil;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.fml.loading.FMLPaths;

import java.io.*;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

public class ConfigLoader {
    public static final String CONFIG_FILE = "theurgy.json5";
    private static ConfigLoader INSTANCE = new ConfigLoader();

    private ConfigLoader() {
    }

    public static ConfigLoader getInstance() {
        return INSTANCE != null ? INSTANCE : new ConfigLoader();
    }

    @Expose
    @SerializedName("chunk_mion")
    private final Map<String, ChunkMionConfig> CHUNK_MION = MapUtil.of(
        MapUtil.createEntry("theurgy:magical_forest", new ChunkMionConfig(200, 0.25f, 80))
    );

    @Expose
    @SerializedName("give_book")
    public final boolean giveBook = true;

    @Expose
    @SerializedName("debug")
    public final boolean debug = true;

    public void load() {
        Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .create();

        Path configPath = FMLPaths.CONFIGDIR.get().resolve(CONFIG_FILE);
        File file = configPath.toFile();

        try {
            if (!file.exists()) {
                System.out.println("Configuration file does not exist. Creating a new one.");
                saveDefaultConfig(file, gson);
            } else {
                try (JsonReader jsonReader = new JsonReader(new FileReader(file))) {
                    INSTANCE = gson.fromJson(jsonReader, ConfigLoader.class);
                    if (INSTANCE == null) {
                        throw new JsonSyntaxException("Parsed configuration is null.");
                    }
                }
            }
        } catch (JsonSyntaxException | IOException e) {
            System.err.println("Invalid configuration file. Regenerating default config.");
            saveDefaultConfig(file, gson);
        }
    }

    private void saveDefaultConfig(File file, Gson gson) {
        try (FileWriter writer = new FileWriter(file)) {
            if (INSTANCE == null) INSTANCE = new ConfigLoader();

            gson.toJson(INSTANCE, ConfigLoader.class, writer);
            System.out.println("Default configuration file created successfully.");
        } catch (IOException e) {
            throw new RuntimeException("Failed to create default configuration file.", e);
        }
    }

    public ChunkMionConfig getMion(ResourceKey<Biome> biome) {
        String biomeId = biome.location().toString();

        return CHUNK_MION.getOrDefault(biomeId, null);
    }

    public ChunkMionConfig getTagMion(Holder<Biome> biome) {
        Stream<TagKey<Biome>> tags = biome.tags();

        for (TagKey<Biome> tag : tags.toList()) {
            String tagId = "#" + tag.location();
            return CHUNK_MION.getOrDefault(tagId, null);
        }

        return null;
    }
}
