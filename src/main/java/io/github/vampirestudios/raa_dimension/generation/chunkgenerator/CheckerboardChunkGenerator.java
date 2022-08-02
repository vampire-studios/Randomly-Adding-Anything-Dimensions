package io.github.vampirestudios.raa_dimension.generation.chunkgenerator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.vampirestudios.raa_dimension.utils.ColoredBlockArrays;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.*;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;

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
    public void buildSurface(WorldGenRegion region, ChunkAccess chunk) {

    }

    private static int method_26528(int i) {
        return i & Integer.MAX_VALUE;
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
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
    public void populateNoise(LevelAccessor world, StructureManager StructureAccessor, ChunkAccess chunk) {
        ChunkPos chunkPos = chunk.getPos();

        for (int i = 0; i < 8; ++i) {
            int j = method_26528(chunkPos.x) ^ i ^ method_26528(chunkPos.z);
            Block[] blocks = ColoredBlockArrays.ALL[j % ColoredBlockArrays.ALL.length];
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

            for (int k = 0; k < 16; ++k) {
                for (int l = 0; l < 16; ++l) {
                    for (int m = 0; m < 16; ++m) {
                        int n = 16 * i + l;
                        int o = k ^ n ^ m;
                        chunk.setBlockState(mutable.set(k, n, m), blocks[o % blocks.length].defaultBlockState(), false);
                    }
                }
            }
        }

    }

    @Override
    public int getHeight(int x, int z, Heightmap.Types heightmapType) {
        return 100;
    }

    @Override
    public BlockGetter getColumnSample(int x, int z) {
        return EmptyBlockGetter.INSTANCE;
    }

}