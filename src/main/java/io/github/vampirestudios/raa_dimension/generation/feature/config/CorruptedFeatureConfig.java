package io.github.vampirestudios.raa_dimension.generation.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.FeatureConfig;

public class CorruptedFeatureConfig implements FeatureConfig {
    public boolean corrupted = false;
    public static final Codec<CorruptedFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codec.BOOL.fieldOf("corrupted").forGetter((chanceAndTypeConfig) -> {
            return chanceAndTypeConfig.corrupted;
        })).apply(instance, CorruptedFeatureConfig::new);
    });

    public CorruptedFeatureConfig(boolean corrupted) {
        this.corrupted = corrupted;
    }
}
