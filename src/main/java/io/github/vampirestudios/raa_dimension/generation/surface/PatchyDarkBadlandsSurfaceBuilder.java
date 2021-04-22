package io.github.vampirestudios.raa_dimension.generation.surface;

import com.mojang.serialization.Codec;
import io.github.vampirestudios.raa_dimension.init.SurfaceBuilders;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import java.util.Random;
import java.util.stream.IntStream;

public class PatchyDarkBadlandsSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
    public static final OctaveSimplexNoiseSampler MESA_NOISE = new OctaveSimplexNoiseSampler(new ChunkRandom(79L), IntStream.of(6, 0));

    public PatchyDarkBadlandsSurfaceBuilder(Codec<TernarySurfaceConfig> ternarySurfaceConfigCodec) {
        super(ternarySurfaceConfigCodec);
    }

    @Override
    public void generate(Random random, Chunk chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int unknownValue, long seed, TernarySurfaceConfig surfaceConfig) {
        double mesaNoise = MESA_NOISE.sample(x * 0.049765625D, z * 0.049765625D, false);
        if (mesaNoise > 0.0D) {
            SurfaceBuilders.DARK_BADLANDS.initSeed(seed);
            SurfaceBuilders.DARK_BADLANDS.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, seaLevel, unknownValue, seed, SurfaceBuilder.BADLANDS_CONFIG);
        } else {
            SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, seaLevel, unknownValue, seed, surfaceConfig);
        }
    }
}