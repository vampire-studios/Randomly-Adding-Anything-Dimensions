/*
package io.github.vampirestudios.raa_dimension;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.vampirestudios.raa.RandomlyAddingAnything;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.BiomeSourceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DimensionalBiomeSource extends BiomeSource {
    private static List<Biome> BIOMES;
    private final BiomeLayerSampler noiseLayer;
    private final long seed;
    public static final Codec<DimensionalBiomeSource> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codec.LONG.fieldOf("seed").stable().forGetter((vanillaLayeredBiomeSource) -> {
            return vanillaLayeredBiomeSource.seed;
        })).apply(instance, instance.stable(DimensionalBiomeSource::new));
    });

    public DimensionalBiomeSource(long seed) {
        super(BIOMES);
        this.seed = seed;
        BIOMES = biomes;
        Set<Biome> biomes1 = new java.util.HashSet<>(biomes);
        this.noiseLayer = DimensionalBiomeLayers.build(seed, biomes1);
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.noiseLayer.sample(biomeX, biomeZ);
    }

    @Override
    protected Codec<? extends BiomeSource> method_28442() {
        return null;
    }

    @Override
    public BiomeSource withSeed(long seed) {
        return RAADimensionAddon.DIMENSIONAL_BIOMES;
    }

    @Override
    public List<Biome> getSpawnBiomes() {
        return new ArrayList<>(BIOMES);
    }

}
*/
