package io.github.vampirestudios.raa_dimension.generation.feature;

import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import io.github.vampirestudios.raa_dimension.utils.FeatureUtils;
import io.github.vampirestudios.raa_dimension.utils.old.OctaveOpenSimplexNoise;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

//Code kindly taken from The Hallow, thanks to everyone who is working on it!
public class TombFeature extends Feature<NoneFeatureConfiguration> {


    private static final OctaveOpenSimplexNoise offsetNoise = new OctaveOpenSimplexNoise(new Random(0), 2, 30D, 4D, 2D);
    private static final BlockState AIR = Blocks.AIR.defaultBlockState();
    private static final EntityType<?> SKELETON = EntityType.SKELETON;
    private static final ResourceLocation LOOT_TABLE = new ResourceLocation(RAADimensionAddon.MOD_ID, "chest/tomb");
    private static BlockState STONE;

    public TombFeature(DimensionData dimensionData) {
        super(NoneFeatureConfiguration.CODEC);
        STONE = Registry.BLOCK.get(new ResourceLocation(RAADimensionAddon.MOD_ID, dimensionData.getName().toLowerCase() + "_stone")).defaultBlockState();
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos pos = context.origin();
        WorldGenLevel world = context.level();
        RandomSource rand = context.random();
        if (world.getBlockState(pos.offset(0, -3, 0)).isAir() || !world.getBlockState(pos.offset(0, -3, 0)).canOcclude() || world.getBlockState(pos.offset(0, -1, 0)).equals(Blocks.BEDROCK.defaultBlockState()))
            return true;
        return this.generate(world, rand, pos.offset(0, -3, 0));
    }

    private boolean generate(ServerLevelAccessor world, RandomSource rand, BlockPos pos) {
        int centreX = pos.getX() + rand.nextInt(16) - 8;
        int centreZ = pos.getZ() + rand.nextInt(16) - 8;
        int lowY = pos.getY() - 3;

        int radius = rand.nextInt(10) + 7;
        int height = rand.nextInt(8) + 6;

        double radiusSquared = radius * radius;

        Vec3 origin = new Vec3(centreX, 0, centreZ);

        BlockPos.MutableBlockPos posMutable = new BlockPos.MutableBlockPos();

        for (int xOffset = -radius; xOffset <= radius; ++xOffset) {
            int x = centreX + xOffset;

            for (int zOffset = -radius; zOffset <= radius; ++zOffset) {
                int z = centreZ + zOffset;

                Vec3 position = new Vec3(x, 0, z);
                double sqrDistTo = position.distanceToSqr(origin);
                if (sqrDistTo <= radiusSquared) {
                    double progress = Mth.smoothstep(sqrDistTo / radiusSquared);
                    int heightOffset = (int) Mth.lerp(progress, height, 0);
                    heightOffset += (int) Mth.lerp(progress, offsetNoise.sample(x, z), 0);

                    posMutable.setX(x);
                    posMutable.setZ(z);

                    this.generateBarrowColumn(world, rand, lowY, heightOffset, posMutable);
                }
            }
        }

//        Utils.createSpawnsFile("tomb", world, pos);
        return true;
    }

    private void generateBarrowColumn(ServerLevelAccessor world, RandomSource rand, int lowY, int heightOffset, BlockPos.MutableBlockPos pos) {
        int upperY = lowY + heightOffset;

        for (int y = upperY; y >= lowY; --y) {
            pos.setY(y);
            /*if (y == upperY) {
                world.setBlock(pos, surfaceConfig.getTopMaterial(), 19);
            } else if (y > upperY - 3) {
                world.setBlock(pos, surfaceConfig.getUnderMaterial(), 19);
            } else*/ if (y == lowY && rand.nextInt(48) == 0) {
                if (rand.nextInt(4) == 0) {
                    FeatureUtils.setLootChest(world, pos, LOOT_TABLE, rand);
                } else {
                    FeatureUtils.setSpawner(world, pos, Rands.chance(2) ? SKELETON : EntityType.ZOMBIE);
                }
            } else {
                world.setBlock(pos, y <= lowY + 1 ? STONE : AIR, 19);
            }
        }
    }

    private interface Coordinate2iFunction<T> {
        T get(int x, int z);
    }

    private interface Coordinate3iFunction<T> {
        T get(int x, int y, int z);
    }

}