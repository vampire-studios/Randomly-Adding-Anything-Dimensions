package io.github.vampirestudios.raa_dimension.config;

import blue.endless.jankson.Comment;
import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = RAADimensionAddon.MOD_ID)
public class GeneralConfig implements ConfigData {

    @Comment("Amount of dimensions to generate")
    public int dimensionGenAmount = 50;

    @Comment("Should portal hubs spawn?")
    public boolean shouldSpawnPortalHub = true;

    @Comment("Amount of surface builders to generate")
    public int surfaceBuilderGenAmount = 50;

    @Comment("Amount of different surface builders to combine together for one surface builder. Example: \"3-6\" will combine between 3 and 6 surface builders into one")
    public String surfaceBuilderSubAmount = "3-6";

    @Comment("Amount of biomes to generate per raa-dimension")
    public int dimensionBiomeGenAmount = 10;

}