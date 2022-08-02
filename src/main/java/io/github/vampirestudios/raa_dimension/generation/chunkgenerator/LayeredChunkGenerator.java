//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.vampirestudios.raa_dimension.generation.chunkgenerator;

import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.gen.*;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import CatSpawner;
import PhantomSpawner;
import PillagerSpawner;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class LayeredChunkGenerator extends NoiseBasedChunkGenerator {
    private static final float[] BIOME_WEIGHT_TABLE = Util.make(new float[25], (fs) -> {
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                float f = 10.0F / Mth.sqrt((float) (i * i + j * j) + 0.2F);
                fs[i + 2 + (j + 2) * 5] = f;
            }
        }

    });
    private final PerlinNoise noiseSampler;
    private final PhantomSpawner phantomSpawner = new PhantomSpawner();
    private final PillagerSpawner pillagerSpawner = new PillagerSpawner();
    private final CatSpawner catSpawner = new CatSpawner();
    private final VillageSiege zombieSiegeManager = new VillageSiege();

    public LayeredChunkGenerator(BiomeSource biomeSource, long worldSeed, Supplier<NoiseGeneratorSettings> settingsSupplier) {
        super(biomeSource, worldSeed, settingsSupplier);
        this.random.consume(2620);
        this.noiseSampler = new PerlinNoise(this.random, IntStream.of(15, 0));
    }

    public void spawnOriginalMobs(WorldGenRegion region) {
        int i = region.getCenterChunkX();
        int j = region.getCenterChunkZ();
        Biome biome = region.getBiome((new ChunkPos(i, j)).getWorldPosition());
        ChunkRandom chunkRandom = new ChunkRandom();
        chunkRandom.setPopulationSeed(region.getSeed(), i << 4, j << 4);
        NaturalSpawner.spawnMobsForChunkGeneration(region, biome, i, j, chunkRandom);
    }

    protected double computeNoiseFalloff(double depth, double scale, int y) {
        double d = 8.5D;
        double e = ((double) y - (8.5D + depth * 8.5D / 8.0D * 4.0D)) * 12.0D * 128.0D / 256.0D / scale;
        if (e < 0.0D) {
            e *= 4.0D;
        }

        e += (Mth.sin(y * 2) * 60);

        return e;
    }

    protected double[] computeNoiseRange(int x, int z) {
        NoiseSettings generationShapeConfig = this.settings.get().getGenerationShapeConfig();
        double[] ds = new double[2];
        float f = 0.0F;
        float g = 0.0F;
        float h = 0.0F;
        int j = this.getSeaLevel();
        float k = this.biomeSource.getBiomeForNoiseGen(x, j, z).getDepth();

        for (int l = -2; l <= 2; ++l) {
            for (int m = -2; m <= 2; ++m) {
                Biome biome = this.biomeSource.getBiomeForNoiseGen(x + l, j, z + m);
                float n = biome.getDepth();
                float o = biome.getScale();
                if (generationShapeConfig.isAmplified() && n > 0.0F) {
                    n = 1.0F + n * 2.0F;
                    o = 1.0F + o * 4.0F;
                }

                float p = BIOME_WEIGHT_TABLE[l + 2 + (m + 2) * 5] / (n + 2.0F);
                if (biome.getDepth() > k) {
                    p /= 2.0F;
                }

                f += o * p;
                g += n * p;
                h += p;
            }
        }

        f /= h;
        g /= h;
        f = f * 0.9F + 0.1F;
        g = (g * 4.0F - 1.0F) / 8.0F;
        ds[0] = (double) g + this.sampleNoise(x, z);
        ds[1] = f;
        return ds;
    }

    private double sampleNoise(int x, int y) {
        double d = this.noiseSampler.getValue(x * 200, 10.0D, y * 200, 1.0D, 0.0D, true) * 65535.0D / 8000.0D;
        if (d < 0.0D) {
            d = -d * 0.3D;
        }

        d = d * 3.0D - 2.0D;
        if (d < 0.0D) {
            d /= 28.0D;
        } else {
            if (d > 1.0D) {
                d = 1.0D;
            }

            d /= 40.0D;
        }

        return -d;
    }

    /*public List<SpawnEntry> getEntitySpawnList(SpawnGroup category, StructureAccessor StructureAccessor, BlockPos pos) {
        if (Feature.SWAMP_HUT.method_14029(this.world, StructureAccessor, pos)) {
            if (category == SpawnGroup.MONSTER) {
                return Feature.SWAMP_HUT.getMonsterSpawns();
            }

            if (category == SpawnGroup.CREATURE) {
                return Feature.SWAMP_HUT.getCreatureSpawns();
            }
        } else if (category == SpawnGroup.MONSTER) {
            if (Feature.PILLAGER_OUTPOST.isApproximatelyInsideStructure(this.world, StructureAccessor, pos)) {
                return Feature.PILLAGER_OUTPOST.getMonsterSpawns();
            }

            if (Feature.OCEAN_MONUMENT.isApproximatelyInsideStructure(this.world, StructureAccessor, pos)) {
                return Feature.OCEAN_MONUMENT.getMonsterSpawns();
            }
        }

        return super.getEntitySpawnList(StructureAccessor, category, pos);
    }*/

    @Override
    public void generateFeatures(WorldGenRegion region, StructureManager StructureAccessor) {
        int chunkX = region.getCenterChunkX();
        int chunkZ = region.getCenterChunkZ();
        ChunkRandom rand = new ChunkRandom();
        rand.setTerrainSeed(chunkX, chunkZ);

        int i = region.getCenterChunkX();
        int j = region.getCenterChunkZ();
        int k = i * 16;
        int l = j * 16;
        BlockPos blockPos = new BlockPos(k, 0, l);
        BlockPos pos = blockPos.offset(8, 8, 8);
        Biome biome = this.biomeSource.getBiomeForNoiseGen(pos.getX(), pos.getY(), pos.getZ());
        ChunkRandom chunkRandom = new ChunkRandom();
        long seed = chunkRandom.setCarverSeed(region.getSeed(), k, l);
        GenerationStep.Decoration[] features = GenerationStep.Decoration.values();

        for (GenerationStep.Decoration feature : features) {
            try {
                biome.generateFeatureStep(StructureAccessor, this, region, seed, chunkRandom, blockPos);
            } catch (Exception exception) {
                CrashReport crashReport = CrashReport.forThrowable(exception, "Biome decoration");
                crashReport.addCategory("Generation").setDetail("CenterX", i).setDetail("CenterZ", j).setDetail("Step", feature).setDetail("Seed", seed).setDetail("Biome", BuiltinRegistries.BIOME.getKey(biome));
                throw new ReportedException(crashReport);
            }
        }
    }

    public void spawnEntities(ServerLevel serverWorld, boolean spawnMonsters, boolean spawnAnimals) {
        this.phantomSpawner.spawn(serverWorld, spawnMonsters, spawnAnimals);
        this.pillagerSpawner.spawn(serverWorld, spawnMonsters, spawnAnimals);
        this.catSpawner.spawn(serverWorld, spawnMonsters, spawnAnimals);
        this.zombieSiegeManager.tick(serverWorld, spawnMonsters, spawnAnimals);
    }

    public int getSpawnHeight() {
        return this.settings.get().getSeaLevel() + 1;
    }

    public int getSeaLevel() {
        return 63;
    }

}