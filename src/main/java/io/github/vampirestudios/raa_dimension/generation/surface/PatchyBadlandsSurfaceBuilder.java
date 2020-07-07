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

public class PatchyBadlandsSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
    public static final OctaveSimplexNoiseSampler MESA_NOISE = new OctaveSimplexNoiseSampler(new ChunkRandom(79L), IntStream.of(6, 0));

    public PatchyBadlandsSurfaceBuilder(Codec<TernarySurfaceConfig> ternarySurfaceConfigCodec) {
        super(ternarySurfaceConfigCodec);
    }

    @Override
    public void generate(Random rand, Chunk chunk, Biome biome, int x, int z, int height, double noise, BlockState state, BlockState state2, int int1, long long1, TernarySurfaceConfig config) {
        double mesaNoise = MESA_NOISE.sample(x * 0.049765625D, z * 0.049765625D, false);
        if (mesaNoise > 0.0D) {
            SurfaceBuilder.BADLANDS.initSeed(long1);
            SurfaceBuilder.BADLANDS.generate(rand, chunk, biome, x, z, height, noise, state, state2, int1, long1, SurfaceBuilder.BADLANDS_CONFIG);
        } else {
            SurfaceBuilder.DEFAULT.generate(rand, chunk, biome, x, z, height, noise, state, state2, int1, long1, config);
        }
    }
}