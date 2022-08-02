package io.github.vampirestudios.raa_dimension.generation.chunkgenerator;

import CatSpawner;
import PhantomSpawner;
import PillagerSpawner;
import io.github.vampirestudios.raa_dimension.utils.old.OctaveOpenSimplexNoise;
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
import net.minecraft.world.level.biome.TheEndBiomeSource;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class ChaosChunkGenerator extends NoiseBasedChunkGenerator {
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

    //from surface chunk generator
    private final PerlinNoise field_16574;
    private final PerlinNoise field_16581;
    private final PerlinNoise field_16575;

    private final OctaveOpenSimplexNoise simplexNoise;

    public ChaosChunkGenerator(BiomeSource biomeSource, long worldSeed, Supplier<NoiseGeneratorSettings> config) {
        super(biomeSource, worldSeed, config);
        this.random.consume(2620);
        this.noiseSampler = new PerlinNoise(this.random, IntStream.of(15, 0));

        this.field_16574 = new PerlinNoise(this.random, IntStream.rangeClosed(-15, 0));
        this.field_16581 = new PerlinNoise(this.random, IntStream.rangeClosed(-15, 0));
        this.field_16575 = new PerlinNoise(this.random, IntStream.rangeClosed(-7, 0));

        this.simplexNoise = new OctaveOpenSimplexNoise(this.random, 4, 1024.0D, 2.0D, 2.0D);
    }

    public void spawnOriginalMobs(WorldGenRegion region) {
        int i = region.getCenterChunkX();
        int j = region.getCenterChunkZ();
        Biome biome = region.getBiome((new ChunkPos(i, j)).getWorldPosition());
        ChunkRandom chunkRandom = new ChunkRandom();
        chunkRandom.setPopulationSeed(region.getSeed(), i << 4, j << 4);
        NaturalSpawner.spawnMobsForChunkGeneration(region, biome, i, j, chunkRandom);
    }

    @Override
    public void sampleNoiseColumn(double[] buffer, int x, int z) {
        NoiseSettings generationShapeConfig = this.settings.get().getGenerationShapeConfig();
        double ac;
        double ad;
        double ai;
        double aj;
        if (this.islandNoise != null) {
            ac = TheEndBiomeSource.getNoiseAt(this.islandNoise, x, z) - 8.0F;
            if (ac > 0.0D) {
                ad = 0.25D;
            } else {
                ad = 1.0D;
            }
        } else {
            float g = 0.0F;
            float h = 0.0F;
            float i = 0.0F;
            int k = this.getSeaLevel();
            float l = this.biomeSource.getBiomeForNoiseGen(x, k, z).getDepth();

            for(int m = -2; m <= 2; ++m) {
                for(int n = -2; n <= 2; ++n) {
                    Biome biome = this.biomeSource.getBiomeForNoiseGen(x + m, k, z + n);
                    float o = biome.getDepth();
                    float p = biome.getScale();
                    float s;
                    float t;
                    if (generationShapeConfig.isAmplified() && o > 0.0F) {
                        s = 1.0F + o * 2.0F;
                        t = 1.0F + p * 4.0F;
                    } else {
                        s = o;
                        t = p;
                    }

                    float u = o > l ? 0.5F : 1.0F;
                    float v = u * BIOME_WEIGHT_TABLE[m + 2 + (n + 2) * 5] / (s + 2.0F);
                    g += t * v;
                    h += s * v;
                    i += v;
                }
            }

            float w = h / i;
            float y = g / i;
            ai = w * 0.5F - 0.125F;
            aj = y * 0.9F + 0.1F;
            ac = ai * 0.265625D;
            ad = 96.0D / aj;
        }
        double[] ds = this.computeNoiseRange(x, z);
        double h = ds[0];
        double k = ds[1];

        double ae = 684.412D * generationShapeConfig.getSampling().getXZScale();
        double af = 684.412D * generationShapeConfig.getSampling().getYScale();
        double ag = ae / generationShapeConfig.getSampling().getXZFactor();
        double ah = af / generationShapeConfig.getSampling().getYFactor();
        ai = generationShapeConfig.getTopSlide().getTarget();
        aj = generationShapeConfig.getTopSlide().getSize();
        double ak = generationShapeConfig.getTopSlide().getOffset();
        double am = generationShapeConfig.getBottomSlide().getSize();

        for(int n = 0; n < this.noiseSizeY; ++n) {
            double o = this.sampleNoise(x, n, z, ae, af, ag, ah);
            o -= this.computeNoisyFalloff(h, k, x, n, z);
            if (aj > 0.0D) {
                o = Mth.clampedLerp(ai, o, ((double)(this.noiseSizeY - n) - ak) / aj);
            } else if (am > 0.0D) {
                o = Mth.clampedLerp(o, -simplexNoise.sample(x, z)*40, o);
            }

            buffer[n] = o;
        }

    }

    protected double computeNoisyFalloff(double depth, double scale, int x, int y, int z) {
        return simplexNoise.sample(x, y);
    }

    public double sampleNoise(int x, int y, int z, double d, double e, double f, double g) {
        double h = 0.0D;
        double i = 0.0D;
        double j = 0.0D;
        double k = 1.0D;

        for(int l = 0; l < 16; ++l) {
            double m = PerlinNoise.wrap((double)x * d * k);
            double n = PerlinNoise.wrap((double)y * e * k);
            double o = PerlinNoise.wrap((double)z * d * k);
            double p = e * k;
            ImprovedNoise perlinNoiseSampler = this.field_16574.getOctaveNoise(l);
            if (perlinNoiseSampler != null) {
                h += perlinNoiseSampler.noise(m, n, o, p, (double)y * p) / k;
            }

            ImprovedNoise perlinNoiseSampler2 = this.field_16581.getOctaveNoise(l);
            if (perlinNoiseSampler2 != null) {
                i += perlinNoiseSampler2.noise(m, n, o, p, (double)y * p) / k;
            }

            if (l < 8) {
                ImprovedNoise perlinNoiseSampler3 = this.field_16575.getOctaveNoise(l);
                if (perlinNoiseSampler3 != null) {
                    j += perlinNoiseSampler3.noise(PerlinNoise.wrap((double)x * f * k), PerlinNoise.wrap((double)y * g * k), PerlinNoise.wrap((double)z * f * k), g * k, (double)y * g * k) / k;
                }
            }

            k /= 2.0D;
        }

        return Mth.clampedLerp(h / 512.0D, i / 512.0D, (j / 10.0D + 1.0D) / 2.0D);
    }

    protected double computeNoiseFalloff(double depth, double scale, int y) {
        double d = 9.5D;
        double e = ((double) y - (d + depth * d / 8.0D * 4.0D)) * 12.0D * 128.0D / 256.0D / scale;
        if (e < 0.0D) {
            e *= 4.0D;
        }

//        e += (Math.sin(e) * ((y + 8) / 4f));

        return e;
    }

    protected double[] computeNoiseRange(int x, int z) {
        NoiseGeneratorSettings chunkGeneratorSettings = this.settings.get();
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
                if (chunkGeneratorSettings.getGenerationShapeConfig().isAmplified() && n > 0.0F) {
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
        ds[0] = (double) g + (this.sampleNoise(x, z));
        ds[1] = f + noiseSampler.getValue(x / 400f, 533, z / 400f);
        return ds;
    }

    private double sampleNoise(int x, int y) {
        double d = this.noiseSampler.getValue(x / 500f, 10.0D, y / 500f, 1.0D, 0.0D, true) * 65535.0D / 8000.0D;
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

        return d * 20f;
    }

    /*public List<Biome.SpawnEntry> getEntitySpawnList(SpawnGroup category, StructureAccessor StructureAccessor, BlockPos pos) {
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