package io.github.vampirestudios.raa_dimension.generation.carvers.old;

import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

public class TeardropCarver extends RAACarver<ProbabilityFeatureConfiguration> {
    public TeardropCarver(DimensionData data) {
        super(ProbabilityFeatureConfiguration.CODEC, data);
    }

    @Override
    public boolean carve(ChunkAccess chunk, Function<BlockPos, Biome> posToBiome, Random random, int seaLevel, int chunkX, int chunkZ, int mainChunkX, int mainChunkZ, BitSet carvingMask,
                         ProbabilityFeatureConfiguration carverConfig) {

        //initialize variables
        int x = (chunkX* 16) + random.nextInt(16);
        int z = (chunkZ* 16) + random.nextInt(16);
        int y = random.nextInt(256);
        double yaw = 1;
        double pitch = 3;
        double xzs = 0.5;
        for (int i = 0; i < 8; i++) {
            y -= 1; //lower the carving region
            xzs += 0.0157; //increase the horizontal stretch
            this.carveRegion(chunk,
                    posToBiome,
                    random.nextInt(),
                    seaLevel,
                    mainChunkX,
                    mainChunkZ,
                    x,
                    y,
                    z,
                    yaw + (i * xzs),
                    pitch / (i / xzs),
                    carvingMask);
        }

        //ease out the bubble
        this.carveRegion(chunk,
                posToBiome,
                random.nextInt(),
                seaLevel,
                mainChunkX,
                mainChunkZ,
                x,
                y - 1,
                z,
                yaw + (5 * xzs),
                pitch / (5 / xzs),
                carvingMask);
        return true;
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
