package io.github.vampirestudios.raa_dimension.generation.chunkgenerator;

import io.github.vampirestudios.raa_dimension.utils.ColoredBlockArrays;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class CheckerboardChunkGenerator extends NoiseChunkGenerator {
    public CheckerboardChunkGenerator(BiomeSource biomeSource, long worldSeed, Supplier<ChunkGeneratorSettings> config) {
        super(biomeSource, worldSeed, config);
    }

    @Override
    public void buildSurface(ChunkRegion region, Chunk chunk) {

    }

    private static int method_26528(int i) {
        return i & Integer.MAX_VALUE;
    }

    @Override
    public int getSpawnHeight(HeightLimitView world) {
        return 100;
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(Executor executor, StructureAccessor accessor, Chunk chunk) {
        ChunkPos chunkPos = chunk.getPos();

        for (int i = 0; i < 8; ++i) {
            int j = method_26528(chunkPos.x) ^ i ^ method_26528(chunkPos.z);
            Block[] blocks = ColoredBlockArrays.ALL[j % ColoredBlockArrays.ALL.length];
            BlockPos.Mutable mutable = new BlockPos.Mutable();

            for (int k = 0; k < 16; ++k) {
                for (int l = 0; l < 16; ++l) {
                    for (int m = 0; m < 16; ++m) {
                        int n = 16 * i + l;
                        int o = k ^ n ^ m;
                        chunk.setBlockState(mutable.set(k, n, m), blocks[o % blocks.length].getDefaultState(), false);
                    }
                }
            }
        }

        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world) {
        return 100;
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world) {
        int i = Math.max(0, world.getBottomY());
        return new VerticalBlockSample(i, new BlockState[0]);
    }

}