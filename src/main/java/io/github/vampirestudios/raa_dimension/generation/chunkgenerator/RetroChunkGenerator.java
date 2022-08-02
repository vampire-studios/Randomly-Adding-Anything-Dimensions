package io.github.vampirestudios.raa_dimension.generation.chunkgenerator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.*;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
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

    private final PerlinSimplexNoise noise = new PerlinSimplexNoise(new ChunkRandom(1234L), IntStream.rangeClosed(-5, 0));

    public RetroChunkGenerator(BiomeSource biomeSource, long worldSeed, StructuresConfig config) {
     super(biomeSource, biomeSource, config, worldSeed);
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return new RetroChunkGenerator(this.biomeSource.withSeed(seed), seed, this.structuresConfig);
    }

    public void buildSurface(WorldGenRegion region, ChunkAccess chunk) {

    }

    public void generateFeatures(WorldGenRegion region) {

    }

    public void carve(BiomeManager biomeAccess, ChunkAccess chunk, GenerationStep.Carving carver) {
    }

    public int getSpawnHeight() {
     return 100;
    }

    @Override
    public void populateNoise(LevelAccessor world, StructureManager accessor, ChunkAccess chunk) {
         BlockState blockState = Blocks.BLACK_CONCRETE.defaultBlockState();
         BlockState blockState2 = Blocks.LIME_CONCRETE.defaultBlockState();
         ChunkPos chunkPos = chunk.getPos();
         BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

         for(int i = 0; i < 16; ++i) {
            for(int j = 0; j < 16; ++j) {
               double d = 64.0D + (this.noise.getValue((float)chunkPos.x + (float)i / 16.0F, (float)chunkPos.z + (float)j / 16.0F, false) + 1.0D) * 20.0D;

               for(int k = 0; (double)k < d; ++k) {
                  chunk.setBlockState(mutable.set(i, k, j), i != 0 && k % 16 != 0 && j != 0 ? blockState : blockState2, false);
               }
            }
         }

      }

      public int getHeight(int x, int z, Heightmap.Types heightmapType) {
         return 100;
      }

      public BlockGetter getColumnSample(int x, int z) {
         return EmptyBlockGetter.INSTANCE;
      }

}