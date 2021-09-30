package io.github.vampirestudios.raa_dimension.generation.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.decorator.DecoratorConfig;

import java.util.Random;

public record BiasedNoiseBasedDecoratorConfig(int noiseToCountRatio, double noiseFactor, double noiseOffset,
                                              Heightmap.Type heightmap) implements DecoratorConfig {
    public static final Codec<BiasedNoiseBasedDecoratorConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codec.INT.fieldOf("noiseToCountRatio").forGetter((countExtraDecoratorConfig) -> {
            return countExtraDecoratorConfig.noiseToCountRatio;
        }), Codec.DOUBLE.fieldOf("noiseFactor").forGetter((countExtraDecoratorConfig) -> {
            return countExtraDecoratorConfig.noiseFactor;
        }), Codec.DOUBLE.fieldOf("noiseOffset").forGetter((countExtraDecoratorConfig) -> {
            return countExtraDecoratorConfig.noiseOffset;
        }), Heightmap.Type.CODEC.fieldOf("heightmap").forGetter((countExtraDecoratorConfig) -> {
            return countExtraDecoratorConfig.heightmap;
        })).apply(instance, BiasedNoiseBasedDecoratorConfig::new);
    });

    public static BiasedNoiseBasedDecoratorConfig create(Random random) {
        return new BiasedNoiseBasedDecoratorConfig(10, 80.0D, 0.0D, Heightmap.Type.OCEAN_FLOOR_WG);
    }
}
