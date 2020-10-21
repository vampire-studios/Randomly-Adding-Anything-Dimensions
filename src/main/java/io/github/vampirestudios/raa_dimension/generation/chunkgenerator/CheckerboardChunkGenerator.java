package io.github.vampirestudios.raa_dimension.generation.chunkgenerator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.vampirestudios.raa_dimension.utils.ColoredBlockArrays;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.*;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;

public class CheckerboardChunkGenerator extends ChunkGenerator {
    public static final Codec<CheckerboardChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(BiomeSource.CODEC.fieldOf("biomeSource").forGetter((noiseChunkGenerator) -> {
            return noiseChunkGenerator.biomeSource;
        }), StructuresConfig.CODEC.fieldOf("config").forGetter((noiseChunkGenerator) -> {
            return noiseChunkGenerator.structuresConfig;
        }), Codec.LONG.fieldOf("worldSeed").stable().forGetter((noiseChunkGenerator) -> {
            return noiseChunkGenerator.worldSeed;
        })).apply(instance, instance.stable(CheckerboardChunkGenerator::new));
    });

    public CheckerboardChunkGenerator(BiomeSource biomeSource, StructuresConfig config, long worldSeed) {
        super(biomeSource, biomeSource, config, worldSeed);
    }

    @Override
    public void buildSurface(ChunkRegion region, Chunk chunk) {

    }

    private static int method_26528(int i) {
        return i & Integer.MAX_VALUE;
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return new CheckerboardChunkGenerator(this.biomeSource.withSeed(seed), this.getStructuresConfig(), seed);
    }

    @Override
    public int getSpawnHeight() {
        return 100;
    }

    @Override
    public void populateNoise(WorldAccess world, StructureAccessor StructureAccessor, Chunk chunk) {
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

    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmapType) {
        return 100;
    }

    @Override
    public BlockView getColumnSample(int x, int z) {
        return EmptyBlockView.INSTANCE;
    }

}