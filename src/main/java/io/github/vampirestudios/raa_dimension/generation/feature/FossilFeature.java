package io.github.vampirestudios.raa_dimension.generation.feature;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.vampirestudios.raa_dimension.utils.JsonConverter;
import io.github.vampirestudios.raa_dimension.utils.WorldStructureManipulation;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FossilFeature extends Feature<NoneFeatureConfiguration> {
    private JsonConverter converter = new JsonConverter();
    private Map<String, JsonConverter.StructureValues> structures;

    public FossilFeature(Codec<NoneFeatureConfiguration> configDeserializer) {
        super(configDeserializer);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos pos = context.origin();
        WorldGenLevel world = context.level();
        RandomSource rand = context.random();
        NoneFeatureConfiguration config = context.config();
        JsonObject fossil1 = null;
        JsonObject fossil2 = null;
        JsonObject fossil3 = null;
        JsonObject fossil4 = null;
        JsonObject fossil5 = null;
        JsonObject fossil6 = null;
        JsonObject fossil7 = null;
        try {
            Resource towerBasePath = world.getServer().getResourceManager().getResourceOrThrow(new ResourceLocation("raa_dimensions:structures/fossils/fossil01.json"));
            fossil1 = new Gson().fromJson(new InputStreamReader(towerBasePath.open()), JsonObject.class);
            JsonObject finalTowerBase = fossil1;

            Resource towerWallsPath = world.getServer().getResourceManager().getResourceOrThrow(new ResourceLocation("raa_dimensions:structures/fossils/fossil02.json"));
            fossil2 = new Gson().fromJson(new InputStreamReader(towerWallsPath.open()), JsonObject.class);
            JsonObject finalTowerWalls = fossil2;

            Resource towerStairsPath = world.getServer().getResourceManager().getResourceOrThrow(new ResourceLocation("raa_dimensions:structures/fossils/fossil03.json"));
            fossil3 = new Gson().fromJson(new InputStreamReader(towerStairsPath.open()), JsonObject.class);
            JsonObject finalTowerStairs = fossil3;

            Resource towerLaddersPath = world.getServer().getResourceManager().getResourceOrThrow(new ResourceLocation("raa_dimensions:structures/fossils/fossil04.json"));
            fossil4 = new Gson().fromJson(new InputStreamReader(towerLaddersPath.open()), JsonObject.class);
            JsonObject finalTowerLadders = fossil4;

            Resource towerPillarPath = world.getServer().getResourceManager().getResourceOrThrow(new ResourceLocation("raa_dimensions:structures/fossils/fossil05.json"));
            fossil5 = new Gson().fromJson(new InputStreamReader(towerPillarPath.open()), JsonObject.class);
            JsonObject finalTowerPillar = fossil5;

            Resource fossil6Path = world.getServer().getResourceManager().getResourceOrThrow(new ResourceLocation("raa_dimensions:structures/fossils/fossil06.json"));
            fossil6 = new Gson().fromJson(new InputStreamReader(fossil6Path.open()), JsonObject.class);
            JsonObject finalFossil6 = fossil6;

            Resource fossil7Path = world.getServer().getResourceManager().getResourceOrThrow(new ResourceLocation("raa_dimensions:structures/fossils/fossil07.json"));
            fossil7 = new Gson().fromJson(new InputStreamReader(fossil7Path.open()), JsonObject.class);
            JsonObject finalFossil7 = fossil7;

            structures = new HashMap<String, JsonConverter.StructureValues>() {{
                put("fossil1", converter.loadStructure(finalTowerBase));
                put("fossil2", converter.loadStructure(finalTowerWalls));
                put("fossil3", converter.loadStructure(finalTowerStairs));
                put("fossil4", converter.loadStructure(finalTowerLadders));
                put("fossil5", converter.loadStructure(finalTowerPillar));
                put("fossil6", converter.loadStructure(finalFossil6));
                put("fossil7", converter.loadStructure(finalFossil7));
            }};
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (fossil1 == null || fossil2 == null || fossil3 == null || fossil4 == null || fossil5 == null || fossil6 == null) {
            System.out.println("Can't get the file");
            return true;
        }

        if (pos.getY() < 9 || !world.getBlockState(pos.offset(0, -1, 0)).canOcclude() || world.getBlockState(pos.offset(0, -1, 0)).equals(Blocks.BEDROCK.defaultBlockState()))
            return true;

        int yChosen = new Random().nextInt(25) + 4;
        while (pos.getY() - yChosen < 5) {
            yChosen = new Random().nextInt(25) + 4;
        }
        pos.offset(0, -yChosen, 0);
        JsonConverter.StructureValues fossilChosen = structures.get("fossil" + (new Random().nextInt(structures.size()) + 1));
        int rotation = new Random().nextInt(4);
        for (int i = 0; i < fossilChosen.getBlockPositions().size(); i++) {
            if (!Rands.chance(6)) {
                Vec3i currBlockPos = fossilChosen.getBlockPositions().get(i);
                String currBlockType = fossilChosen.getBlockTypes().get(fossilChosen.getBlockStates().get(i));
                Map<String, String> currBlockProp = fossilChosen.getBlockProperties().get(fossilChosen.getBlockStates().get(i));

                currBlockPos = WorldStructureManipulation.rotatePos(rotation, currBlockPos, fossilChosen.getSize());

                WorldStructureManipulation.placeBlock(world, pos.offset(currBlockPos), currBlockType, currBlockProp, rotation);
            }
        }

//        Utils.createSpawnsFile("fossil", world, pos);

        return true;
    }
}