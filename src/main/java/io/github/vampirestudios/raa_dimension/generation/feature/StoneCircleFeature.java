package io.github.vampirestudios.raa_dimension.generation.feature;

import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import io.github.vampirestudios.raa_dimension.utils.FeatureUtils;
import io.github.vampirestudios.raa_dimension.utils.old.OctaveOpenSimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

//Code kindly taken from The Hallow, thanks to everyone who is working on it!
public class StoneCircleFeature extends Feature<NoneFeatureConfiguration> {

    private static final OctaveOpenSimplexNoise offsetNoise = new OctaveOpenSimplexNoise(new Random(0), 2, 25D, 4D, 3D);
    private static final ResourceLocation LOOT_TABLE = new ResourceLocation(RAADimensionAddon.MOD_ID, "chest/stone_circle");
    private static BlockState STONE;
    private static BlockState COBBLESTONE;

    public StoneCircleFeature(DimensionData dimensionData) {
        super(NoneFeatureConfiguration.CODEC);
        STONE = Registry.BLOCK.get(new ResourceLocation(RAADimensionAddon.MOD_ID, dimensionData.getName().toLowerCase() + "_stone")).defaultBlockState();
        COBBLESTONE = Registry.BLOCK.get(new ResourceLocation(RAADimensionAddon.MOD_ID, dimensionData.getName().toLowerCase() + "_cobblestone")).defaultBlockState();
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos pos = context.origin();
        WorldGenLevel world = context.level();
        RandomSource rand = context.random();
        return this.generate(world, rand, pos);
    }

    private boolean generate(ServerLevelAccessor world, RandomSource rand, BlockPos pos) {
        if (world.getBlockState(pos.offset(0, -1, 0)).isAir() || !world.getBlockState(pos.offset(0, -1, 0)).canOcclude() || world.getBlockState(pos.offset(0, -1, 0)).equals(Blocks.BEDROCK.defaultBlockState()))
            return true;

        int centreX = pos.getX() + rand.nextInt(16) - 8;
        int centreZ = pos.getZ() + rand.nextInt(16) - 8;
        int lowY = pos.getY() - 1;

        int radius = rand.nextInt(6) + 14;

        int squaredRadius = radius * radius;
        int baseHeight = rand.nextInt(3) + 5;

        final double pythagRadiusPart = Math.sqrt((double) squaredRadius / 2D);

        BlockPos.MutableBlockPos posMutable = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos posMutable2 = new BlockPos.MutableBlockPos();

        for (int quarter = 0; quarter < 4; ++quarter) {
            for (int localPosition = -1; localPosition < 2; ++localPosition) {
                int qBit1 = quarter & 0b01;
                int qBit2 = quarter & 0b10;

                double xOffset = 0;
                double zOffset = 0;

                if (qBit1 == 0) {
                    switch (localPosition) {
                        case -1:
                            xOffset = -pythagRadiusPart;
                            break;
                        case 1:
                            xOffset = pythagRadiusPart;
                            break;
                        default:
                            break;
                    }

                    if (qBit2 == 0) {
                        zOffset = (xOffset == 0) ? radius : pythagRadiusPart;
                    } else {
                        zOffset = (xOffset == 0) ? -radius : -pythagRadiusPart;
                    }
                } else {
                    switch (localPosition) {
                        case -1:
                            zOffset = -pythagRadiusPart;
                            break;
                        case 1:
                            zOffset = pythagRadiusPart;
                            break;
                        default:
                            break;
                    }

                    if (qBit2 == 0) {
                        xOffset = (zOffset == 0) ? radius : pythagRadiusPart;
                    } else {
                        xOffset = (zOffset == 0) ? -radius : -pythagRadiusPart;
                    }
                }

                posMutable.setX((int) (centreX + xOffset));
                posMutable.setZ((int) (centreZ + zOffset));

                posMutable2.setX((int) (centreX + xOffset));
                posMutable2.setZ((int) (centreZ + zOffset));

                generateStone(world, rand, posMutable, posMutable2, baseHeight + rand.nextInt(3), lowY);
            }
        }

        if (rand.nextInt(3) == 0) {
            FeatureUtils.setLootChest(world, new BlockPos(centreX + rand.nextInt(3) - 1, lowY - 2 - rand.nextInt(3), centreZ + rand.nextInt(3) - 1), LOOT_TABLE, rand);
        }

        //Record spawn in text file
//        Utils.createSpawnsFile("stone_circle", world, pos);

        return true;
    }

    private void generateStone(LevelWriter world, RandomSource rand, final BlockPos centre, BlockPos.MutableBlockPos mutable, int height, int lowY) {
        final int posX = centre.getX();
        final int posZ = centre.getZ();

        for (int xOffset = -3; xOffset < 4; ++xOffset) {
            mutable.setX(posX + xOffset);
            for (int zOffset = -3; zOffset < 4; ++zOffset) {
                mutable.setZ(posZ + zOffset);
                mutable.setY(0);

                double squaredDistanceTo = centre.distSqr(mutable) / 9D;

                int localHeight = (int) (offsetNoise.sample(mutable.getX(), mutable.getZ()) + (int) Mth.lerp(squaredDistanceTo, height, 0D) + lowY);

                for (int y = lowY - 5; y < localHeight + 1; ++y) {
                    mutable.setY(y);
                    world.setBlock(mutable, rand.nextInt(3) == 0 ? COBBLESTONE : STONE, 19);
                }
            }
        }
    }
}