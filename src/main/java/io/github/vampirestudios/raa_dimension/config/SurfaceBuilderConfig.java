package io.github.vampirestudios.raa_dimension.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.raa_core.config.RAADataConfig;
import io.github.vampirestudios.raa_core.helpers.GsonHelper;
import io.github.vampirestudios.raa_dimension.generation.surface.random.SurfaceBuilderGenerator;

import java.io.FileWriter;

public class SurfaceBuilderConfig extends RAADataConfig {
    public SurfaceBuilderConfig(String fileName) {
        super(fileName);
    }

    @Override
    public void generate() {
        SurfaceBuilderGenerator.generate();
    }

    @Override
    protected JsonObject upgrade(JsonObject json, int version) {
        return null;
    }

    @Override
    protected void load(JsonObject jsonObject) {
        SurfaceBuilderGenerator.load(jsonObject);
    }

    @Override
    protected void save(FileWriter fileWriter) {
        JsonObject main = new JsonObject();
        main.addProperty("configVersion", 2);
        SurfaceBuilderGenerator.save(main);
        GsonHelper.getGson().toJson(main, fileWriter);
    }
}
