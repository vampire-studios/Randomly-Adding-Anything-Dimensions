package io.github.vampirestudios.raa_dimension.generation.chunkgenerator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.*;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;

import java.util.stream.IntStream;

public class RetroChunkGenerator extends ChunkGenerator {
    public static final Codec<CheckerboardChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(BiomeSource.CODEC.fieldOf("biomeSource").forGetter((noiseChunkGenerator) -> {
            return noiseChunkGenerator.biomeSource;
        }), StructuresConfig.CODEC.fieldOf("config").forGetter((noiseChunkGenerator) -> {
            return noiseChunkGenerator.structuresConfig;
        }), Codec.LONG.fieldOf("worldSeed").stable().forGetter((noiseChunkGenerator) -> {
            return noiseChunkGenerator.worldSeed;
        })).apply(instance, instance.stable(CheckerboardChunkGenerator::new));
    });

    private final OctaveSimplexNoiseSampler noise = new OctaveSimplexNoiseSampler(new ChunkRandom(1234L), IntStream.rangeClosed(-5, 0));

    public RetroChunkGenerator(BiomeSource biomeSource, long worldSeed, StructuresConfig config) {
     super(biomeSource, biomeSource, config, worldSeed);
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return new RetroChunkGenerator(this.biomeSource.withSeed(seed), seed, this.structuresConfig);
    }

    public void buildSurface(ChunkRegion region, Chunk chunk) {

    }

    public void generateFeatures(ChunkRegion region) {

    }

    public void carve(BiomeAccess biomeAccess, Chunk chunk, GenerationStep.Carver carver) {
    }

    public int getSpawnHeight() {
     return 100;
    }

    @Override
    public void populateNoise(WorldAccess world, StructureAccessor accessor, Chunk chunk) {
         BlockState blockState = Blocks.BLACK_CONCRETE.getDefaultState();
         BlockState blockState2 = Blocks.LIME_CONCRETE.getDefaultState();
         ChunkPos chunkPos = chunk.getPos();
         BlockPos.Mutable mutable = new BlockPos.Mutable();

         for(int i = 0; i < 16; ++i) {
            for(int j = 0; j < 16; ++j) {
               double d = 64.0D + (this.noise.sample((float)chunkPos.x + (float)i / 16.0F, (float)chunkPos.z + (float)j / 16.0F, false) + 1.0D) * 20.0D;

               for(int k = 0; (double)k < d; ++k) {
                  chunk.setBlockState(mutable.set(i, k, j), i != 0 && k % 16 != 0 && j != 0 ? blockState : blockState2, false);
               }
            }
         }

      }

      public int getHeight(int x, int z, Heightmap.Type heightmapType) {
         return 100;
      }

      public BlockView getColumnSample(int x, int z) {
         return EmptyBlockView.INSTANCE;
      }

}