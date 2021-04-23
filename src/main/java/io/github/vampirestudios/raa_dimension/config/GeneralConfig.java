package io.github.vampirestudios.raa_dimension.config;

import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = RAADimensionAddon.MOD_ID)
public class GeneralConfig implements ConfigData {

    @Comment("Amount of dimensions to generate")
    public int dimensionGenAmount = 50;

    @Comment("Should portal hubs spawn?")
    public boolean shouldSpawnPortalHub = true;

}