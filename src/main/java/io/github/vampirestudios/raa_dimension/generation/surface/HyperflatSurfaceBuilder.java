package io.github.vampirestudios.raa_dimension.generation.surface;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import java.util.Random;
import java.util.stream.IntStream;

public class HyperflatSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
    public static OctaveSimplexNoiseSampler HEIGHT = new OctaveSimplexNoiseSampler(new ChunkRandom(79), IntStream.of(4, 0));
    public static OctaveSimplexNoiseSampler WATER_NOISE = new OctaveSimplexNoiseSampler(new ChunkRandom(7979), IntStream.of(4, 0));

    public HyperflatSurfaceBuilder(Codec<TernarySurfaceConfig> ternarySurfaceConfigCodec) {
        super(ternarySurfaceConfigCodec);
    }

    @Override
    public void generate(Random rand, Chunk chunk, Biome biome, int x, int z, int height, double noise, BlockState stone, BlockState water, int var11, long seed, TernarySurfaceConfig config) {
        BlockPos.Mutable delPos = new BlockPos.Mutable(x, height, z);
        for (int i = 0; i < height; i++) {
            chunk.setBlockState(delPos, Blocks.AIR.getDefaultState(), false);
            delPos.offset(Direction.DOWN);
        }

        double noiseHeight = HEIGHT.sample(x * 0.05, z * 0.05, false);
        BlockPos.Mutable pos = new BlockPos.Mutable(x, 0, z);
        for (int i = 0; i < 80 + (noiseHeight * 8); i++) {
            chunk.setBlockState(pos, stone, false);
            pos.offset(Direction.UP);
        }
        for (int i = 0; i < 3; i++) {
            chunk.setBlockState(pos, Blocks.DIRT.getDefaultState(), false);
            pos.offset(Direction.UP);
        }
        if (noiseHeight > 0) {
            chunk.setBlockState(pos.add(0, -1, 0), config.getTopMaterial(), false);
            if (pos.getY() == 84) chunk.setBlockState(pos.add(0, -1, 0), Blocks.SAND.getDefaultState(), false);
        } else {
            chunk.setBlockState(pos.add(0, -1, 0), WATER_NOISE.sample(x * 0.05, z * 0.05, false) > 0.2 ? Blocks.GRAVEL.getDefaultState() : Blocks.SAND.getDefaultState(), false);
            while (pos.getY() < 84) {
                chunk.setBlockState(pos, water, false);
                pos.offset(Direction.UP);
            }
        }
    }
}
