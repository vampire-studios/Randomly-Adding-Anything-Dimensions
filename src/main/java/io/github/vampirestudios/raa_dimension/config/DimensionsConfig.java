package io.github.vampirestudios.raa_dimension.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.raa_core.config.RAADataConfig;
import io.github.vampirestudios.raa_core.helpers.GsonHelper;
import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import io.github.vampirestudios.raa_dimension.init.Dimensions;
import java.io.FileWriter;
import java.util.Arrays;
import net.minecraft.core.Registry;

public class DimensionsConfig extends RAADataConfig {
    public DimensionsConfig(String fileName) {
        super(fileName);
    }

    @Override
    public void generate() {
        Dimensions.generate();
    }

    @Override
    protected JsonObject upgrade(JsonObject json, int version) {
        JsonArray dimensions = net.minecraft.util.GsonHelper.getAsJsonArray(json, "dimensions");

        switch (version) {
            case 0:
                break;
            case 1:
                iterateArrayObjects(dimensions, dimension -> {
                    if (!net.minecraft.util.GsonHelper.isStringValue(dimension.get("id")))
                        dimension.addProperty("id", GsonHelper.idFromOldStyle(dimension.getAsJsonObject("id")).toString());
                    if (dimension.has("dimensionColorPallet")) {
                        dimension.add("dimensionColorPalette", dimension.getAsJsonObject("dimensionColorPallet"));
                    }
                    dimension.add("hasSkyLight", dimension.get("hasLight"));

                    JsonObject biomeData = dimension.getAsJsonObject("biomeData");
                    if (!net.minecraft.util.GsonHelper.isStringValue(biomeData.get("id")))
                        biomeData.addProperty("id", GsonHelper.idFromOldStyle(biomeData.getAsJsonObject("id")).toString());

                    /*if (!dimension.has("dimensionChunkGenerator")) {
                        dimension.addProperty("dimensionChunkGenerator", Utils.randomCG(100).name());
                    }*/
                });
                break;
        }

        return json;
    }

    @Override
    protected void load(JsonObject jsonObject) {
        DimensionData[] dimensionsData = GsonHelper.getGson().fromJson(net.minecraft.util.GsonHelper.getAsJsonArray(jsonObject, "dimensions"), DimensionData[].class);
        Arrays.stream(dimensionsData).forEach(dimensionData -> Registry.register(Dimensions.DIMENSIONS, dimensionData.getId(), dimensionData));
    }

    @Override
    protected void save(FileWriter fileWriter) {
        JsonObject main = new JsonObject();
        main.add("dimensions", GsonHelper.getGson().toJsonTree(Dimensions.DIMENSIONS.stream().toArray(DimensionData[]::new)));
        main.addProperty("configVersion", CURRENT_VERSION);
        GsonHelper.getGson().toJson(main, fileWriter);
    }
}
