package io.github.vampirestudios.raa_dimension.generation.feature;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.vampirestudios.raa_dimension.utils.JsonConverter;
import io.github.vampirestudios.raa_dimension.utils.WorldStructureManipulation;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ShrineFeature extends Feature<DefaultFeatureConfig> {
    private JsonConverter converter = new JsonConverter();
    private Map<String, JsonConverter.StructureValues> structures;

    public ShrineFeature(Codec<DefaultFeatureConfig> function) {
        super(function);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        BlockPos pos = context.getOrigin();
        StructureWorldAccess world = context.getWorld();
        Random rand = context.getRandom();
        DefaultFeatureConfig config = context.getConfig();
        JsonObject jsonObject = null;
        try {
            Resource path = world.getServer().getResourceManager().getResource(new Identifier("raa:structures/shrine/shrine.json"));
            jsonObject = new Gson().fromJson(new InputStreamReader(path.getInputStream()), JsonObject.class);
            JsonObject finalJsonObject = jsonObject;
            structures = new HashMap<String, JsonConverter.StructureValues>() {{
                put("shrine", converter.loadStructure(finalJsonObject));
            }};
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (jsonObject == null) {
            System.out.println("Can't get the file");
            return true;
        }

        Vec3i tempPos = WorldStructureManipulation.circularSpawnCheck(world, pos, structures.get("shrine").getSize(), 0.125f);
        if (tempPos.compareTo(Vec3i.ZERO) == 0) {
            return true;
        }
        pos = new BlockPos(tempPos);

        JsonConverter.StructureValues shrine = structures.get("shrine");
        int rotation = new Random().nextInt(4);
        for (int i = 0; i < shrine.getBlockPositions().size(); i++) {
            String currBlockType = shrine.getBlockTypes().get(shrine.getBlockStates().get(i));
            Vec3i currBlockPos = shrine.getBlockPositions().get(i);
            Map<String, String> currBlockProp = shrine.getBlockProperties().get(shrine.getBlockStates().get(i));

            currBlockPos = WorldStructureManipulation.rotatePos(rotation, currBlockPos, shrine.getSize());

            if (currBlockType.equals("minecraft:air")) {
                WorldStructureManipulation.placeBlock(world, pos.add(currBlockPos), "minecraft:air", new HashMap<>(), rotation);
            } else {
                switch (currBlockType) {
                    case "minecraft:stone_bricks":
                        WorldStructureManipulation.placeBlock(world, pos.add(currBlockPos), "raa:" + (world.getDimension().getSuffix()).substring(4) + "_stone_bricks", new HashMap<>(), rotation);
                        break;
                    case "minecraft:chiseled_stone_bricks":
                        WorldStructureManipulation.placeBlock(world, pos.add(currBlockPos), "raa:chiseled_" + (world.getDimension().getSuffix()).substring(4) + "_stone_bricks", new HashMap<>(), rotation);
                        break;
                    case "minecraft:stone":
                        WorldStructureManipulation.placeBlock(world, pos.add(currBlockPos), "raa:" + (world.getDimension().getSuffix()).substring(4) + "_stone", new HashMap<>(), rotation);
                        break;
                    case "minecraft:stone_brick_slab":
                        WorldStructureManipulation.placeBlock(world, pos.add(currBlockPos), "raa:" + (world.getDimension().getSuffix()).substring(4) + "_stone_brick_slab", new HashMap<>(), rotation);
                        break;
                    case "minecraft:stone_slab":
                        WorldStructureManipulation.placeBlock(world, pos.add(currBlockPos), "raa:" + (world.getDimension().getSuffix()).substring(4) + "_stone_slab", currBlockProp, rotation);
                        break;
                    case "minecraft:stone_brick_stairs":
                        WorldStructureManipulation.placeBlock(world, pos.add(currBlockPos), "raa:" + (world.getDimension().getSuffix()).substring(4) + "_stone_brick_stairs", currBlockProp, rotation);
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

//        Utils.createSpawnsFile("shrine", world, pos);

        return true;
    }
}