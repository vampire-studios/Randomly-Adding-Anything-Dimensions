package io.github.vampirestudios.raa_dimension.generation.feature;

import com.mojang.serialization.Codec;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class OutpostFeature extends Feature<NoneFeatureConfiguration> {
    public OutpostFeature(Codec<NoneFeatureConfiguration> configDeserializer) {
        super(configDeserializer);
    }

    //Generates tiered outposts
    // T0 = stone brick
    // T1 = brick
    // T2 = obsidian
    // T-1 = cobblestone

    public static void placeBlockAt(ServerLevelAccessor world, BlockPos pos, int tier) {
        switch (tier) {
            case -1 -> {
                int randneg1 = Rands.randInt(4);
                switch (randneg1) {
                    case 0, 1 -> world.setBlock(pos, Blocks.COBBLESTONE.defaultBlockState(), 2);
                    case 2 -> world.setBlock(pos, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 2);
                    case 3 ->
                            world.setBlock(pos, (Rands.chance(3)) ? Blocks.COBWEB.defaultBlockState() : Blocks.AIR.defaultBlockState(), 2);
                }
            }
            case 0 -> {
                int rand = Rands.randInt(5);
                switch (rand) {
                    case 0, 4 -> world.setBlock(pos, Blocks.STONE_BRICKS.defaultBlockState(), 2);
                    case 1 -> world.setBlock(pos, Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 2);
                    case 2 -> world.setBlock(pos, Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 2);
                    case 3 ->
                            world.setBlock(pos, (Rands.chance(4)) ? Blocks.COBWEB.defaultBlockState() : Blocks.AIR.defaultBlockState(), 2);
                }
            }
            case 1 -> {
                int rand1 = Rands.randInt(8);
                if (rand1 == 0) {
                    world.setBlock(pos, (Rands.chance(4)) ? Blocks.COBWEB.defaultBlockState() : Blocks.AIR.defaultBlockState(), 2);
                } else {
                    world.setBlock(pos, Blocks.BRICKS.defaultBlockState(), 2);
                }
            }
            case 2 -> {
                int rand2 = Rands.randInt(20);
                if (rand2 == 0) {
                    world.setBlock(pos, (Rands.chance(4)) ? Blocks.COBWEB.defaultBlockState() : Blocks.AIR.defaultBlockState(), 2);
                } else {
                    world.setBlock(pos, Blocks.OBSIDIAN.defaultBlockState(), 2);
                }
            }
        }
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos pos = context.origin();
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        if (world.getBlockState(pos.offset(0, -1, 0)).isAir() || !world.getBlockState(pos.offset(0, -1, 0)).canOcclude() || world.getBlockState(pos.offset(0, -1, 0)).equals(Blocks.BEDROCK.defaultBlockState()))
            return true;
        int tier = 0;
        if (Rands.chance(4)) tier = -1; //ugly hack to make cobblestone work
        if (Rands.chance(10)) tier = 1;
        if (tier == 1) if (Rands.chance(10)) tier = 2;
        boolean hasRoof = Rands.chance(2);

        //height modification
        int height = Rands.randIntRange(6, 15);
        if (tier == -1) height = Rands.randIntRange(6, 12);
        if (tier == 1) height = Rands.randIntRange(9, 18);
        if (tier == 2) height = Rands.randIntRange(12, 21);
        //floor
        for (int j = -2; j <= 2; j++) {
            for (int k = -2; k <= 2; k++) {
                placeBlockAt(world, pos.offset(j, 0, k), tier);
            }
        }
        //"staircase" area
        for (int i = 0; i < height; i++) {
            for (int j = -2; j <= 2; j++) {
                for (int k = -2; k <= 2; k++) {
                    if (j == -2 || j == 2 || k == -2 || k == 2) {
                        placeBlockAt(world, pos.offset(j, i, k), tier);
                    } else {
                        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            }
        }
        //room area
        int heightTotal = height + Rands.randIntRange(3, 5);
        if (tier == -1) {
            if (hasRoof) {
                heightTotal = height + Rands.randIntRange(2, 3);
            } else {
                heightTotal = height + Rands.randIntRange(3, 4);
            }
        }
        if (tier == 1) heightTotal = height + Rands.randIntRange(4, 6);
        if (tier == 2) heightTotal = height + Rands.randIntRange(5, 7);
        for (int i = height - 2; i < heightTotal; i++) {
            for (int j = -3; j <= 3; j++) {
                for (int k = -3; k <= 3; k++) {
                    if (j == -3 || j == 3 || k == -3 || k == 3) {
                        placeBlockAt(world, pos.offset(j, i, k), tier);
                    } else {
                        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }
                    if (j == -2 && k == -2 && i == height) {
                        world.setBlock(pos.offset(j, i, k), StructurePiece.reorient(world, pos, Blocks.CHEST.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)), 2);
//                        LootableContainerBlockEntity.setLootTable(world, Rands.getRandom(), pos.add(j, i, k), (tier >= 1) ? (tier >= 2) ? LootTables.END_CITY_TREASURE_CHEST : LootTables.SIMPLE_DUNGEON_CHEST : RAALootTables.OUTPOST_LOOT);
                        RandomizableContainerBlockEntity.setLootTable(world, random, pos.offset(j, i, k), (tier >= 2) ? BuiltInLootTables.END_CITY_TREASURE : BuiltInLootTables.SIMPLE_DUNGEON);
                    }
                    if (tier >= 1) {
                        if (j == 2 && k == 2 && i == height) {
                            world.setBlock(pos.offset(j, i, k), StructurePiece.reorient(world, pos, Blocks.CHEST.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)), 2);
                            RandomizableContainerBlockEntity.setLootTable(world, random, pos.offset(j, i, k), (tier >= 2) ? BuiltInLootTables.END_CITY_TREASURE : BuiltInLootTables.SIMPLE_DUNGEON);
                        }
                    }
                    if (tier >= 2) {
                        if (j == -2 && k == 2 && i == height) {
                            world.setBlock(pos.offset(j, i, k), StructurePiece.reorient(world, pos, Blocks.CHEST.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)), 2);
                            RandomizableContainerBlockEntity.setLootTable(world, random, pos.offset(j, i, k), BuiltInLootTables.END_CITY_TREASURE);
                        }
                        if (j == 2 && k == -2 && i == height) {
                            world.setBlock(pos.offset(j, i, k), StructurePiece.reorient(world, pos, Blocks.CHEST.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)), 2);
                            RandomizableContainerBlockEntity.setLootTable(world, random, pos.offset(j, i, k), BuiltInLootTables.END_CITY_TREASURE);
                        }
                    }
                }
            }
        }
        //roof
        if (hasRoof || tier >= 1) {
            for (int j = -3; j <= 3; j++) {
                for (int k = -3; k <= 3; k++) {
                    placeBlockAt(world, pos.offset(j, heightTotal - 1, k), tier);
                }
            }
        }

//        Utils.createSpawnsFile("outpost", world, pos);
        return true;
    }
}
