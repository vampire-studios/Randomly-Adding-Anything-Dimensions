package io.github.vampirestudios.raa_dimension.generation.chunkgenerator.wip.caves;

import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.chunk.SurfaceChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.List;
import java.util.function.Supplier;

public class CavesChunkGenerator extends NoiseChunkGenerator {
    private final double[] noiseFalloff = this.buildNoiseFalloff();

    public CavesChunkGenerator(BiomeSource biomeSource, long seed, Supplier<ChunkGeneratorSettings> settingsSupplier) {
        super(biomeSource, seed, settingsSupplier);
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world) {
        return super.getColumnSample(x, z, world);
    }

    protected double[] computeNoiseRange(int x, int z) {
        return new double[]{0.0D, 0.0D};
    }

    protected double computeNoiseFalloff(double depth, double scale, int y) {
        return this.noiseFalloff[y];
    }

    private double[] buildNoiseFalloff() {
        double[] ds = new double[this.noiseSizeY];

        for (int i = 0; i < this.noiseSizeY; ++i) {
            ds[i] = Math.cos((double) i * 3.141592653589793D * 6.0D / (double) this.getMaxY()) * 2.0D;
            double d = i;
            if (i > this.noiseSizeY / 2) {
                d = this.noiseSizeY - 1 - i;
            }

            if (d < 4.0D) {
                d = 4.0D - d;
                ds[i] -= d * d * d * 10.0D;
            }
        }

        return ds;
    }

    public int getSpawnHeight() {
        return this.world.getSeaLevel() + 1;
    }

    public int getMaxY() {
        return 256;
    }

    public int getSeaLevel() {
        return 32;
    }
}
