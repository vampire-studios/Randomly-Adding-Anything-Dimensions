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

public class UndegroundBeeHiveFeature extends Feature<DefaultFeatureConfig> {
    private final JsonConverter converter = new JsonConverter();
    private Map<String, JsonConverter.StructureValues> structures;

    public UndegroundBeeHiveFeature(Codec<DefaultFeatureConfig> function) {
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
            Resource path = world.getServer().getResourceManager().getResource(new Identifier("raa_dimensions:structures/underground_bee_nest.json"));
            jsonObject = new Gson().fromJson(new InputStreamReader(path.getInputStream()), JsonObject.class);
            JsonObject finalJsonObject = jsonObject;
            structures = new HashMap<String, JsonConverter.StructureValues>() {{
                put("underground_bee_nest", converter.loadStructure(finalJsonObject));
            }};
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (jsonObject == null) {
            System.out.println("Can't get the file");
            return true;
        }

        Vec3i tempPos = WorldStructureManipulation.circularSpawnCheck(world, pos, structures.get("underground_bee_nest").getSize(), 0.125f);
        if (tempPos.compareTo(Vec3i.ZERO) == 0) {
            return true;
        }
        pos = new BlockPos(tempPos);

        JsonConverter.StructureValues shrine = structures.get("underground_bee_nest");
        int rotation = new Random().nextInt(4);
        for (int i = 0; i < shrine.getBlockPositions().size(); i++) {
            String currBlockType = shrine.getBlockTypes().get(shrine.getBlockStates().get(i));
            if (currBlockType.equals("minecraft:cave_air")) {
                Vec3i currBlockPos = shrine.getBlockPositions().get(i);
                Map<String, String> currBlockProp = shrine.getBlockProperties().get(shrine.getBlockStates().get(i));

                currBlockPos = WorldStructureManipulation.rotatePos(rotation, currBlockPos, shrine.getSize());

                WorldStructureManipulation.placeBlock(world, pos.add(currBlockPos), currBlockType, currBlockProp, rotation);
            }
        }

//        Utils.createSpawnsFile("underground_bee_hive", world, pos);

        return true;
    }
}