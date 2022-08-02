package io.github.vampirestudios.raa_dimension.generation.carvers.old;

import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

public class VerticalCarver extends RAACarver<ProbabilityFeatureConfiguration> {
    public VerticalCarver(DimensionData data) {
        super(ProbabilityFeatureConfiguration.CODEC, data);
    }

    @Override
    public boolean carve(ChunkAccess chunk, Function<BlockPos, Biome> posToBiome, Random random, int seaLevel, int chunkX, int chunkZ, int mainChunkX, int mainChunkZ, BitSet carvingMask, ProbabilityFeatureConfiguration carverConfig) {

        double x = (chunkX * 16 + random.nextInt(16));
        double y = random.nextInt(256);
        double z = (chunkZ * 16 + random.nextInt(16));

        for(int p = 0; p < 3; ++p) {
            y-=5;
            float t;
            t = 2.0F + random.nextFloat() * 3.0F;
            this.carveCave(chunk, posToBiome, random.nextLong(), seaLevel, mainChunkX, mainChunkZ, x, y, z, t, 0.35, carvingMask);
        }
        return true;
    }

    protected void carveCave(ChunkAccess chunk, Function<BlockPos, Biome> posToBiome, long seed, int seaLevel, int mainChunkX, int mainChunkZ, double x, double y, double z, float yaw, double yawPitchRatio, BitSet carvingMask) {
        double d = 1.5D + (double)(Mth.sin(1.5707964F) * yaw);
        double e = d * yawPitchRatio;
        this.carveRegion(chunk, posToBiome, seed, seaLevel, mainChunkX, mainChunkZ, x + 1.0D, y, z, d, e, carvingMask);
    }

    @Override
    public boolean shouldCarve(Random random, int chunkX, int chunkZ, ProbabilityFeatureConfiguration config) {
        return random.nextFloat() <= config.probability;
    }

    @Override
    protected boolean isPositionExcluded(double scaledRelativeX, double scaledRelativeY, double scaledRelativeZ, int y) {
        return false;
    }

}
