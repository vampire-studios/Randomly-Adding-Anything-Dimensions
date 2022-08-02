package io.github.vampirestudios.raa_dimension.generation.feature;

import com.mojang.serialization.Codec;
import io.github.vampirestudios.raa_dimension.generation.feature.config.CorruptedFeatureConfig;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.ArrayList;

public class CraterFeature extends Feature<CorruptedFeatureConfig> {
    public static final ArrayList<BlockState> ALLOWED_STATES = new ArrayList<BlockState>();

    public CraterFeature(Codec<CorruptedFeatureConfig> configDeserializer) {
        super(configDeserializer);
        ALLOWED_STATES.add(Blocks.GRASS_BLOCK.defaultBlockState());
        ALLOWED_STATES.add(Blocks.STONE.defaultBlockState());
        ALLOWED_STATES.add(Blocks.GRAVEL.defaultBlockState());
        ALLOWED_STATES.add(Blocks.SAND.defaultBlockState());
    }

    private static boolean canSpawn(ServerLevelAccessor world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state == Blocks.GRASS_BLOCK.defaultBlockState() || state == Blocks.PODZOL.defaultBlockState() || state == Blocks.COARSE_DIRT.defaultBlockState())
            return true;
        return state == Blocks.GRAVEL.defaultBlockState() || state == Blocks.SAND.defaultBlockState() || state == Blocks.STONE.defaultBlockState();
    }

    @Override
    public boolean place(FeaturePlaceContext<CorruptedFeatureConfig> context) {
        BlockPos pos = context.origin();
        WorldGenLevel world = context.level();
        CorruptedFeatureConfig config = context.config();
        if (world.getBlockState(pos.below()).isAir() || !world.getBlockState(pos.below()).canOcclude() || world.getBlockState(pos.below()).equals(Blocks.BEDROCK.defaultBlockState()))
            return true;
        if (canSpawn(world, pos.offset(0, -1, 0))) {
            int amtMax = Rands.randIntRange(1, 3);
            int scale = Rands.randIntRange(1, 3);
            for (int amt = 0; amt < amtMax; amt++) {
                for (int i = -(amt + scale); i <= (amt + scale); i++) {
                    for (int j = -(amt + scale); j <= (amt + scale); j++) {
                        if (i == -(amt + scale) || i == (amt + scale) || j == -(amt + scale) || j == (amt + scale)) {
                            if (!Rands.chance(3)) {
                                this.setBlock(world, new BlockPos(pos.getX() + i, pos.getY() - (amtMax - amt), pos.getZ() + j), Blocks.AIR.defaultBlockState());
                            }
                            if (config.corrupted && Rands.chance(3)) {
                                this.setBlock(world, new BlockPos(pos.getX() + i, pos.getY() - (amtMax - amt), pos.getZ() + j), Blocks.NETHERRACK.defaultBlockState());
                                if (Rands.chance(2)) {
                                    this.setBlock(world, new BlockPos(pos.getX() + i, pos.getY() - (amtMax - amt) + 1, pos.getZ() + j), Blocks.FIRE.defaultBlockState());
                                }
                            }
                        } else {
                            this.setBlock(world, new BlockPos(pos.getX() + i, pos.getY() - (amtMax - amt), pos.getZ() + j), Blocks.AIR.defaultBlockState());
                        }
                    }
                }
            }
        }

        return true;
    }
}
