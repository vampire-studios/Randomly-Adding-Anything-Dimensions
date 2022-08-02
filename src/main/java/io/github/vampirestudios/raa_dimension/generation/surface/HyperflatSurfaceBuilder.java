package io.github.vampirestudios.raa_dimension.generation.surface;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.SurfaceSystem;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import java.util.Random;
import java.util.stream.IntStream;

public class HyperflatSurfaceBuilder extends SurfaceSystem<TernarySurfaceConfig> {
    public static PerlinSimplexNoise HEIGHT = new PerlinSimplexNoise(new ChunkRandom(79), IntStream.of(4, 0));
    public static PerlinSimplexNoise WATER_NOISE = new PerlinSimplexNoise(new ChunkRandom(7979), IntStream.of(4, 0));

    public HyperflatSurfaceBuilder(Codec<TernarySurfaceConfig> ternarySurfaceConfigCodec) {
        super(ternarySurfaceConfigCodec);
    }

    @Override
    public void generate(Random random, ChunkAccess chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int unknownValue, long seed, TernarySurfaceConfig surfaceConfig) {
        BlockPos.MutableBlockPos delPos = new BlockPos.MutableBlockPos(x, height, z);
        for (int i = 0; i < height; i++) {
            chunk.setBlockState(delPos, Blocks.AIR.defaultBlockState(), false);
            delPos.relative(Direction.DOWN);
        }

        double noiseHeight = HEIGHT.getValue(x * 0.05, z * 0.05, false);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, 0, z);
        for (int i = 0; i < 80 + (noiseHeight * 8); i++) {
            chunk.setBlockState(pos, defaultBlock, false);
            pos.relative(Direction.UP);
        }
        for (int i = 0; i < 3; i++) {
            chunk.setBlockState(pos, Blocks.DIRT.defaultBlockState(), false);
            pos.relative(Direction.UP);
        }
        if (noiseHeight > 0) {
            chunk.setBlockState(pos.offset(0, -1, 0), surfaceConfig.getTopMaterial(), false);
            if (pos.getY() == 84) chunk.setBlockState(pos.offset(0, -1, 0), Blocks.SAND.defaultBlockState(), false);
        } else {
            chunk.setBlockState(pos.offset(0, -1, 0), WATER_NOISE.getValue(x * 0.05, z * 0.05, false) > 0.2 ? Blocks.GRAVEL.defaultBlockState() : Blocks.SAND.defaultBlockState(), false);
            while (pos.getY() < 84) {
                chunk.setBlockState(pos, defaultBlock, false);
                pos.relative(Direction.UP);
            }
        }
    }
}
