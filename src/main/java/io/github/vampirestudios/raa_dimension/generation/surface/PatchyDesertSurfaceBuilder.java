package io.github.vampirestudios.raa_dimension.generation.surface;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import java.util.Random;
import java.util.stream.IntStream;

public class PatchyDesertSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
    public static final OctaveSimplexNoiseSampler DESERT_NOISE = new OctaveSimplexNoiseSampler(new ChunkRandom(79L), IntStream.of(6, 0));

    public PatchyDesertSurfaceBuilder(Codec<TernarySurfaceConfig> ternarySurfaceConfigCodec) {
        super(ternarySurfaceConfigCodec);
    }

    @Override
    public void generate(Random random, Chunk chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int unknownValue, long seed, TernarySurfaceConfig surfaceConfig) {
        double desertNoise = DESERT_NOISE.sample(x * 0.049765625D, z * 0.049765625D, false);
        if (desertNoise > 0.0D) {
            SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, seaLevel, unknownValue, seed, SurfaceBuilder.SAND_CONFIG);
        } else {
            SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, seaLevel, unknownValue, seed, surfaceConfig);
        }
    }
}
