package io.github.vampirestudios.raa_dimension.generation.chunkgenerator.wip.overworld;

import CatSpawner;
import PhantomSpawner;
import PillagerSpawner;
import io.github.vampirestudios.raa.utils.noise.old.OctaveOpenSimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.IWorld;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.SurfaceChunkGenerator;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import java.util.List;

public class PillarWorldChunkGenerator extends SurfaceChunkGenerator<OverworldChunkGeneratorConfig> {
    private final PhantomSpawner phantomSpawner = new PhantomSpawner();
    private final PillagerSpawner pillagerSpawner = new PillagerSpawner();
    private final CatSpawner catSpawner = new CatSpawner();
    private final VillageSiege zombieSiegeManager = new VillageSiege();

    private final OctaveOpenSimplexNoise simplexNoise;

    public PillarWorldChunkGenerator(IWorld iWorld, BiomeSource biomeSource, OverworldChunkGeneratorConfig config) {
        super(iWorld, biomeSource, 8, 4, 256, config, false);
        this.random.consume(2620);
        this.simplexNoise = new OctaveOpenSimplexNoise(this.random, 4, 1024.0D, 384.0D, -128.0D);
    }

    public void populateEntities(WorldGenRegion region) {
        int centerChunkX = region.getCenterChunkX();
        int centerChunkZ = region.getCenterChunkZ();
        Biome biome = region.getBiome((new ChunkPos(centerChunkX, centerChunkZ)).getCenterBlockPos());
        ChunkRandom chunkRandom = new ChunkRandom();
        chunkRandom.setPopulationSeed(region.getSeed(), centerChunkX << 4, centerChunkZ << 4);
        NaturalSpawner.spawnMobsForChunkGeneration(region, biome, centerChunkX, centerChunkZ, chunkRandom);
    }

    protected void sampleNoiseColumn(double[] doubles_1, int int_1, int int_2) {
        this.sampleNoiseColumn(doubles_1, int_1, int_2, 684.4119873046875D, 684.4119873046875D, 8.555149841308594D, 4.277574920654297D, 1, -1);
    }

    protected double computeNoiseFalloff(double double_1, double double_2, int int_1) {
        double double_4 = ((double) int_1 - (8.5D + double_1 * 8.5D / 16.0D * 2.0D)) * 12.0D * 64 / 512 / double_2;
        if (double_4 < 0.0D) {
            double_4 *= 2.0D;
        }

        return double_4;
    }

    protected double[] computeNoiseRange(int x, int z) {
        double[] doubles = new double[2];
        doubles[0] = simplexNoise.sample(x, z) * 128;
        doubles[1] = simplexNoise.sample(z, x) * 128;
        return doubles;
    }

    public List<Biome.SpawnEntry> getEntitySpawnList(StructureManager StructureAccessor, MobCategory entityCategory_1, BlockPos blockPos_1) {
        if (Feature.SWAMP_HUT.method_14029(this.world, StructureAccessor, blockPos_1)) {
            if (entityCategory_1 == MobCategory.MONSTER) {
                return Feature.SWAMP_HUT.getMonsterSpawns();
            }

            if (entityCategory_1 == MobCategory.CREATURE) {
                return Feature.SWAMP_HUT.getCreatureSpawns();
            }
        } else if (entityCategory_1 == MobCategory.MONSTER) {
            if (Feature.PILLAGER_OUTPOST.isApproximatelyInsideStructure(this.world, StructureAccessor, blockPos_1)) {
                return Feature.PILLAGER_OUTPOST.getMonsterSpawns();
            }

            if (Feature.OCEAN_MONUMENT.isApproximatelyInsideStructure(this.world, StructureAccessor, blockPos_1)) {
                return Feature.OCEAN_MONUMENT.getMonsterSpawns();
            }
        }

        return super.getEntitySpawnList(StructureAccessor, entityCategory_1, blockPos_1);
    }

    public void spawnEntities(ServerLevel serverWorld_1, boolean boolean_1, boolean boolean_2) {
        this.phantomSpawner.spawn(serverWorld_1, boolean_1, boolean_2);
        this.pillagerSpawner.spawn(serverWorld_1, boolean_1, boolean_2);
        this.catSpawner.spawn(serverWorld_1, boolean_1, boolean_2);
        this.zombieSiegeManager.tick(serverWorld_1, boolean_1, boolean_2);
    }

    public int getSpawnHeight() {
        return 248;
    }

    public int getSeaLevel() {
        return 63;
    }
}
