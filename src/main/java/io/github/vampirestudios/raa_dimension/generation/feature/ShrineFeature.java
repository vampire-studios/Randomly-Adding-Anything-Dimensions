package io.github.vampirestudios.raa_dimension.generation.feature;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.vampirestudios.raa_dimension.utils.JsonConverter;
import io.github.vampirestudios.raa_dimension.utils.WorldStructureManipulation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ShrineFeature extends Feature<NoneFeatureConfiguration> {
    private JsonConverter converter = new JsonConverter();
    private Map<String, JsonConverter.StructureValues> structures;

    public ShrineFeature(Codec<NoneFeatureConfiguration> function) {
        super(function);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos pos = context.origin();
        WorldGenLevel world = context.level();
        RandomSource rand = context.random();
        NoneFeatureConfiguration config = context.config();
        JsonObject jsonObject = null;
        try {
            Resource path = world.getServer().getResourceManager().getResourceOrThrow(new ResourceLocation("raa_dimensions:structures/shrine/shrine.json"));
            jsonObject = new Gson().fromJson(new InputStreamReader(path.open()), JsonObject.class);
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
                WorldStructureManipulation.placeBlock(world, pos.offset(currBlockPos), "minecraft:air", new HashMap<>(), rotation);
            } else {
                Registry<DimensionType> registry = world.registryAccess().ownedRegistryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
                switch (currBlockType) {
                    case "minecraft:stone_bricks" ->
                            WorldStructureManipulation.placeBlock(world, pos.offset(currBlockPos), "raa_dimensions:" + registry.getKey(world.dimensionType()).getPath().substring(4) + "_stone_bricks", new HashMap<>(), rotation);
                    case "minecraft:chiseled_stone_bricks" ->
                            WorldStructureManipulation.placeBlock(world, pos.offset(currBlockPos), "raa_dimensions:chiseled_" + registry.getKey(world.dimensionType()).getPath().substring(4) + "_stone_bricks", new HashMap<>(), rotation);
                    case "minecraft:stone" ->
                            WorldStructureManipulation.placeBlock(world, pos.offset(currBlockPos), "raa_dimensions:" + registry.getKey(world.dimensionType()).getPath() + "_stone", new HashMap<>(), rotation);
                    case "minecraft:stone_brick_slab" ->
                            WorldStructureManipulation.placeBlock(world, pos.offset(currBlockPos), "raa_dimensions:" + registry.getKey(world.dimensionType()).getPath() + "_stone_brick_slab", new HashMap<>(), rotation);
                    case "minecraft:stone_slab" ->
                            WorldStructureManipulation.placeBlock(world, pos.offset(currBlockPos), "raa_dimensions:" + registry.getKey(world.dimensionType()).getPath() + "_stone_slab", currBlockProp, rotation);
                    case "minecraft:stone_brick_stairs" ->
                            WorldStructureManipulation.placeBlock(world, pos.offset(currBlockPos), "raa_dimensions:" + registry.getKey(world.dimensionType()).getPath() + "_stone_brick_stairs", currBlockProp, rotation);
                    case "minecraft:ladder" ->
                            WorldStructureManipulation.placeBlock(world, pos.offset(currBlockPos), currBlockType, new HashMap<>(), 4 - rotation);
                    default ->
                            WorldStructureManipulation.placeBlock(world, pos.offset(currBlockPos), currBlockType, new HashMap<>(), rotation);
                }
            }
        }

//        Utils.createSpawnsFile("shrine", world, pos);

        return true;
    }
}