package io.github.vampirestudios.raa_dimension.generation.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.ArrayList;
import java.util.List;

public class CampfireFeature extends Feature<NoneFeatureConfiguration> {
    public CampfireFeature(Codec<NoneFeatureConfiguration> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos pos = context.origin();
        WorldGenLevel world = context.level();
        RandomSource rand = context.random();
        NoneFeatureConfiguration config = context.config();
        if (world.getBlockState(pos.offset(0, -1, 0)).isAir() || !world.getBlockState(pos.offset(0, -1, 0)).canOcclude() || world.getBlockState(pos.offset(0, -1, 0)).equals(Blocks.BEDROCK.defaultBlockState()))
            return true;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (j == -1 || j == 1 || i == -1 || i == 1)
                    world.setBlock(pos.offset(i, -1, j), Blocks.DIRT_PATH.defaultBlockState(), 2);
            }
        }
        List<Block> stairs = new ArrayList<>(
                ImmutableList.of(
                        Blocks.ACACIA_STAIRS,
                        Blocks.BIRCH_STAIRS,
                        Blocks.DARK_OAK_STAIRS,
                        Blocks.JUNGLE_STAIRS,
                        Blocks.OAK_STAIRS,
                        Blocks.SPRUCE_STAIRS,
                        Blocks.STONE_STAIRS,
                        Blocks.BRICK_STAIRS,
                        Blocks.STONE_BRICK_STAIRS
                )
        );
        Block stair = Rands.list(stairs);

        if (Rands.chance(2))
            world.setBlock(pos.offset(0, 0, 2), stair.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH), 2);
        if (Rands.chance(2))
            world.setBlock(pos.offset(0, 0, -2), stair.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH), 2);
        if (Rands.chance(4)) {
            world.setBlock(pos, Blocks.CAMPFIRE.defaultBlockState(), 2);
        } else {
            world.setBlock(pos, Blocks.CAMPFIRE.defaultBlockState().setValue(BlockStateProperties.LIT, false), 2);
        }

        //half of all campfires have chests
        if (Rands.chance(2)) {
            world.setBlock(pos.offset(-2, 0, 0), StructurePiece.reorient(world, pos, Blocks.CHEST.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST)), 2);
//            LootableContainerBlockEntity.setLootTable(world, Rands.getRandom(), pos.add(-2, 0, 0), RAALootTables.CAMPFIRE_LOOT);
            RandomizableContainerBlockEntity.setLootTable(world, rand, pos.offset(-2, 0, 0), BuiltInLootTables.DESERT_PYRAMID);
        } else {
            if (Rands.chance(2))
                world.setBlock(pos.offset(-2, 0, 0), stair.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST), 2);
        }

        Block woolBlock = Rands.values(new Block[]{Blocks.WHITE_WOOL, Blocks.ORANGE_WOOL, Blocks.MAGENTA_WOOL, Blocks.LIGHT_BLUE_WOOL,
                Blocks.YELLOW_WOOL, Blocks.LIME_WOOL, Blocks.PINK_WOOL, Blocks.GRAY_WOOL,
                Blocks.LIGHT_GRAY_WOOL, Blocks.CYAN_WOOL, Blocks.PURPLE_WOOL, Blocks.BLUE_WOOL,
                Blocks.BROWN_WOOL, Blocks.GREEN_WOOL, Blocks.RED_WOOL, Blocks.BLACK_WOOL});
        Block carpetBlock = Rands.values(new Block[]{Blocks.WHITE_CARPET, Blocks.ORANGE_CARPET, Blocks.MAGENTA_CARPET, Blocks.LIGHT_BLUE_CARPET,
                Blocks.YELLOW_CARPET, Blocks.LIME_CARPET, Blocks.PINK_CARPET, Blocks.GRAY_CARPET,
                Blocks.LIGHT_GRAY_CARPET, Blocks.CYAN_CARPET, Blocks.PURPLE_CARPET, Blocks.BLUE_CARPET,
                Blocks.BROWN_CARPET, Blocks.GREEN_CARPET, Blocks.RED_CARPET, Blocks.BLACK_CARPET});

        //1/2 of all campfires have tents
        if (Rands.chance(2)) {
            List<Block> fences = new ArrayList<>(
                    ImmutableList.of(
                            Blocks.ACACIA_FENCE,
                            Blocks.BIRCH_FENCE,
                            Blocks.DARK_OAK_FENCE,
                            Blocks.JUNGLE_FENCE,
                            Blocks.OAK_FENCE,
                            Blocks.SPRUCE_FENCE,
                            Blocks.STONE_BRICK_WALL,
                            Blocks.BRICK_WALL
                    )
            );
            Block fence = Rands.list(fences);

            for (int i = -1; i <= 1; i++) {
                for (int j = 0; j < 3; j++) {
                    world.setBlock(pos.offset(5 - j, j, i), woolBlock.defaultBlockState(), 2);
                }
            }
            world.setBlock(pos.offset(2, 2, 1), woolBlock.defaultBlockState(), 2);
            world.setBlock(pos.offset(2, 2, 0), woolBlock.defaultBlockState(), 2);
            world.setBlock(pos.offset(2, 2, -1), woolBlock.defaultBlockState(), 2);

            world.setBlock(pos.offset(2, 0, -1), fence.defaultBlockState(), 2);
            world.setBlock(pos.offset(2, 1, -1), fence.defaultBlockState(), 2);
            world.setBlock(pos.offset(2, 0, 1), fence.defaultBlockState(), 2);
            world.setBlock(pos.offset(2, 1, 1), fence.defaultBlockState(), 2);

            world.setBlock(pos.offset(3, 0, -1), carpetBlock.defaultBlockState(), 2);
            world.setBlock(pos.offset(4, 0, -1), carpetBlock.defaultBlockState(), 2);
            world.setBlock(pos.offset(3, 0, 0), carpetBlock.defaultBlockState(), 2);
            world.setBlock(pos.offset(4, 0, 0), carpetBlock.defaultBlockState(), 2);
            world.setBlock(pos.offset(3, 0, 1), carpetBlock.defaultBlockState(), 2);
            world.setBlock(pos.offset(4, 0, 1), carpetBlock.defaultBlockState(), 2);
            world.setBlock(pos.offset(2, 0, 0), carpetBlock.defaultBlockState(), 2);

            // 1/2 chance for a lantern
            if (Rands.chance(2))
                world.setBlock(pos.offset(3, 1, -1), Blocks.LANTERN.defaultBlockState().setValue(BlockStateProperties.HANGING, true), 2);

            world.setBlock(pos.offset(3, 0, 2), StructurePiece.reorient(world, pos, Blocks.CHEST.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST)), 2);
//            LootableContainerBlockEntity.setLootTable(world, Rands.getRandom(), pos.add(3, 0, 2), RAALootTables.CAMPFIRE_TENT_LOOT);
            RandomizableContainerBlockEntity.setLootTable(world, rand, pos.offset(3, 0, 2), BuiltInLootTables.DESERT_PYRAMID);
        } else {
            if (Rands.chance(2))
                world.setBlock(pos.offset(2, 0, 0), stair.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST), 2);
        }

//        Utils.createSpawnsFile("campfire", world, pos);
        return true;
    }
}
