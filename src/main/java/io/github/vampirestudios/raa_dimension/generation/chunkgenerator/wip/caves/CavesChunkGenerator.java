package io.github.vampirestudios.raa_dimension.generation.chunkgenerator.wip.caves;

import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.realms.gui.screen.RealmsWorldGeneratorType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.chunk.SurfaceChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class CavesChunkGenerator extends NoiseChunkGenerator {
    private final double[] noiseFalloff = this.buildNoiseFalloff();

    public CavesChunkGenerator(BiomeSource biomeSource, long seed, Supplier<ChunkGeneratorSettings> settingsSupplier) {
        super(biomeSource, seed, settingsSupplier);
    }

    @Override
    protected void sampleNoiseColumn(double[] buffer, int x, int z) {
        this.sampleNoiseColumn(buffer, x, z, 684.412D, 2053.236D, 8.555150000000001D, 34.2206D, 3, -10);
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
            ds[i] = Math.cos((double) i * 3.141592653589793D * 6.0D / (double) this.getNoiseSizeY()) * 2.0D;
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

    @Override
    public List<SpawnSettings.SpawnEntry> getEntitySpawnList(Biome biome, StructureAccessor accessor, SpawnGroup group, BlockPos pos) {
        if (group == SpawnGroup.MONSTER) {
            if (StructureFeature.FORTRESS.(this.world, StructureAccessor, pos)) {
                return Feature.NETHER_BRIDGE.getMonsterSpawns();
            }

            if (Feature.NETHER_BRIDGE.isApproximatelyInsideStructure(this.world, StructureAccessor, pos) && this.world.getBlockState(pos.down()).getBlock() == Blocks.NETHER_BRICKS) {
                return Feature.NETHER_BRIDGE.getMonsterSpawns();
            }
        }
        return super.getEntitySpawnList(biome, accessor, group, pos);
    }

    public List<Biome.SpawnEntry> getEntitySpawnList(StructureAccessor StructureAccessor, SpawnGroup category, BlockPos pos) {

        return super.getEntitySpawnList(StructureAccessor, category, pos);
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
