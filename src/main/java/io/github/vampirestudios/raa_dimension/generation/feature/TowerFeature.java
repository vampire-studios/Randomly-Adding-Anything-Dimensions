package io.github.vampirestudios.raa_dimension.generation.feature;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.vampirestudios.raa_dimension.utils.JsonConverter;
import io.github.vampirestudios.raa_dimension.utils.WorldStructureManipulation;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.resource.Resource;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class TowerFeature extends Feature<DefaultFeatureConfig> {
    private JsonConverter converter = new JsonConverter();
    private Map<String, JsonConverter.StructureValues> structures;

    public TowerFeature(Codec<DefaultFeatureConfig> function) {
        super(function);
    }

    private static void placePiece(ServerWorldAccess world, BlockPos pos, int rotation, JsonConverter.StructureValues piece, int decay) {
        for (int i = 0; i < piece.getBlockPositions().size(); i++) {
            Vec3i currBlockPos = piece.getBlockPositions().get(i);
            String currBlockType = piece.getBlockTypes().get(piece.getBlockStates().get(i));
//            Map<String, String> currBlockProp = piece.getBlockProperties().get(piece.getBlockStates().get(i));

            //Rotate
            currBlockPos = WorldStructureManipulation.rotatePos(rotation, currBlockPos, piece.getSize());

            //Spawn blocks
            if (currBlockType.equals("minecraft:air") || (decay > 0 && Rands.chance(14 - decay))) {
                WorldStructureManipulation.placeBlock(world, pos.add(currBlockPos), "minecraft:air", new HashMap<>(), rotation);
            } else {
                switch (currBlockType) {
                    case "minecraft:stone_bricks":
                        WorldStructureManipulation.placeBlock(world, pos.add(currBlockPos), "raa_dimensions:" + (world.getDimension().getSuffix()).substring(4) + "_stone_bricks", new HashMap<>(), rotation);
                        break;
                    case "minecraft:chiseled_stone_bricks":
                        WorldStructureManipulation.placeBlock(world, pos.add(currBlockPos), "raa_dimensions:" + "chiseled_" + (world.getDimension().getSuffix()).substring(4), new HashMap<>(), rotation);
                        break;
                    case "minecraft:ladder":
                        WorldStructureManipulation.placeBlock(world, pos.add(currBlockPos), currBlockType, new HashMap<>(), 4 - rotation);
                        break;
                    default:
                        WorldStructureManipulation.placeBlock(world, pos.add(currBlockPos), currBlockType, new HashMap<>(), rotation);
                        break;
                }
            }
        }
    }

    private static void fillWindows(ServerWorldAccess world, BlockPos pos, int fill) {
        //Fill windows part-way if outside or all the way if next to blocks
        for (int i = 0; i < 4; i++) {
            float xPart = 6.5f - 5.5f * MathHelper.cos((float) (Math.PI / 2 * i));
            float zPart = 6.5f - 5.5f * MathHelper.sin((float) (Math.PI / 2 * i));
            float xPart2 = 6.5f - 6.5f * MathHelper.cos((float) (Math.PI / 2 * i));
            float zPart2 = 6.5f - 6.5f * MathHelper.sin((float) (Math.PI / 2 * i));

            for (int j = 2; j < 4; j++) {
                if (!world.isAir(pos.add(MathHelper.floor(xPart2), j, MathHelper.floor(zPart2))) || (fill + 2 == j) || fill == 2) {
                    world.setBlockState(pos.add(MathHelper.floor(xPart), j, MathHelper.floor(zPart)), Registry.BLOCK.get(Identifier.tryParse("raa_dimensions:" + (world.getDimension().getSuffix()).substring(4) + "_stone_bricks")).getDefaultState(), 2);
                }
                if (!world.isAir(pos.add(MathHelper.ceil(xPart2), j, MathHelper.ceil(zPart2))) || (fill + 2 == j) || fill == 2) {
                    world.setBlockState(pos.add(MathHelper.ceil(xPart), j, MathHelper.ceil(zPart)), Registry.BLOCK.get(Identifier.tryParse("raa_dimensions:" + (world.getDimension().getSuffix()).substring(4) + "_stone_bricks")).getDefaultState(), 2);
                }
            }
        }
    }

    private static void placeDecoration(ServerWorldAccess world, BlockPos pos, int rotation, List<String> blocks, List<Vec3i> blockPos, List<Map<String, String>> blockProps) {
        if (!world.isAir(pos.add(0, -1, 0))) {
            for (int i = 0; i < blockPos.size(); i++) {
                String currBlock = blocks.get(i);
                Vec3i currPos = blockPos.get(i);
                Map<String, String> currProps = blockProps.get(i);
                int x = currPos.getX();
                int z = currPos.getZ();
                int y = currPos.getY();
                int chestType = 0;

                //Rotate
                int xTemp = x;
                x = (x + z) * Math.round(MathHelper.cos((float) (Math.PI / 2f * (rotation - z))));
                z = (xTemp + z) * -Math.round(MathHelper.sin((float) (Math.PI / 2f * (rotation - z))));
                currPos = new Vec3i(x, currPos.getY(), z);

                //Spawn entity
                if (currBlock.equals("armor_stand")) {
                    float standRotation;
                    if (rotation % 2 == 0) {
                        standRotation = -45f;
                    } else {
                        standRotation = 45f;
                    }
                    WorldStructureManipulation.spawnEntity(world, pos.add(currPos), "minecraft:" + currBlock, blockProps.get(i), standRotation);

                    //Spawn block
                } else {
                    if (currBlock.equals("barrel") && blocks.size() != 1) {
                        int rotate = new Random().nextInt(3);
                        currProps.put("facing", (rotate == 0) ? "SOUTH" : (rotate == 1) ? "EAST" : "UP");
                    } else if (currBlock.equals("potted_")) {
                        String[] plants = {"white_tulip", "spruce_sapling", "red_tulip", "red_mushroom", "poppy", "pink_tulip", "oxeye_daisy", "orange_tulip",
                                "oak_sapling", "lily_of_the_valley", "jungle_sapling", "fern", "dead_bush", "dark_oak_sapling", "dandelion", "cactus",
                                "brown_mushroom", "blue_orchid", "birch_sapling", "bamboo", "azure_bluet", "allium", "acacia_sapling", "cornflower"};
                        currBlock += plants[new Random().nextInt(plants.length)];
                    } else if (currBlock.equals("iron_bars")) {
                        if (x == z && y == 0) {
                            currProps.put("north", "TRUE");
                            currProps.put("west", "TRUE");
                            currProps.put("south", "TRUE");
                            currProps.put("east", "TRUE");
                        } else {
                            currProps.put("north", "TRUE");
                            currProps.put("west", "TRUE");
                        }
                    } else if (currBlock.contains("chest")) {
                        if (currBlock.equals("chest1")) {
                            chestType = 1;
                        } else if (currBlock.equals("chest2")) {
                            chestType = 2;
                        } else {
                            chestType = 3;
                        }
                        currBlock = "chest";
                    }

                    WorldStructureManipulation.placeBlock(world, pos.add(currPos), "minecraft:" + currBlock, currProps, rotation);

                    //Chest loot
                    if (chestType == 1) {
                        LootableContainerBlockEntity.setLootTable(world, Rands.getRandom(), pos.add(x, y, z), LootTables.SHIPWRECK_SUPPLY_CHEST);
                    } else if (chestType == 2) {
                        LootableContainerBlockEntity.setLootTable(world, Rands.getRandom(), pos.add(x, y, z), LootTables.VILLAGE_WEAPONSMITH_CHEST);
                    } else if (chestType == 3) {
                        if (Rands.chance(5)) {
                            LootableContainerBlockEntity.setLootTable(world, Rands.getRandom(), pos.add(x, y, z), LootTables.SIMPLE_DUNGEON_CHEST);
                        } else if (Rands.chance(8)) {
                            LootableContainerBlockEntity.setLootTable(world, Rands.getRandom(), pos.add(x, y, z), LootTables.STRONGHOLD_LIBRARY_CHEST);
                        } else {
                            LootableContainerBlockEntity.setLootTable(world, Rands.getRandom(), pos.add(x, y, z), LootTables.VILLAGE_CARTOGRAPHER_CHEST);
                        }
                    }
                }
            }
        }
    }

    private static void placeRoom(ServerWorldAccess world, BlockPos pos, Map<String, JsonConverter.StructureValues> pieces, String type, int decay) {
        //walls
        placePiece(world, pos.add(1, 0, 1), 0, pieces.get("tower_walls"), decay + 2);
        //stairs/ladders
        if (Rands.chance(2)) {
            placePiece(world, pos, new Random().nextInt(4), pieces.get("tower_stairs"), decay - 1);
        } else {
            placePiece(world, pos, new Random().nextInt(4), pieces.get("tower_ladders"), decay - 1);
        }

        //Populate corner items
        String cornerBlocksString = "barrel, barrel, barrel, barrel; wall_torch; smoker; furnace; crafting_table; " +
                "iron_bars, iron_bars, iron_bars, iron_bars; oak_fence, oak_pressure_plate; smithing_table; blast_furnace; armor_stand; " +
                "oak_stairs, oak_stairs, white_carpet, red_carpet; oak_fence, oak_pressure_plate, oak_stairs; scaffolding, potted_; bookshelf, bookshelf; armor_stand";
        String cornerPosString = "0 0 0, 0 0 1, 1 0 0, 0 1 0; 0 1 0; 0 0 0; 0 0 0; 0 0 0; " +
                "0 0 0, 0 0 1, 1 0 0, 0 1 0; 0 0 0, 0 1 0; 0 0 0; 0 0 0; 0 0 0; " +
                "0 0 0, 0 0 1, 0 1 0, 0 1 1; 0 0 0, 0 1 0, 0 0 1; 0 0 0, 0 1 0; 0 0 0, 0 1 0; 0 0 0";
        String cornerPropsString = "facing:UP, facing:UP, facing:UP, facing:UP; facing:SOUTH; facing:SOUTH; facing:SOUTH; NULL; " +
                "NULL, NULL, NULL, NULL; NULL, NULL; NULL; facing:SOUTH; NULL; " +
                "facing:NORTH half:TOP shape:STRAIGHT, facing:SOUTH half:TOP shape:STRAIGHT, NULL, NULL; NULL, NULL, facing:SOUTH half:BOTTOM shape:STRAIGHT; distance:0, NULL; NULL, NULL; NULL";

        //Populate center items
        String centerBlocksString = "chest1, air; barrel; stonecutter; tnt; oak_fence, oak_pressure_plate; " +
                "chest2, air; iron_bars; damaged_anvil; grindstone; armor_stand; " +
                "chest3, air; bell; scaffolding, oak_pressure_plate; scaffolding, lantern";
        String centerPosString = "0 0 0, 0 0 1; 0 0 0; 0 0 0; 0 0 0; 0 0 0, 0 1 0; " +
                "0 0 0, 0 0 1; 0 0 0; 0 0 0; 0 0 0; 0 0 0; " +
                "0 0 0, 0 0 1; 0 1 0; 0 0 0, 0 1 0; 0 0 0, 0 1 0";
        String centerPropsString = "facing:SOUTH type:SINGLE, NULL; facing:UP; facing:SOUTH; NULL; NULL, NULL; " +
                "facing:SOUTH type:SINGLE, NULL; west:TRUE east:TRUE; facing:WEST; face:FLOOR facing:WEST; armor:ALL; " +
                "facing:SOUTH type:SINGLE, NULL; attachment:SINGLE_WALL facing:NORTH; distance:0, NULL; distance:0, NULL";

        List<List<String>> cornerBlocks = new ArrayList<>();
        List<List<Vec3i>> cornerPos = new ArrayList<>();
        List<List<Map<String, String>>> cornerProps = new ArrayList<>();
        decorateRooms(cornerBlocksString, cornerPosString, cornerPropsString, cornerBlocks, cornerPos, cornerProps);

        List<List<String>> centerBlocks = new ArrayList<>();
        List<List<Vec3i>> centerPos = new ArrayList<>();
        List<List<Map<String, String>>> centerProps = new ArrayList<>();
        decorateRooms(centerBlocksString, centerPosString, centerPropsString, centerBlocks, centerPos, centerProps);

        Random rand = new Random();
        int randIndex;

        //Populate room based on type
        switch (type) {
            case "Storage":
                //Center barrels
                for (int i = 0; i < 8; i++) {
                    world.setBlockState(pos.add(6 + i % 2, 0, 5 + i / 2), Blocks.BARREL.getDefaultState().with(Properties.FACING, Direction.UP), 2);
                    if (i % 6 / 2 != 0) {
                        world.setBlockState(pos.add(6 + i % 2, 1, 5 + i / 2), Blocks.BARREL.getDefaultState().with(Properties.FACING, Direction.UP), 2);
                    }
                }
                world.setBlockState(pos.add(5, 0, 7), Blocks.BARREL.getDefaultState().with(Properties.FACING, Direction.UP), 2);
                world.setBlockState(pos.add(8, 0, 6), Blocks.BARREL.getDefaultState().with(Properties.FACING, Direction.UP), 2);

                //Storage corners
                for (int i = 0; i < 4; i++) {
                    randIndex = rand.nextInt(5);
                    placeDecoration(world, pos.add(3 + 7 * (i / 2), 0, 3 + 7 * Math.round(MathHelper.sin((float) (Math.PI / 3 * i)))), i, cornerBlocks.get(randIndex), cornerPos.get(randIndex), cornerProps.get(randIndex));
                }

                //Storage center
                randIndex = rand.nextInt(5);
                placeDecoration(world, pos.add(5, 0, 6), 3, centerBlocks.get(randIndex), centerPos.get(randIndex), centerProps.get(randIndex));
                randIndex = rand.nextInt(5);
                placeDecoration(world, pos.add(8, 0, 7), 1, centerBlocks.get(randIndex), centerPos.get(randIndex), centerProps.get(randIndex));

                fillWindows(world, pos, 2);
                break;
            case "Armory":
                //Center iron bars
                for (int i = 0; i < 4; i++) {
                    world.setBlockState(pos.add(6 + i % 2, 0, 5 + i / 2 * 3), Blocks.IRON_BARS.getDefaultState().with(Properties.WEST, true).with(Properties.EAST, true), 2);
                }
                world.setBlockState(pos.add(5, 0, 5), Blocks.IRON_BARS.getDefaultState().with(Properties.SOUTH, true).with(Properties.EAST, true), 2);
                world.setBlockState(pos.add(5, 0, 8), Blocks.IRON_BARS.getDefaultState().with(Properties.NORTH, true).with(Properties.EAST, true), 2);
                world.setBlockState(pos.add(8, 0, 5), Blocks.IRON_BARS.getDefaultState().with(Properties.SOUTH, true).with(Properties.WEST, true), 2);
                world.setBlockState(pos.add(8, 0, 8), Blocks.IRON_BARS.getDefaultState().with(Properties.NORTH, true).with(Properties.WEST, true), 2);
                world.setBlockState(pos.add(5, 0, 7), Blocks.IRON_BARS.getDefaultState().with(Properties.NORTH, true).with(Properties.SOUTH, true), 2);
                world.setBlockState(pos.add(8, 0, 6), Blocks.IRON_BARS.getDefaultState().with(Properties.NORTH, true).with(Properties.SOUTH, true), 2);

                //Armory corners
                for (int i = 0; i < 4; i++) {
                    randIndex = rand.nextInt(5) + 5;
                    placeDecoration(world, pos.add(3 + 7 * (i / 2), 0, 3 + 7 * Math.round(MathHelper.sin((float) (Math.PI / 3 * i)))), i, cornerBlocks.get(randIndex), cornerPos.get(randIndex), cornerProps.get(randIndex));
                }

                //Armory center
                randIndex = rand.nextInt(5) + 5;
                placeDecoration(world, pos.add(5, 0, 6), 3, centerBlocks.get(randIndex), centerPos.get(randIndex), centerProps.get(randIndex));
                randIndex = rand.nextInt(5) + 5;
                placeDecoration(world, pos.add(8, 0, 7), 1, centerBlocks.get(randIndex), centerPos.get(randIndex), centerProps.get(randIndex));

                fillWindows(world, pos, 0);
                break;
            case "Barracks":
                //Center Books/Beds
                for (int i = 0; i < 4; i++) {
                    world.setBlockState(pos.add(6 + i % 2, 0, 6 + i / 2), Blocks.OAK_PLANKS.getDefaultState(), 2);
                    world.setBlockState(pos.add(6 + i % 2, 1, 6 + i / 2), Blocks.BOOKSHELF.getDefaultState(), 2);
                }
                List<String> bedSheets = Arrays.asList("white_carpet", "red_carpet");
                List<Vec3i> bedPos = Arrays.asList(Vec3i.ZERO, new Vec3i(0, 0, 1), new Vec3i(0, 1, 0), new Vec3i(0, 1, 1));
                List<Map<String, String>> bedProps = Arrays.asList(ImmutableMap.of("facing", "NORTH", "half", "TOP", "shape", "STRAIGHT"), ImmutableMap.of("facing", "SOUTH", "half", "TOP", "shape", "STRAIGHT"), new HashMap<>(), new HashMap<>());
                for (int i = 0; i < 4; i++) {
                    int x = 5 + 3 * (i / 2);
                    int z = 5 + 3 * Math.round(MathHelper.sin((float) (Math.PI / 3 * i)));
                    List<String> bedItems = Arrays.asList("oak_stairs", "oak_stairs", bedSheets.get((i + 1) % 2), bedSheets.get(i % 2));
                    placeDecoration(world, pos.add(x, 0, z), (i + 1) % 4, bedItems, bedPos, bedProps);
                    if (i % 2 == 0) {
                        List<String> tableItems = Arrays.asList("scaffolding", "oak_pressure_plate");
                        List<Vec3i> tablePos = Arrays.asList(Vec3i.ZERO, new Vec3i(0, 1, 0));
                        List<Map<String, String>> tableProps = Arrays.asList(ImmutableMap.of("distance", "0"), new HashMap<>());
                        placeDecoration(world, pos.add(x - 2 * i + 2, 0, z), i, tableItems, tablePos, tableProps);
                    }
                }

                //Barracks corners
                for (int i = 0; i < 4; i++) {
                    randIndex = rand.nextInt(5) + 10;
                    placeDecoration(world, pos.add(3 + 7 * (i / 2), 0, 3 + 7 * Math.round(MathHelper.sin((float) (Math.PI / 3 * i)))), i, cornerBlocks.get(randIndex), cornerPos.get(randIndex), cornerProps.get(randIndex));
                }

                //Barracks center
                randIndex = rand.nextInt(4) + 10;
                placeDecoration(world, pos.add(5, 0, 6), 3, centerBlocks.get(randIndex), centerPos.get(randIndex), centerProps.get(randIndex));
                randIndex = rand.nextInt(4) + 10;
                placeDecoration(world, pos.add(8, 0, 7), 1, centerBlocks.get(randIndex), centerPos.get(randIndex), centerProps.get(randIndex));

                fillWindows(world, pos, 1);
                break;
            case "Empty2":
                fillWindows(world, pos, 1);
                break;
            default:
                fillWindows(world, pos, 0);
                break;
        }

        //pillar
        if (Rands.chance(2)) {
            placePiece(world, pos.add(6, 0, 6), 0, pieces.get("tower_pillar"), decay);
        }
    }

    private static void decorateRooms(String blocksString, String posString, String propsString, List<List<String>> blocks, List<List<Vec3i>> pos, List<List<Map<String, String>>> props) {
        List<String> temp1;
        temp1 = Arrays.asList(blocksString.split("; "));
        for (String i : temp1) {
            blocks.add(Arrays.asList(i.split(", ")));
        }

        temp1 = Arrays.asList(posString.split("; "));
        for (String i : temp1) {
            List<Vec3i> temp2 = new ArrayList<>();
            String[] temp3 = i.split(", ");
            for (String j : temp3) {
                List<String> temp4 = Arrays.asList(j.split(" "));
                temp2.add(new Vec3i(Integer.parseInt(temp4.get(0)), Integer.parseInt(temp4.get(1)), Integer.parseInt(temp4.get(2))));
            }
            pos.add(temp2);
        }

        temp1 = Arrays.asList(propsString.split("; "));
        for (String i : temp1) {
            List<Map<String, String>> temp2 = new ArrayList<>();
            String[] temp3 = i.split(", ");
            for (String j : temp3) {
                Map<String, String> temp4 = new HashMap<>();
                if (!j.equals("NULL")) {
                    String[] temp5 = j.split(" ");
                    for (String k : temp5) {
                        List<String> temp6 = Arrays.asList(k.split(":"));
                        temp4.put(temp6.get(0), temp6.get(1));
                    }
                }
                temp2.add(temp4);
            }
            props.add(temp2);
        }
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        BlockPos pos = context.getOrigin();
        StructureWorldAccess world = context.getWorld();
        Random rand = context.getRandom();
        DefaultFeatureConfig config = context.getConfig();
        JsonObject towerBase = null;
        JsonObject towerWalls = null;
        JsonObject towerStairs = null;
        JsonObject towerLadders = null;
        JsonObject towerPillar = null;
        JsonObject towerRoof = null;
        try {
            Resource towerBasePath = world.getServer().getResourceManager().getResource(new Identifier("raa_dimensions:structures/tower/tower_base.json"));
            towerBase = new Gson().fromJson(new InputStreamReader(towerBasePath.getInputStream()), JsonObject.class);
            JsonObject finalTowerBase = towerBase;

            Resource towerWallsPath = world.getServer().getResourceManager().getResource(new Identifier("raa_dimensions:structures/tower/tower_walls.json"));
            towerWalls = new Gson().fromJson(new InputStreamReader(towerWallsPath.getInputStream()), JsonObject.class);
            JsonObject finalTowerWalls = towerWalls;

            Resource towerStairsPath = world.getServer().getResourceManager().getResource(new Identifier("raa_dimensions:structures/tower/tower_stairs.json"));
            towerStairs = new Gson().fromJson(new InputStreamReader(towerStairsPath.getInputStream()), JsonObject.class);
            JsonObject finalTowerStairs = towerStairs;

            Resource towerLaddersPath = world.getServer().getResourceManager().getResource(new Identifier("raa_dimensions:structures/tower/tower_ladders.json"));
            towerLadders = new Gson().fromJson(new InputStreamReader(towerLaddersPath.getInputStream()), JsonObject.class);
            JsonObject finalTowerLadders = towerLadders;

            Resource towerPillarPath = world.getServer().getResourceManager().getResource(new Identifier("raa_dimensions:structures/tower/tower_pillar.json"));
            towerPillar = new Gson().fromJson(new InputStreamReader(towerPillarPath.getInputStream()), JsonObject.class);
            JsonObject finalTowerPillar = towerPillar;

            Resource towerRoofPath = world.getServer().getResourceManager().getResource(new Identifier("raa_dimensions:structures/tower/tower_roof.json"));
            towerRoof = new Gson().fromJson(new InputStreamReader(towerRoofPath.getInputStream()), JsonObject.class);
            JsonObject finalTowerRoof = towerRoof;

            structures = new HashMap<String, JsonConverter.StructureValues>() {{
                put("tower_base", converter.loadStructure(finalTowerBase));
                put("tower_walls", converter.loadStructure(finalTowerWalls));
                put("tower_stairs", converter.loadStructure(finalTowerStairs));
                put("tower_ladders", converter.loadStructure(finalTowerLadders));
                put("tower_pillar", converter.loadStructure(finalTowerPillar));
                put("tower_roof", converter.loadStructure(finalTowerRoof));
            }};
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (towerBase == null || towerWalls == null || towerStairs == null || towerLadders == null || towerPillar == null || towerRoof == null) {
            System.out.println("Can't get the file");
            return true;
        }


        //Check if structure can generate in the area
        Vec3i size = structures.get("tower_base").getSize();
        Vec3i tempPos = WorldStructureManipulation.circularSpawnCheck(world, pos, new Vec3i(size.getX(), 9, size.getZ()), 0.25f);
        if (tempPos.compareTo(Vec3i.ZERO) == 0) {
            return true;
        }
        pos = new BlockPos(tempPos);

        //Generate basement
        if (pos.getY() > 10 && Rands.chance(3)) {
            placePiece(world, pos.add(0, -7, 0), 0, structures.get("tower_base"), 0);
            placeRoom(world, pos.add(0, -6, 0), structures, "Storage", -2);
        }
        placePiece(world, pos, 0, structures.get("tower_base"), 0);

        int level;
        int lastFloor = -1;
        int levelChance = new Random().nextInt(20);
        for (level = 0; levelChance < 24 - 7 * level; level++) {
            if (pos.getY() + 7 * level < 248) {
                String roomName;
                int roomNum = new Random().nextInt(3);
                while (roomNum == lastFloor) {
                    roomNum = new Random().nextInt(3);
                }
                lastFloor = roomNum;
                if (roomNum == 0) {
                    roomName = "Armory";
                } else if (roomNum == 1) {
                    roomName = "Barracks";
                } else if (roomNum == 2 && Rands.chance(2)) {
                    roomName = "Empty2";
                } else {
                    roomName = "Empty";
                }
                placeRoom(world, pos.add(0, 1 + level * 7, 0), structures, roomName, 2 * level + 2);
            } else {
                break;
            }
        }

        placePiece(world, pos.add(0, 1 + level * 7, 0), 0, structures.get("tower_roof"), 2 * level + 4);

        //Place in the door
        List<Integer> windowsOpen = Arrays.asList(0, 0, 0, 0);
        int max = 0;
        int index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j < 4; j++) {
                if (world.isAir(pos.add(MathHelper.floor(6.5f + 6.5f * MathHelper.cos((float) Math.PI / 2 * i)), j, MathHelper.floor(6.5f + 6.5f * MathHelper.sin((float) Math.PI / 2 * i))))) {
                    windowsOpen.set(i, windowsOpen.get(i) + 1);
                }
                if (world.isAir(pos.add(MathHelper.ceil(6.5f + 6.5f * MathHelper.cos((float) Math.PI / 2 * i)), j, MathHelper.ceil(6.5f + 6.5f * MathHelper.sin((float) Math.PI / 2 * i))))) {
                    windowsOpen.set(i, windowsOpen.get(i) + 1);
                }
            }
            if (windowsOpen.get(i) > max) {
                max = windowsOpen.get(i);
                index = i;
            }
        }
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                world.setBlockState(pos.add(MathHelper.floor(6.5f + 5.5f * MathHelper.cos((float) Math.PI / 2 * index)), 4 - i, MathHelper.floor(6.5f + 5.5f * MathHelper.sin((float) Math.PI / 2 * index))), Registry.BLOCK.get(Identifier.tryParse("raa_dimensions:" + (world.getDimension().getSuffix()).substring(4) + "_stone_bricks")).getDefaultState(), 2);
                world.setBlockState(pos.add(MathHelper.ceil(6.5f + 5.5f * MathHelper.cos((float) Math.PI / 2 * index)), 4 - i, MathHelper.ceil(6.5f + 5.5f * MathHelper.sin((float) Math.PI / 2 * index))), Registry.BLOCK.get(Identifier.tryParse("raa_dimensions:" + (world.getDimension().getSuffix()).substring(4) + "_stone_bricks")).getDefaultState(), 2);
            } else {
                world.setBlockState(pos.add(MathHelper.floor(6.5f + 5.5f * MathHelper.cos((float) Math.PI / 2 * index)), 4 - i, MathHelper.floor(6.5f + 5.5f * MathHelper.sin((float) Math.PI / 2 * index))), Blocks.AIR.getDefaultState(), 2);
                world.setBlockState(pos.add(MathHelper.ceil(6.5f + 5.5f * MathHelper.cos((float) Math.PI / 2 * index)), 4 - i, MathHelper.ceil(6.5f + 5.5f * MathHelper.sin((float) Math.PI / 2 * index))), Blocks.AIR.getDefaultState(), 2);
            }
        }

        //Record spawn in text file
//        Utils.createSpawnsFile("tower", world, pos);

        return true;
    }
}