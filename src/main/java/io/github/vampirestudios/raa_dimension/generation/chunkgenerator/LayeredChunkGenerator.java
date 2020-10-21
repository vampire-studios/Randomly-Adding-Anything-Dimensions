//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.vampirestudios.raa_dimension.generation.chunkgenerator;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.village.ZombieSiegeManager;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;

import java.util.function.Supplier;
import java.util.stream.IntStream;

public class LayeredChunkGenerator extends NoiseChunkGenerator {
    private static final float[] BIOME_WEIGHT_TABLE = Util.make(new float[25], (fs) -> {
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                float f = 10.0F / MathHelper.sqrt((float) (i * i + j * j) + 0.2F);
                fs[i + 2 + (j + 2) * 5] = f;
            }
        }

    });
    private final OctavePerlinNoiseSampler noiseSampler;
    private final PhantomSpawner phantomSpawner = new PhantomSpawner();
    private final PillagerSpawner pillagerSpawner = new PillagerSpawner();
    private final CatSpawner catSpawner = new CatSpawner();
    private final ZombieSiegeManager zombieSiegeManager = new ZombieSiegeManager();

    public LayeredChunkGenerator(BiomeSource biomeSource, long worldSeed, Supplier<ChunkGeneratorSettings> settingsSupplier) {
        super(biomeSource, worldSeed, settingsSupplier);
        this.random.consume(2620);
        this.noiseSampler = new OctavePerlinNoiseSampler(this.random, IntStream.of(15, 0));
    }

    public void populateEntities(ChunkRegion region) {
        int i = region.getCenterChunkX();
        int j = region.getCenterChunkZ();
        Biome biome = region.getBiome((new ChunkPos(i, j)).getStartPos());
        ChunkRandom chunkRandom = new ChunkRandom();
        chunkRandom.setPopulationSeed(region.getSeed(), i << 4, j << 4);
        SpawnHelper.populateEntities(region, biome, i, j, chunkRandom);
    }

    protected double computeNoiseFalloff(double depth, double scale, int y) {
        double d = 8.5D;
        double e = ((double) y - (8.5D + depth * 8.5D / 8.0D * 4.0D)) * 12.0D * 128.0D / 256.0D / scale;
        if (e < 0.0D) {
            e *= 4.0D;
        }

        e += (MathHelper.sin(y * 2) * 60);

        return e;
    }

    protected double[] computeNoiseRange(int x, int z) {
        GenerationShapeConfig generationShapeConfig = this.settings.get().getGenerationShapeConfig();
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
        double d = this.noiseSampler.sample(x * 200, 10.0D, y * 200, 1.0D, 0.0D, true) * 65535.0D / 8000.0D;
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
    public void generateFeatures(ChunkRegion region, StructureAccessor StructureAccessor) {
        int chunkX = region.getCenterChunkX();
        int chunkZ = region.getCenterChunkZ();
        ChunkRandom rand = new ChunkRandom();
        rand.setTerrainSeed(chunkX, chunkZ);

        int i = region.getCenterChunkX();
        int j = region.getCenterChunkZ();
        int k = i * 16;
        int l = j * 16;
        BlockPos blockPos = new BlockPos(k, 0, l);
        BlockPos pos = blockPos.add(8, 8, 8);
        Biome biome = this.biomeSource.getBiomeForNoiseGen(pos.getX(), pos.getY(), pos.getZ());
        ChunkRandom chunkRandom = new ChunkRandom();
        long seed = chunkRandom.setCarverSeed(region.getSeed(), k, l);
        GenerationStep.Feature[] features = GenerationStep.Feature.values();

        for (GenerationStep.Feature feature : features) {
            try {
                biome.generateFeatureStep(StructureAccessor, this, region, seed, chunkRandom, blockPos);
            } catch (Exception exception) {
                CrashReport crashReport = CrashReport.create(exception, "Biome decoration");
                crashReport.addElement("Generation").add("CenterX", i).add("CenterZ", j).add("Step", feature).add("Seed", seed).add("Biome", BuiltinRegistries.BIOME.getId(biome));
                throw new CrashException(crashReport);
            }
        }
    }

    public void spawnEntities(ServerWorld serverWorld, boolean spawnMonsters, boolean spawnAnimals) {
        this.phantomSpawner.spawn(serverWorld, spawnMonsters, spawnAnimals);
        this.pillagerSpawner.spawn(serverWorld, spawnMonsters, spawnAnimals);
        this.catSpawner.spawn(serverWorld, spawnMonsters, spawnAnimals);
        this.zombieSiegeManager.spawn(serverWorld, spawnMonsters, spawnAnimals);
    }

    public int getSpawnHeight() {
        return this.settings.get().getSeaLevel() + 1;
    }

    public int getSeaLevel() {
        return 63;
    }

}